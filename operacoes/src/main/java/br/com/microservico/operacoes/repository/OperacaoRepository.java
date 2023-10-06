package br.com.microservico.operacoes.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.microservico.operacoes.model.Operacao;
import br.com.microservico.operacoes.model.Status;

@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Long> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Operacao o SET o.status = :status, dataAtualizacao = :dataAtualizacao WHERE o.codigoOperacao = :codigoOperacao")
	void atualizaStatus(@Param("status") Status status, @Param("codigoOperacao")Integer codigoOperacao, LocalDateTime dataAtualizacao);

	Optional<Operacao> findByCodigoOperacao(Integer codigoOperacao);

}
