package br.com.tecnosys.cashflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "Representação de uma empresa")
public class EmpresaDTO {
    @Schema(description = "ID único da empresa", example = "1")
    private Long id;

    @Schema(description = "Nome da empresa", example = "Empresa Exemplo", required = true)
    @NotEmpty(message = "O nome da empresa deve ser informado")
    private String nome;

    @Schema(description = "CNPJ da empresa", example = "12.345.678/0001-90", required = true)
    @NotEmpty(message = "O cnpj da empresa deve ser informado")
    private String cnpj;
}
