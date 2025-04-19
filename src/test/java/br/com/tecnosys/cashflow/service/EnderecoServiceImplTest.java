package br.com.tecnosys.cashflow.service;

import br.com.tecnosys.cashflow.dto.ViaCepDTO;
import br.com.tecnosys.cashflow.exception.BusinessException;
import br.com.tecnosys.cashflow.http.ViaCepFeign;
import br.com.tecnosys.cashflow.service.impl.EnderecoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceImplTest {

    @Mock
    private ViaCepFeign viaCepFeign;

    @InjectMocks
    private EnderecoServiceImpl enderecoService;

    private ViaCepDTO viaCepDTO;

    @BeforeEach
    void setUp() {
        viaCepDTO = new ViaCepDTO();
        viaCepDTO.setCep("12345-678");
        viaCepDTO.setLogradouro("Rua Teste");
        viaCepDTO.setComplemento("");
        viaCepDTO.setBairro("Centro");
        viaCepDTO.setLocalidade("São Paulo");
        viaCepDTO.setUf("SP");
    }

    @Test
    void getEndereco_QuandoCepValido_DeveRetornarEndereco() {
        // Arrange
        ResponseEntity<ViaCepDTO> responseEntity = ResponseEntity.ok(viaCepDTO);
        when(viaCepFeign.getViaCep(anyString())).thenReturn(responseEntity);

        // Act
        ViaCepDTO resultado = enderecoService.getEndereco("12345-678");

        // Assert
        assertNotNull(resultado);
        assertEquals("12345-678", resultado.getCep());
        assertEquals("Rua Teste", resultado.getLogradouro());
        assertEquals("Centro", resultado.getBairro());
        assertEquals("São Paulo", resultado.getLocalidade());
        assertEquals("SP", resultado.getUf());
    }

    @Test
    void getEndereco_QuandoCepInvalido_DeveLancarBusinessException() {
        // Arrange
        ResponseEntity<ViaCepDTO> responseEntity = ResponseEntity.ok(null);
        when(viaCepFeign.getViaCep(anyString())).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            enderecoService.getEndereco("00000-000");
        });
    }
} 