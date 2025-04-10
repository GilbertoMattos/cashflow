package br.com.tecnosys.cashflow.controller;

import java.util.List;
import java.util.Optional;

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

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpresaDTO>> createEmpresa(@Valid @RequestBody EmpresaDTO empresaDTO) {
        EmpresaDTO createdEmpresa = empresaService.save(empresaDTO);
        ApiResponse<EmpresaDTO> response = new ApiResponse<>(createdEmpresa);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpresaDTO>> getEmpresaById(@PathVariable Long id) {
        Optional<EmpresaDTO> empresaDTO = empresaService.findById(id);
        if (empresaDTO.isPresent()) {
            ApiResponse<EmpresaDTO> response = new ApiResponse<>(empresaDTO.get());
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("Empresa não encontrada");
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmpresaDTO>>> getAllEmpresas(Pageable pageable) {
        ApiResponse<List<EmpresaDTO>> empresas = empresaService.findAll(pageable);
        return ResponseEntity.ok(empresas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpresaDTO>> updateEmpresa(@PathVariable Long id,
            @RequestBody EmpresaDTO empresaDTO) {
        EmpresaDTO updatedEmpresa = empresaService.update(id, empresaDTO);
        ApiResponse<EmpresaDTO> response = new ApiResponse<>(updatedEmpresa);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmpresa(@PathVariable Long id) {
        empresaService.deleteById(id);
        ApiResponse<Void> response = new ApiResponse<>();
        return ResponseEntity.ok(response);
    }
}
