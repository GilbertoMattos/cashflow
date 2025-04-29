package br.com.tecnosys.cashflow.controller;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.OperadorDTO;
import br.com.tecnosys.cashflow.exception.BusinessException;
import br.com.tecnosys.cashflow.service.OperadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/operador")
@Tag(name = "Operador", description = "API para gerenciamento de operadores")
public class OperadorController {

    private final OperadorService operadorService;

    public OperadorController(OperadorService operadorService) {
        this.operadorService = operadorService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo operador", description = "Cria um novo operador com os dados fornecidos")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Operador criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperadorDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<OperadorDTO>> createOperador(
            @Parameter(description = "Dados do operador a ser criado") @Valid @RequestBody OperadorDTO operadorDTO) {
        OperadorDTO createdOperador = operadorService.save(operadorDTO);
        ApiResponse<OperadorDTO> response = new ApiResponse<>(createdOperador);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar operador por ID", description = "Retorna um operador específico pelo seu ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Operador encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperadorDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Operador não encontrado",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<OperadorDTO>> getOperadorById(
            @Parameter(description = "ID do operador a ser buscado") @PathVariable Long id) {
        Optional<OperadorDTO> operadorDTO = operadorService.findById(id);
        if (operadorDTO.isPresent()) {
            ApiResponse<OperadorDTO> response = new ApiResponse<>(operadorDTO.get());
            return ResponseEntity.ok(response);
        } else {
            throw new BusinessException("Operador não encontrado");
        }
    }

    @GetMapping
    @Operation(summary = "Listar todos os operadores", description = "Retorna uma lista paginada de todos os operadores")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de operadores retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperadorDTO.class)))
    })
    public ResponseEntity<ApiResponse<List<OperadorDTO>>> getAllOperadores(
            @Parameter(description = "Informações de paginação") Pageable pageable) {
        ApiResponse<List<OperadorDTO>> operadores = operadorService.findAll(pageable);
        return ResponseEntity.ok(operadores);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar operador", description = "Atualiza os dados de um operador existente")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Operador atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperadorDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Operador não encontrado",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<OperadorDTO>> updateOperador(
            @Parameter(description = "ID do operador a ser atualizado") @PathVariable Long id,
            @Parameter(description = "Novos dados do operador") @RequestBody OperadorDTO operadorDTO) {
        OperadorDTO updatedOperador = operadorService.update(id, operadorDTO);
        ApiResponse<OperadorDTO> response = new ApiResponse<>(updatedOperador);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir operador", description = "Remove um operador existente pelo seu ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Operador excluído com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Operador não encontrado",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> deleteOperador(
            @Parameter(description = "ID do operador a ser excluído") @PathVariable Long id) {
        operadorService.deleteById(id);
        ApiResponse<Void> response = new ApiResponse<>();
        return ResponseEntity.ok(response);
    }
}
