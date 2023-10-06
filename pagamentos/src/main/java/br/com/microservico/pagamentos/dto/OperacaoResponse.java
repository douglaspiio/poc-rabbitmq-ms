package br.com.microservico.pagamentos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.microservico.pagamentos.model.Status;
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
	private BigDecimal valorOperacao;

}
