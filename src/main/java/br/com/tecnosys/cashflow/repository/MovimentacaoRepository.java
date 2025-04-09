package br.com.tecnosys.cashflow.repository;

import br.com.tecnosys.cashflow.domain.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
}
