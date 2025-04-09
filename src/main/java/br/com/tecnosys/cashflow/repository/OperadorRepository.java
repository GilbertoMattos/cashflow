package br.com.tecnosys.cashflow.repository;

import br.com.tecnosys.cashflow.domain.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {
}
