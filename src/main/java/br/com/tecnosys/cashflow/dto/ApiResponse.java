package br.com.tecnosys.cashflow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
 
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message = "Operação realizada com sucesso";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
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
        this.data = data;
        this.pagination = pagination;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination {
        private int page; // Número da página atual
        private int size; // Tamanho da página (itens por página)
        private long totalElements; // Total de elementos no conjunto de dados
        private int totalPages; // Total de páginas
    }
}