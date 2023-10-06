package br.com.microservico.operacoes.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.microservico.operacoes.dto.OperacaoRequest;
import br.com.microservico.operacoes.dto.OperacaoResponse;
import br.com.microservico.operacoes.model.Operacao;
import br.com.microservico.operacoes.model.Status;
import br.com.microservico.operacoes.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperacaoService {

	@Autowired
    private OperacaoRepository repository;
	@Autowired
    private ModelMapper modelMapper;
	@Autowired
	private RabbitTemplate rabbitTemplate;

    public List<OperacaoResponse> obterTodos() {
        return repository.findAll().stream()
                .map(o -> modelMapper.map(o, OperacaoResponse.class))
                .collect(Collectors.toList());
    }

    public OperacaoResponse obterPorId(Integer opeCodigo) {
    	Operacao operacao = repository.findByCodigoOperacao(opeCodigo)
    	.orElseThrow(EntityNotFoundException::new);
    	
    	return OperacaoResponse.builder()
				.codigoOperacao(operacao.getCodigoOperacao())
				.dataAtualizacao(operacao.getDataAtualizacao())
				.dataCriacao(operacao.getDataCriacao())
				.valorOperacao(operacao.getValorOperacao())
				.statusOperacao(operacao.getStatus())
				.quantidadeTitulos(operacao.getQuantidadeTitulos())
				.build();
    }

    public OperacaoResponse criarOperacao(OperacaoRequest dto) {
        Operacao pedido = modelMapper.map(dto, Operacao.class);

        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setDataAtualizacao(null);
        pedido.setStatus(Status.CRIADA);
        pedido.setQuantidadeTitulos(dto.getQuantidadeTitulos());
        pedido.setValorOperacao(dto.getValor());
        pedido.setCodigoOperacao(obterTodos().size() + 1);
        repository.save(pedido);
        
        OperacaoResponse operacaoResponse = modelMapper.map(pedido, OperacaoResponse.class);
        rabbitTemplate.convertAndSend("operacao.realizada", operacaoResponse);
        
        return operacaoResponse;
    }

    public ResponseEntity<OperacaoResponse> atualizaStatus(Integer codigoOperacao, Status status) {
    	try {
    		Operacao operacao = repository.findByCodigoOperacao(codigoOperacao)
    		    	.orElseThrow(EntityNotFoundException::new);
    		
    		if (operacao == null) {
    			throw new EntityNotFoundException();
    		}
    		repository.atualizaStatus(status, operacao.getCodigoOperacao(), LocalDateTime.now());
    		return ResponseEntity.ok(OperacaoResponse.builder()
    				.codigoOperacao(operacao.getCodigoOperacao())
    				.dataAtualizacao(operacao.getDataAtualizacao())
    				.dataCriacao(operacao.getDataCriacao())
    				.valorOperacao(operacao.getValorOperacao())
    				.statusOperacao(status)
    				.quantidadeTitulos(operacao.getQuantidadeTitulos())
    				.build());
    	}catch(RuntimeException e) {
    		e.printStackTrace();
    		throw new RuntimeException();
    	}
    }
}
