package br.com.tecnosys.cashflow.service;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.OperadorDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OperadorService {
    OperadorDTO save(OperadorDTO operador);

    Optional<OperadorDTO> findById(Long id);

    ApiResponse<List<OperadorDTO>> findAll(Pageable pageable);

    OperadorDTO update(Long id, OperadorDTO operador);

    void deleteById(Long id);
}