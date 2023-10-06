package br.com.microservico.pagamentos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.microservico.pagamentos.dto.OperacaoResponse;
import br.com.microservico.pagamentos.dto.PagamentoOperacaoRequest;
import br.com.microservico.pagamentos.dto.PagamentoResponse;
import br.com.microservico.pagamentos.http.OperacaoClient;
import br.com.microservico.pagamentos.model.Pagamento;
import br.com.microservico.pagamentos.model.Status;
import br.com.microservico.pagamentos.repository.PagamentoRepositoy;

@Service
public class PagamentoService {

	@Autowired
	private PagamentoRepositoy repository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private OperacaoClient operacaoClient;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public List<PagamentoResponse> obterTodos() {
		return repository.findAll().stream().map(p -> modelMapper.map(p, PagamentoResponse.class))
				.collect(Collectors.toList());
	}

	public OperacaoResponse obterPorCodigoOperacao(Long id) {
		Pagamento pagamento = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		return modelMapper.map(pagamento, OperacaoResponse.class);
	}

	public PagamentoResponse criarPagamento(PagamentoOperacaoRequest pagamentoDTO) {
		
		rabbitTemplate.convertAndSend("pagamento.operacao", pagamentoDTO);
		
		OperacaoResponse operacao = operacaoClient.listarPorOpeCodigo(pagamentoDTO.getCodigoOperacao()).getBody();
		Pagamento pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);

		if (null == operacao) {
			throw new EntityNotFoundException();
		}

		repository.save(Pagamento.builder()
				.codigoOperacao(pagamentoDTO.getCodigoOperacao())
				.dataAtualizacao(null)
				.dataCriacao(LocalDateTime.now())
				.identificadorSacado(pagamentoDTO.getIdentificadorSacado())
				.status(validarStatus(operacao.getValorOperacao(),pagamentoDTO.getValorAPagar()))
				.valorOriginal(operacao.getValorOperacao())
				.valorRecebido(pagamentoDTO.getValorAPagar())
				.build());
		OperacaoResponse operacaoResponse = modelMapper.map(pagamento, OperacaoResponse.class);

		if (validarStatus(operacao.getValorOperacao(),pagamentoDTO.getValorAPagar()).equals(Status.CONCLUIDO)) {
			operacaoClient.atualizaStatus(operacao.getCodigoOperacao(), Status.PAGA);
			rabbitTemplate.convertAndSend("operacoes.finalizadas", operacaoResponse);
			rabbitTemplate.convertAndSend("pagamentos.finalizados", operacaoResponse);
		} else if (validarStatus(operacao.getValorOperacao(),pagamentoDTO.getValorAPagar()).equals(Status.PARCIAL)) {
			operacaoClient.atualizaStatus(operacao.getCodigoOperacao(), Status.PAGA_PARCIALMENTE);
			rabbitTemplate.convertAndSend("operacoes.paga-parcialmente", operacaoResponse);
			rabbitTemplate.convertAndSend("pagamentos.parciais", operacaoResponse);
		}

		return PagamentoResponse.builder()
				.codigoOperacao(pagamentoDTO.getCodigoOperacao())
				.dataAtualizacao(null)
				.dataCriacao(LocalDateTime.now())
				.identificadorSacado(pagamentoDTO.getIdentificadorSacado())
				.status(pagamento.getStatus())
				.valorOriginal(pagamento.getValorOriginal())
				.valorRecebido(pagamentoDTO.getValorAPagar())
				.build();
	}

	private Status validarStatus(Double valorOriginal, Double valorPago) {
		if (valorPago.equals(valorOriginal)) {
			return Status.CONCLUIDO;
		} else if (valorPago < valorOriginal) {
			return Status.PARCIAL;
		}
		throw new RuntimeException("Valor ao tentar pagar excedente ao valor pendente.");
	}

	public OperacaoResponse atualizarPagamento(Long id, PagamentoOperacaoRequest dto) {
		Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
		pagamento.setId(id);
		pagamento = repository.save(pagamento);
		return modelMapper.map(pagamento, OperacaoResponse.class);
	}

	public ResponseEntity<PagamentoResponse> obterPorCodigoOperacao(@NotNull Integer codigoOperacao) {
		try {
			Pagamento pagamento = repository.findByCodigoOperacao(codigoOperacao)
					.orElseThrow(() -> new EntityNotFoundException());
			return ResponseEntity.ok(PagamentoResponse.builder()
					.codigoOperacao(pagamento.getCodigoOperacao())
					.dataAtualizacao(pagamento.getDataAtualizacao())
					.dataCriacao(pagamento.getDataCriacao())
					.identificadorSacado(pagamento.getIdentificadorSacado())
					.status(pagamento.getStatus())
					.valorOriginal(pagamento.getValorOriginal())
					.valorRecebido(pagamento.getValorRecebido())
					.build());
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public ResponseEntity<PagamentoResponse> atualizarStatusPagamento(@NotNull Integer opeCodigo, Status status) {
		Pagamento pagamento = repository.findByCodigoOperacao(opeCodigo).orElseThrow(EntityNotFoundException::new);

		try {
			if (null == pagamento) {
				throw new EntityNotFoundException();
			}
			
			pagamento.setStatus(status);
			repository.save(pagamento);
			
			PagamentoResponse pagamentoResponse = PagamentoResponse.builder()
			.codigoOperacao(pagamento.getCodigoOperacao())
			.dataAtualizacao(LocalDateTime.now())
			.dataCriacao(pagamento.getDataCriacao())
			.identificadorSacado(pagamento.getIdentificadorSacado())
			.status(pagamento.getStatus())
			.valorOriginal(pagamento.getValorOriginal())
			.valorRecebido(pagamento.getValorRecebido())
			.build();
			
			if(status.equals(Status.EXCLUIDO)) {
				operacaoClient.atualizaStatus(opeCodigo, Status.EXCLUIDA);
				rabbitTemplate.convertAndSend("pagamentos.recusados",pagamentoResponse);
				rabbitTemplate.convertAndSend("operacao.recusadas", pagamentoResponse);
			}else if(status.equals(Status.CONCLUIDO)) {
				operacaoClient.atualizaStatus(opeCodigo, Status.PAGA);
				rabbitTemplate.convertAndSend("pagamentos.finalizados", pagamentoResponse);
				rabbitTemplate.convertAndSend("operacoes.finalizadas", pagamentoResponse);
			}else if(status.equals(Status.PAGA_PARCIALMENTE)) {
				operacaoClient.atualizaStatus(opeCodigo, Status.PAGA_PARCIALMENTE);
				rabbitTemplate.convertAndSend("operacoes.paga-parcialmente", pagamentoResponse);
				rabbitTemplate.convertAndSend("pagamentos.parciais", pagamentoResponse);
			}else if(status.equals(Status.REJEITADO)) {
				operacaoClient.atualizaStatus(opeCodigo, Status.RECUSADA);
				rabbitTemplate.convertAndSend("pagamentos.recusados", pagamentoResponse);
				rabbitTemplate.convertAndSend("operacao.recusadas", pagamentoResponse);
			}
			
			return ResponseEntity.ok(pagamentoResponse);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

	}
}
