package br.com.tecnosys.cashflow.controller;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.EmpresaDTO;
import br.com.tecnosys.cashflow.exception.GlobalExceptionHandler;
import br.com.tecnosys.cashflow.service.EmpresaService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
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
public class EmpresaControllerTest {

    @Mock
    private EmpresaService empresaService;

    @InjectMocks
    private EmpresaController empresaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private EmpresaDTO empresaDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mockMvc = MockMvcBuilders
                .standaloneSetup(empresaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                .build();

        empresaDTO = new EmpresaDTO();
        empresaDTO.setId(1L);
        empresaDTO.setNome("Empresa Teste");
        empresaDTO.setCnpj("12345678901234");
    }

    @Test
    void shouldVerifyApiResponseStructure() throws Exception {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(1L);
        dto.setNome("Test");
        dto.setCnpj("12345678901234");

        ApiResponse<EmpresaDTO> response = new ApiResponse<>(dto);
        String json = objectMapper.writeValueAsString(response);

        // Debug
        System.out.println("JSON Structure: " + json);

        // Verificar se o JSON contém todos os campos esperados
        assertThat(json).contains("message");
        assertThat(json).contains("data");
    }

    @Test
    @DisplayName("Deve criar uma empresa com sucesso")
    void createEmpresa_Success() throws Exception {
        when(empresaService.save(any(EmpresaDTO.class))).thenReturn(empresaDTO);

        mockMvc.perform(post("/api/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empresaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Operação realizada com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Empresa Teste"))
                .andExpect(jsonPath("$.data.cnpj").value("12345678901234"));
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar criar empresa sem nome")
    void createEmpresa_ValidationError_NomeMissing() throws Exception {
        EmpresaDTO invalidEmpresa = new EmpresaDTO();
        invalidEmpresa.setId(1L);
        invalidEmpresa.setCnpj("12345678901234");

        mockMvc.perform(post("/api/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmpresa)))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.message").value("O nome da empresa deve ser informado"));
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar criar empresa sem CNPJ")
    void createEmpresa_ValidationError_CnpjMissing() throws Exception {
        EmpresaDTO invalidEmpresa = new EmpresaDTO();
        invalidEmpresa.setId(1L);
        invalidEmpresa.setNome("Empresa Teste");

        mockMvc.perform(post("/api/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmpresa)))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.message").value("O cnpj da empresa deve ser informado"));
    }

    @Test
    @DisplayName("Deve buscar uma empresa por ID com sucesso")
    void getEmpresaById_Success() throws Exception {
        when(empresaService.findById(1L)).thenReturn(Optional.of(empresaDTO));

        mockMvc.perform(get("/api/empresas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operação realizada com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Empresa Teste"))
                .andExpect(jsonPath("$.data.cnpj").value("12345678901234"));
    }

    @Test
    @DisplayName("Deve retornar erro quando buscar empresa com ID inexistente")
    void getEmpresaById_NotFound() throws Exception {
        when(empresaService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/empresas/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Deve listar todas as empresas com paginação")
    void getAllEmpresas_Success() throws Exception {
        List<EmpresaDTO> empresaList = Collections.singletonList(empresaDTO);
        ApiResponse<List<EmpresaDTO>> response = new ApiResponse<>(empresaList);
        response.setPagination(new ApiResponse.Pagination(0, 10, 1, 1));

        when(empresaService.findAll(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/api/empresas")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nome").value("Empresa Teste"))
                .andExpect(jsonPath("$.data[0].cnpj").value("12345678901234"))
                .andExpect(jsonPath("$.pagination.page").value(0))
                .andExpect(jsonPath("$.pagination.size").value(10))
                .andExpect(jsonPath("$.pagination.totalElements").value(1))
                .andExpect(jsonPath("$.pagination.totalPages").value(1));
    }

    @Test
    @DisplayName("Deve atualizar uma empresa com sucesso")
    void updateEmpresa_Success() throws Exception {
        when(empresaService.update(any(Long.class), any(EmpresaDTO.class))).thenReturn(empresaDTO);

        mockMvc.perform(put("/api/empresas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empresaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operação realizada com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Empresa Teste"))
                .andExpect(jsonPath("$.data.cnpj").value("12345678901234"));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar atualizar empresa inexistente")
    void updateEmpresa_NotFound() throws Exception {
        when(empresaService.update(any(Long.class), any(EmpresaDTO.class)))
                .thenThrow(new RuntimeException("Empresa não encontrada"));

        mockMvc.perform(put("/api/empresas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empresaDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Deve excluir uma empresa com sucesso")
    void deleteEmpresa_Success() throws Exception {
        doNothing().when(empresaService).deleteById(1L);

        mockMvc.perform(delete("/api/empresas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar excluir empresa inexistente")
    void deleteEmpresa_NotFound() throws Exception {
        doThrow(new RuntimeException("Empresa não encontrada"))
                .when(empresaService).deleteById(1L);

        mockMvc.perform(delete("/api/empresas/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}