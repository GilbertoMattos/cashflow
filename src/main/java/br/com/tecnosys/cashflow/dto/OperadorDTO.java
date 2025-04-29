package br.com.tecnosys.cashflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representação de um operador")
public class OperadorDTO {
    @Schema(description = "ID único do operador", example = "1")
    private Long id;

    @Schema(description = "Nome do operador", example = "João Silva", required = true)
    @NotEmpty(message = "O nome do operador deve ser informado")
    private String nome;
}
