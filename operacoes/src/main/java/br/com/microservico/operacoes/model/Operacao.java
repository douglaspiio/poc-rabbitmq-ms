package br.com.microservico.operacoes.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "operacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Operacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;
	@NotNull
	@Enumerated(EnumType.STRING)
	private Status status;
	@NotNull
	private Integer codigoOperacao;
	@NotNull
	private Integer quantidadeTitulos;
	@NotNull
	private BigDecimal valorOperacao;
}
