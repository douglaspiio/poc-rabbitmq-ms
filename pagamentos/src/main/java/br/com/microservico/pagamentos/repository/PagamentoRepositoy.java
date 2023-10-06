package br.com.microservico.pagamentos.repository;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.microservico.pagamentos.model.Pagamento;

public interface PagamentoRepositoy extends JpaRepository<Pagamento, Long> {

	Optional<Pagamento> findByCodigoOperacao(@NotNull Integer codigoOperacao);
}
