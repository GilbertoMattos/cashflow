package br.com.tecnosys.cashflow.controller;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.OperadorDTO;
import br.com.tecnosys.cashflow.exception.GlobalExceptionHandler;
import br.com.tecnosys.cashflow.service.OperadorService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OperadorControllerTest {

    @Mock
    private OperadorService operadorService;

    @InjectMocks
    private OperadorController operadorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private OperadorDTO operadorDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mockMvc = MockMvcBuilders
                .standaloneSetup(operadorController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                .build();

        operadorDTO = new OperadorDTO();
        operadorDTO.setId(1L);
        operadorDTO.setNome("Operador Teste");
    }

    @Test
    void shouldVerifyApiResponseStructure() throws Exception {
        OperadorDTO dto = new OperadorDTO();
        dto.setId(1L);
        dto.setNome("Test");

        ApiResponse<OperadorDTO> response = new ApiResponse<>(dto);
        String json = objectMapper.writeValueAsString(response);

        assertThat(json).contains("message");
        assertThat(json).contains("data");
    }

    @Test
    @DisplayName("Deve criar um operador com sucesso")
    void createOperador_Success() throws Exception {
        when(operadorService.save(any(OperadorDTO.class))).thenReturn(operadorDTO);

        mockMvc.perform(post("/api/operador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operadorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Operação realizada com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Operador Teste"));
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar criar operador sem nome")
    void createOperador_ValidationError_NomeMissing() throws Exception {
        OperadorDTO invalidOperador = new OperadorDTO();
        invalidOperador.setId(1L);

        mockMvc.perform(post("/api/operador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOperador)))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.message").value("O nome do operador deve ser informado"));
    }

    @Test
    @DisplayName("Deve buscar um operador por ID com sucesso")
    void getOperadorById_Success() throws Exception {
        when(operadorService.findById(1L)).thenReturn(Optional.of(operadorDTO));

        mockMvc.perform(get("/api/operador/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operação realizada com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Operador Teste"));
    }

    @Test
    @DisplayName("Deve retornar erro quando buscar operador com ID inexistente")
    void getOperadorById_NotFound() throws Exception {
        when(operadorService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/operador/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Deve listar todos os operadores com paginação")
    void getAllOperadores_Success() throws Exception {
        List<OperadorDTO> operadorList = Collections.singletonList(operadorDTO);
        ApiResponse<List<OperadorDTO>> response = new ApiResponse<>(operadorList);
        response.setPagination(new ApiResponse.Pagination(0, 10, 1, 1));

        when(operadorService.findAll(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/api/operador")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nome").value("Operador Teste"))
                .andExpect(jsonPath("$.pagination.page").value(0))
                .andExpect(jsonPath("$.pagination.size").value(10))
                .andExpect(jsonPath("$.pagination.totalElements").value(1))
                .andExpect(jsonPath("$.pagination.totalPages").value(1));
    }

    @Test
    @DisplayName("Deve atualizar um operador com sucesso")
    void updateOperador_Success() throws Exception {
        when(operadorService.update(any(Long.class), any(OperadorDTO.class))).thenReturn(operadorDTO);

        mockMvc.perform(put("/api/operador/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operadorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operação realizada com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Operador Teste"));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar atualizar operador inexistente")
    void updateOperador_NotFound() throws Exception {
        when(operadorService.update(any(Long.class), any(OperadorDTO.class)))
                .thenThrow(new RuntimeException("Operador não encontrado"));

        mockMvc.perform(put("/api/operador/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operadorDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Deve excluir um operador com sucesso")
    void deleteOperador_Success() throws Exception {
        doNothing().when(operadorService).deleteById(1L);

        mockMvc.perform(delete("/api/operador/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar excluir operador inexistente")
    void deleteOperador_NotFound() throws Exception {
        doThrow(new RuntimeException("Operador não encontrado"))
                .when(operadorService).deleteById(1L);

        mockMvc.perform(delete("/api/operador/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}