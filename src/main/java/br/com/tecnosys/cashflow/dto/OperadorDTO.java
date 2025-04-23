package br.com.tecnosys.cashflow.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperadorDTO {
    private Long id;
    @NotEmpty(message = "O nome do operador deve ser informado")
    private String nome;
}