package br.com.microservico.pagamentos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoOperacaoRequest {
	
	private Integer codigoOperacao;
	private Double valorAPagar;
	private String identificadorSacado;

}
