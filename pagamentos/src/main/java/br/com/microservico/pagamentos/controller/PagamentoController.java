package br.com.microservico.pagamentos.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.microservico.pagamentos.dto.PagamentoOperacaoRequest;
import br.com.microservico.pagamentos.dto.PagamentoResponse;
import br.com.microservico.pagamentos.model.Status;
import br.com.microservico.pagamentos.service.PagamentoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

	@Autowired
    private PagamentoService service;
    

    @GetMapping(path = "/listar")
    @ApiOperation(value = "Endpoint responsável por listar todos os pagamentos registrados")
    public List<PagamentoResponse> listarTodos() {
        return service.obterTodos();
    }

    @GetMapping(path = "/listar/{codigoOperacao}")
    @ApiOperation(value = "Endpoint responsável por listar pagamento por codigo da operação")
    public ResponseEntity<PagamentoResponse> detalhar(@PathVariable @NotNull Integer codigoOperacao) {
        return service.obterPorCodigoOperacao(codigoOperacao);
    }

    @PostMapping(path = "/cadastrar")
    @ApiOperation(value = "Endpoint responsável por cadastrar pagamentos")
    public ResponseEntity<PagamentoResponse> cadastrar(@RequestBody @Valid PagamentoOperacaoRequest dto, UriComponentsBuilder uriBuilder) {
    	PagamentoResponse pagamento = service.criarPagamento(dto);
        URI endereco = uriBuilder.path("/pagamentos/{codigoOperacao}").buildAndExpand(pagamento.getCodigoOperacao()).toUri();
        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/atualizar/status/{opeCodigo}")
    @ApiOperation(value = "Endpoint responsável por atualizar status dos pagamentos")
    public ResponseEntity<PagamentoResponse> atualizarStatus(@PathVariable @NotNull Integer opeCodigo, @RequestParam Status status) {
        return service.atualizarStatusPagamento(opeCodigo, status);
    }

    @DeleteMapping("/deletar/{opeCodigo}")
    @ApiOperation(value = "Endpoint responsável por atualizar status dos pagamentos para excluido.")
    public ResponseEntity<PagamentoResponse> atualizarStatusParaExcluido(@PathVariable @NotNull Integer opeCodigo) {
        return service.atualizarStatusPagamento(opeCodigo, Status.EXCLUIDO);
    }

}
