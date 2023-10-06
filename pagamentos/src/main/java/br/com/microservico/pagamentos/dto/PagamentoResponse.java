package br.com.microservico.pagamentos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.microservico.pagamentos.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagamentoResponse {
	
	    private BigDecimal valorRecebido;
	    private String identificadorSacado;
	    private Status status;
	    private Integer codigoOperacao;
	    private BigDecimal valorOriginal;
		private LocalDateTime dataCriacao;
		private LocalDateTime dataAtualizacao;

}
