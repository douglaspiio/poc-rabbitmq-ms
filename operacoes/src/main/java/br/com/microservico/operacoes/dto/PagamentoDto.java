package br.com.microservico.operacoes.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoDto {
	
	private Integer opeCodigo;
    private BigDecimal valor;
}
