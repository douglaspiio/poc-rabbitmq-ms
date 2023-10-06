package br.com.microservico.operacoes.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.microservico.operacoes.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@Builder
public class OperacaoResponse{

	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;
	private Status statusOperacao;
	private Integer codigoOperacao;
	private Integer quantidadeTitulos;
	private Double valorOperacao;

}
