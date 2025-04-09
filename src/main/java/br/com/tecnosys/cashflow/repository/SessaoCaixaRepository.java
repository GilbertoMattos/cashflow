package br.com.tecnosys.cashflow.repository;

import br.com.tecnosys.cashflow.domain.SessaoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoCaixaRepository extends JpaRepository<SessaoCaixa, Long> {
}
