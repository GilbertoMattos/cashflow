package br.com.tecnosys.cashflow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta padrão da API")
public class ApiResponse<T> {

    @Schema(description = "Mensagem de retorno da operação", example = "Operação realizada com sucesso")
    private String message = "Operação realizada com sucesso";

    @Schema(description = "Dados retornados pela operação")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @Schema(description = "Informações de paginação, quando aplicável")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pagination pagination;

    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(T data) {
        this.message = "Operação realizada com sucesso";
        this.data = data;
    }

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponse(T data, Pagination pagination) {
        this.message = "Operação realizada com sucesso";
        this.data = data;
        this.pagination = pagination;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Informações de paginação")
    public static class Pagination {
        @Schema(description = "Número da página atual", example = "0")
        private int page;

        @Schema(description = "Tamanho da página (itens por página)", example = "20")
        private int size;

        @Schema(description = "Total de elementos no conjunto de dados", example = "100")
        private long totalElements;

        @Schema(description = "Total de páginas", example = "5")
        private int totalPages;
    }
}
