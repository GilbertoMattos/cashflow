package br.com.tecnosys.cashflow.service;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.EmpresaDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

public interface EmpresaService {
    EmpresaDTO save(EmpresaDTO empresa);
    Optional<EmpresaDTO> findById(Long id);
    ApiResponse<List<EmpresaDTO>> findAll(Pageable pageable);
    EmpresaDTO update(Long id, EmpresaDTO empresa);
    void deleteById(Long id);
}
