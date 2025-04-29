package br.com.tecnosys.cashflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de endereço retornados pela API ViaCEP")
public class ViaCepDTO {
    @Schema(description = "CEP formatado", example = "01001-000")
    private String cep;

    @Schema(description = "Nome da rua, avenida, etc.", example = "Praça da Sé")
    private String logradouro;

    @Schema(description = "Complemento do endereço", example = "lado ímpar")
    private String complemento;

    @Schema(description = "Unidade", example = "")
    private String unidade;

    @Schema(description = "Bairro", example = "Sé")
    private String bairro;

    @Schema(description = "Cidade", example = "São Paulo")
    private String localidade;

    @Schema(description = "Sigla do estado", example = "SP")
    private String uf;

    @Schema(description = "Nome do estado", example = "São Paulo")
    private String estado;

    @Schema(description = "Região do país", example = "Sudeste")
    private String regiao;

    @Schema(description = "Código IBGE do município", example = "3550308")
    private String ibge;

    @Schema(description = "Código GIA", example = "1004")
    private String gia;

    @Schema(description = "Código DDD", example = "11")
    private String ddd;

    @Schema(description = "Código SIAFI", example = "7107")
    private String siafi;
}
