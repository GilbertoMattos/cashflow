package br.com.tecnosys.cashflow.controller;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.EmpresaDTO;
import br.com.tecnosys.cashflow.exception.BusinessException;
import br.com.tecnosys.cashflow.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/empresa")
@Slf4j
@Tag(name = "Empresa", description = "API para gerenciamento de empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova empresa", description = "Cria uma nova empresa com os dados fornecidos")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Empresa criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpresaDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<EmpresaDTO>> createEmpresa(
            @Parameter(description = "Dados da empresa a ser criada") @Valid @RequestBody EmpresaDTO empresaDTO) {
        log.info("Recebida requisição para criar empresa: {}", empresaDTO.getNome());
        EmpresaDTO createdEmpresa = empresaService.save(empresaDTO);
        log.debug("Empresa criada com sucesso: {}", createdEmpresa.getId());
        ApiResponse<EmpresaDTO> response = new ApiResponse<>(createdEmpresa);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empresa por ID", description = "Retorna uma empresa específica pelo seu ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Empresa encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpresaDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Empresa não encontrada",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<EmpresaDTO>> getEmpresaById(
            @Parameter(description = "ID da empresa a ser buscada") @PathVariable Long id) {
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
    @Operation(summary = "Listar todas as empresas", description = "Retorna uma lista paginada de todas as empresas")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de empresas retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpresaDTO.class)))
    })
    public ResponseEntity<ApiResponse<List<EmpresaDTO>>> getAllEmpresas(
            @Parameter(description = "Informações de paginação") Pageable pageable) {
        log.info("Recebida requisição para listar todas as empresas. Page: {}, Size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        ApiResponse<List<EmpresaDTO>> empresas = empresaService.findAll(pageable);
        log.debug("Retornando {} empresas", empresas.getData().size());
        return ResponseEntity.ok(empresas);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa existente")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpresaDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Empresa não encontrada",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<EmpresaDTO>> updateEmpresa(
            @Parameter(description = "ID da empresa a ser atualizada") @PathVariable Long id,
            @Parameter(description = "Novos dados da empresa") @RequestBody EmpresaDTO empresaDTO) {
        log.info("Recebida requisição para atualizar empresa com ID: {}", id);
        EmpresaDTO updatedEmpresa = empresaService.update(id, empresaDTO);
        log.debug("Empresa atualizada com sucesso: {}", updatedEmpresa.getNome());
        ApiResponse<EmpresaDTO> response = new ApiResponse<>(updatedEmpresa);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir empresa", description = "Remove uma empresa existente pelo seu ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Empresa excluída com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Empresa não encontrada",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<Void>> deleteEmpresa(
            @Parameter(description = "ID da empresa a ser excluída") @PathVariable Long id) {
        log.info("Recebida requisição para excluir empresa com ID: {}", id);
        empresaService.deleteById(id);
        log.debug("Empresa com ID {} excluída com sucesso", id);
        ApiResponse<Void> response = new ApiResponse<>();
        return ResponseEntity.ok(response);
    }
}
