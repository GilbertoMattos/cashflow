package br.com.tecnosys.cashflow.controller;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.OperadorDTO;
import br.com.tecnosys.cashflow.service.OperadorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/operador")
public class OperadorController {

    private final OperadorService operadorService;

    public OperadorController(OperadorService operadorService) {
        this.operadorService = operadorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OperadorDTO>> createOperador(@Valid @RequestBody OperadorDTO operadorDTO) {
        OperadorDTO createdOperador = operadorService.save(operadorDTO);
        ApiResponse<OperadorDTO> response = new ApiResponse<>(createdOperador);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OperadorDTO>> getOperadorById(@PathVariable Long id) {
        Optional<OperadorDTO> operadorDTO = operadorService.findById(id);
        if (operadorDTO.isPresent()) {
            ApiResponse<OperadorDTO> response = new ApiResponse<>(operadorDTO.get());
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("Operador não encontrado");
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OperadorDTO>>> getAllOperadores(Pageable pageable) {
        ApiResponse<List<OperadorDTO>> operadores = operadorService.findAll(pageable);
        return ResponseEntity.ok(operadores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OperadorDTO>> updateOperador(@PathVariable Long id, 
            @RequestBody OperadorDTO operadorDTO) {
        OperadorDTO updatedOperador = operadorService.update(id, operadorDTO);
        ApiResponse<OperadorDTO> response = new ApiResponse<>(updatedOperador);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOperador(@PathVariable Long id) {
        operadorService.deleteById(id);
        ApiResponse<Void> response = new ApiResponse<>();
        return ResponseEntity.ok(response);
    }
}