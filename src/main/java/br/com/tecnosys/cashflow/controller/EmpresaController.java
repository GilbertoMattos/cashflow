package br.com.tecnosys.cashflow.controller;

import java.util.List;
import java.util.Optional;

import br.com.tecnosys.cashflow.exception.BusinessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.EmpresaDTO;
import br.com.tecnosys.cashflow.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/empresa")
@Slf4j
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpresaDTO>> createEmpresa(@Valid @RequestBody EmpresaDTO empresaDTO) {
        log.info("Recebida requisição para criar empresa: {}", empresaDTO.getNome());
        EmpresaDTO createdEmpresa = empresaService.save(empresaDTO);
        log.debug("Empresa criada com sucesso: {}", createdEmpresa.getId());
        ApiResponse<EmpresaDTO> response = new ApiResponse<>(createdEmpresa);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpresaDTO>> getEmpresaById(@PathVariable Long id) {
        log.info("Recebida requisição para buscar empresa com ID: {}", id);
        Optional<EmpresaDTO> empresaDTO = empresaService.findById(id);
        if (empresaDTO.isPresent()) {
            log.debug("Empresa encontrada: {}", empresaDTO.get().getNome());
            ApiResponse<EmpresaDTO> response = new ApiResponse<>(empresaDTO.get());
            return ResponseEntity.ok(response);
        } else {
            log.warn("Empresa com ID {} não encontrada", id);
            throw new BusinessException("Empresa não encontrada");
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmpresaDTO>>> getAllEmpresas(Pageable pageable) {
        log.info("Recebida requisição para listar todas as empresas. Page: {}, Size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        ApiResponse<List<EmpresaDTO>> empresas = empresaService.findAll(pageable);
        log.debug("Retornando {} empresas", empresas.getData().size());
        return ResponseEntity.ok(empresas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpresaDTO>> updateEmpresa(@PathVariable Long id,
            @RequestBody EmpresaDTO empresaDTO) {
        log.info("Recebida requisição para atualizar empresa com ID: {}", id);
        EmpresaDTO updatedEmpresa = empresaService.update(id, empresaDTO);
        log.debug("Empresa atualizada com sucesso: {}", updatedEmpresa.getNome());
        ApiResponse<EmpresaDTO> response = new ApiResponse<>(updatedEmpresa);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmpresa(@PathVariable Long id) {
        log.info("Recebida requisição para excluir empresa com ID: {}", id);
        empresaService.deleteById(id);
        log.debug("Empresa com ID {} excluída com sucesso", id);
        ApiResponse<Void> response = new ApiResponse<>();
        return ResponseEntity.ok(response);
    }
}
