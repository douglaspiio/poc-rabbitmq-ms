package br.com.microservico.pagamentos.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.br.CNPJ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private Double valorRecebido;

    @NotBlank
    @CNPJ
    private String identificadorSacado;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @NotNull
    private Integer codigoOperacao;
    
    @NotNull
    @Positive
    private Double valorOriginal;
    
    @NotNull
	private LocalDateTime dataCriacao;
    
	private LocalDateTime dataAtualizacao;


}
