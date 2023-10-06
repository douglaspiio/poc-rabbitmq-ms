package br.com.microservico.pagamentos.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.microservico.pagamentos.dto.OperacaoResponse;
import br.com.microservico.pagamentos.model.Status;

@FeignClient(name = "operacao-ms", url = "http://localhost:8081")
public interface OperacaoClient {
	
    @PutMapping(path = "/operacoes/{opeCodigo}/status")
    void atualizaStatus(@PathVariable Integer opeCodigo, @RequestParam Status status);
    
    @GetMapping(path = "/operacoes/consultar/{opeCodigo}")
    ResponseEntity<OperacaoResponse> listarPorOpeCodigo(@PathVariable Integer opeCodigo);
}
