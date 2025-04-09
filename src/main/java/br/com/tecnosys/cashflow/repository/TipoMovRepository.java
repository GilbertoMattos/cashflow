package br.com.tecnosys.cashflow.repository;

import br.com.tecnosys.cashflow.domain.TipoMov;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMovRepository extends JpaRepository<TipoMov, Long> {
}
