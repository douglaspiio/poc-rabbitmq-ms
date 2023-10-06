package br.com.microservico.pagamentos.dto;

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
	
	    private Double valorRecebido;
	    private String identificadorSacado;
	    private Status status;
	    private Integer codigoOperacao;
	    private Double valorOriginal;
		private LocalDateTime dataCriacao;
		private LocalDateTime dataAtualizacao;

}
