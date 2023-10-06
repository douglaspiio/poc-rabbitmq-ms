package br.com.microservico.operacoes.controller;

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

import br.com.microservico.operacoes.dto.OperacaoRequest;
import br.com.microservico.operacoes.dto.OperacaoResponse;
import br.com.microservico.operacoes.model.Status;
import br.com.microservico.operacoes.service.OperacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/operacoes")
@Api(value = "Api de OPERACOES")
@ApiOperation(value = "Api de OPERACOES")
public class OperacaoController {

	@Autowired
	private OperacaoService service;

	@GetMapping(path = "/consultar")
	@ApiOperation(value = "Endpoint responsável pela consulta de todas as operações")
	public List<OperacaoResponse> listarTodos() {
		return service.obterTodos();
	}

	@GetMapping(path = "/consultar/{opeCodigo}")
	@ApiOperation(value = "Endpoint responsável pela consulta de operações pelo codigo da operação")
	public ResponseEntity<OperacaoResponse> listarPorOpeCodigo(@PathVariable @NotNull Integer opeCodigo) {
		return ResponseEntity.ok(service.obterPorId(opeCodigo));
	}

	@PostMapping(path = "/criar")
	@ApiOperation(value = "Endpoint responsável pela criação de operações")
	public ResponseEntity<OperacaoResponse> criarOperacao(@RequestBody @Valid OperacaoRequest dto,
			UriComponentsBuilder uriBuilder) {
		OperacaoResponse pedidoRealizado = service.criarOperacao(dto);
		URI endereco = uriBuilder.path("/operacoes/{id}").buildAndExpand(service.obterTodos().size()).toUri();
		return ResponseEntity.created(endereco).body(pedidoRealizado);

	}

	@PutMapping("/{opeCodigo}/status")
	@ApiOperation(value = "Endpoint responsável pela atualização de status de operações")
	public ResponseEntity<OperacaoResponse> atualizaStatusOperacao(@PathVariable Integer opeCodigo,
			@RequestParam Status status) {
		return service.atualizaStatus(opeCodigo, status);
	}
	
	@DeleteMapping(path= "/apagar")
	@ApiOperation(value = "Endpoint responsável pela exclusão lógidca de operações")
	public ResponseEntity<OperacaoResponse> atualizaStatusOperacao(@PathVariable Integer opeCodigo) {
		return service.atualizaStatus(opeCodigo, Status.EXCLUIDA);
	}
	
}
