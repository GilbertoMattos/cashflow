package br.com.tecnosys.cashflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;           // Ex: "success" ou "error"
    private String message;          // Mensagem descritiva
    private T data;                  // Os dados propriamente ditos (pode ser uma lista paginada)
    private Pagination pagination;   // Metadados de paginação

    // Construtor adicional para facilitar o uso sem paginação
    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination {
        private int page;           // Número da página atual
        private int size;           // Tamanho da página (itens por página)
        private long totalElements; // Total de elementos no conjunto de dados
        private int totalPages;     // Total de páginas
    }
}