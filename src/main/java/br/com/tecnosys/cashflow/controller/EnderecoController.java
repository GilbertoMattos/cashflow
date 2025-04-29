package br.com.tecnosys.cashflow.controller;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.ViaCepDTO;
import br.com.tecnosys.cashflow.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/endereco")
@Tag(name = "Endereço", description = "API para consulta de endereços pelo CEP")
public class EnderecoController {

    private final EnderecoService viaCepService;

    public EnderecoController(EnderecoService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("{cep}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consultar endereço por CEP", description = "Retorna os dados de endereço para o CEP informado")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Endereço encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaCepDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "CEP inválido",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Endereço não encontrado",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<ViaCepDTO>> getEndereco(
            @Parameter(description = "CEP a ser consultado", example = "01001000") @PathVariable String cep) {
        return ResponseEntity.ok(new ApiResponse<>(viaCepService.getEndereco(cep)));
    }
}
