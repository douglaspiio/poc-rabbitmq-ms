package br.com.microservico.pagamentos.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoOperacaoRequest {
	
	private Integer codigoOperacao;
	private Integer quantidadeTitulos;
	private BigDecimal valorAPagar;
	private String identificadorSacado;

}
