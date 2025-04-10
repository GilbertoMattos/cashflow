package br.com.tecnosys.cashflow.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmpresaDTO {
    private Long id;
    @NotEmpty(message = "O nome da empresa deve ser informado")
    private String nome;
    @NotEmpty(message = "O cnpj da empresa deve ser informado")
    private String cnpj;

}
