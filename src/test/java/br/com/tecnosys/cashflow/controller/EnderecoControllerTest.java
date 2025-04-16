package br.com.tecnosys.cashflow.controller;

import br.com.tecnosys.cashflow.dto.ViaCepDTO;
import br.com.tecnosys.cashflow.exception.BusinessException;
import br.com.tecnosys.cashflow.exception.GlobalExceptionHandler;
import br.com.tecnosys.cashflow.service.ViaCepService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EnderecoControllerTest {

    private static final String BASE_URL = "/api/endereco";
    private static final String CEP_VALIDO = "01001000";
    private static final String CEP_INVALIDO = "00000000";
    private static final String CEP_NAO_ENCONTRADO = "99999999";
    private static final String CEP_FORMATO_INVALIDO = "123";

    @Mock
    private ViaCepService viaCepService;

    @InjectMocks
    private EnderecoController enderecoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ViaCepDTO viaCepDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(enderecoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        viaCepDTO = criarViaCepDTO();
    }

    private ViaCepDTO criarViaCepDTO() {
        return ViaCepDTO.builder()
                .cep("01001-000")
                .logradouro("Praça da Sé")
                .complemento("lado ímpar")
                .bairro("Sé")
                .localidade("São Paulo")
                .uf("SP")
                .ibge("3550308")
                .gia("1004")
                .ddd("11")
                .siafi("7107")
                .build();
    }

    @Nested
    @DisplayName("Testes de Sucesso")
    class SucessoTests {

        @Test
        @DisplayName("Deve retornar endereço quando CEP válido")
        void getEndereco_QuandoCepValido_DeveRetornarEndereco() throws Exception {
            // Arrange
            when(viaCepService.getEndereco(CEP_VALIDO)).thenReturn(viaCepDTO);

            // Act & Assert
            realizarRequisicaoGet(CEP_VALIDO)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Operação realizada com sucesso"))
                    .andExpect(jsonPath("$.data").exists())
                    .andExpect(jsonPath("$.data.cep").value(viaCepDTO.getCep()))
                    .andExpect(jsonPath("$.data.logradouro").value(viaCepDTO.getLogradouro()))
                    .andExpect(jsonPath("$.data.bairro").value(viaCepDTO.getBairro()))
                    .andExpect(jsonPath("$.data.localidade").value(viaCepDTO.getLocalidade()))
                    .andExpect(jsonPath("$.data.uf").value(viaCepDTO.getUf()));

            verify(viaCepService).getEndereco(CEP_VALIDO);
        }
    }

    @Nested
    @DisplayName("Testes de Erro")
    class ErroTests {

        @Test
        @DisplayName("Deve retornar erro quando CEP não encontrado")
        void getEndereco_QuandoCepNaoEncontrado_DeveRetornarErro() throws Exception {
            // Arrange
            when(viaCepService.getEndereco(anyString()))
                    .thenThrow(new BusinessException("Endereco nao encontrado"));

            // Act & Assert
            realizarRequisicaoGet(CEP_NAO_ENCONTRADO)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Endereco nao encontrado"));

            verify(viaCepService).getEndereco(CEP_NAO_ENCONTRADO);
        }

        @Test
        @DisplayName("Deve retornar erro quando serviço ViaCEP indisponível")
        void getEndereco_QuandoServicoIndisponivel_DeveRetornarErro() throws Exception {
            // Arrange
            when(viaCepService.getEndereco(anyString()))
                    .thenThrow(criarFeignServiceUnavailableException());

            // Act & Assert
            realizarRequisicaoGet(CEP_VALIDO)
                    .andExpect(status().isServiceUnavailable())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Serviço indisponível"));

            verify(viaCepService).getEndereco(CEP_VALIDO);
        }

        @Test
        @DisplayName("Deve retornar erro quando CEP tem formato inválido")
        void getEndereco_QuandoCepFormatoInvalido_DeveRetornarErro() throws Exception {
            // Arrange
            when(viaCepService.getEndereco(anyString()))
                    .thenThrow(new BusinessException("Formato de CEP inválido"));

            // Act & Assert
            realizarRequisicaoGet(CEP_FORMATO_INVALIDO)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Formato de CEP inválido"));

            verify(viaCepService).getEndereco(CEP_FORMATO_INVALIDO);
        }
    }

    private ResultActions realizarRequisicaoGet(String cep) throws Exception {
        return mockMvc.perform(get(BASE_URL + "/{cep}", cep)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private FeignException.ServiceUnavailable criarFeignServiceUnavailableException() {
        return new FeignException.ServiceUnavailable(
                "Serviço indisponível",
                Request.create(Request.HttpMethod.GET, "", Map.of(), null, Charset.defaultCharset(), null),
                null,
                null);
    }
}