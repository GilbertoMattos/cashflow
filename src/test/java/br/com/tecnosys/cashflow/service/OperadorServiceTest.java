package br.com.tecnosys.cashflow.service;

import br.com.tecnosys.cashflow.domain.Operador;
import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.OperadorDTO;
import br.com.tecnosys.cashflow.repository.OperadorRepository;
import br.com.tecnosys.cashflow.service.impl.OperadorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperadorServiceTest {

    @Mock
    private OperadorRepository operadorRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OperadorServiceImpl operadorService;

    private OperadorDTO operadorDTO;
    private Operador operador;

    @BeforeEach
    void setUp() {
        operadorDTO = criarOperadorDTO();
        operador = criarOperador();
    }

    private OperadorDTO criarOperadorDTO() {
        OperadorDTO dto = new OperadorDTO();
        dto.setId(1L);
        dto.setNome("Operador Teste");
        return dto;
    }

    private Operador criarOperador() {
        Operador op = new Operador();
        op.setId(1L);
        op.setNome("Operador Teste");
        return op;
    }

    @Nested
    @DisplayName("Testes de Criação de Operador")
    class CriacaoOperadorTests {

        @Test
        @DisplayName("Deve criar um operador com sucesso quando dados válidos")
        void save_QuandoDadosValidos_DeveRetornarOperadorCriado() {
            // Arrange
            when(modelMapper.map(any(OperadorDTO.class), eq(Operador.class))).thenReturn(operador);
            when(modelMapper.map(any(Operador.class), eq(OperadorDTO.class))).thenReturn(operadorDTO);
            when(operadorRepository.save(any(Operador.class))).thenReturn(operador);

            // Act
            OperadorDTO resultado = operadorService.save(operadorDTO);

            // Assert
            assertThat(resultado)
                    .isNotNull()
                    .satisfies(dto -> {
                        assertThat(dto.getId()).isEqualTo(operadorDTO.getId());
                        assertThat(dto.getNome()).isEqualTo(operadorDTO.getNome());
                    });

            verify(operadorRepository).save(any(Operador.class));
            verify(modelMapper).map(any(OperadorDTO.class), eq(Operador.class));
            verify(modelMapper).map(any(Operador.class), eq(OperadorDTO.class));
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Operador")
    class BuscaOperadorTests {

        @Test
        @DisplayName("Deve retornar operador quando ID existente")
        void findById_QuandoExiste_DeveRetornarOperador() {
            // Arrange
            when(operadorRepository.findById(1L)).thenReturn(Optional.of(operador));
            when(modelMapper.map(any(Operador.class), eq(OperadorDTO.class))).thenReturn(operadorDTO);

            // Act
            Optional<OperadorDTO> resultado = operadorService.findById(1L);

            // Assert
            assertThat(resultado)
                    .isPresent()
                    .hasValueSatisfying(dto -> {
                        assertThat(dto.getId()).isEqualTo(operadorDTO.getId());
                        assertThat(dto.getNome()).isEqualTo(operadorDTO.getNome());
                    });

            verify(operadorRepository).findById(1L);
            verify(modelMapper).map(any(Operador.class), eq(OperadorDTO.class));
        }

        @Test
        @DisplayName("Deve retornar vazio quando ID não existe")
        void findById_QuandoNaoExiste_DeveRetornarVazio() {
            // Arrange
            when(operadorRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            Optional<OperadorDTO> resultado = operadorService.findById(1L);

            // Assert
            assertThat(resultado).isEmpty();
            verify(operadorRepository).findById(1L);
            verify(modelMapper, never()).map(any(), any());
        }
    }

    @Nested
    @DisplayName("Testes de Listagem de Operadores")
    class ListagemOperadoresTests {

        @Test
        @DisplayName("Deve retornar lista paginada de operadores")
        void findAll_DeveRetornarListaPaginada() {
            // Arrange
            int pageNumber = 0;
            int pageSize = 10;
            long totalElements = 1;

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            List<Operador> operadores = Collections.singletonList(operador);
            Page<Operador> page = new PageImpl<>(operadores, pageable, totalElements);

            when(operadorRepository.findAll(pageable)).thenReturn(page);
            when(modelMapper.map(any(Operador.class), eq(OperadorDTO.class))).thenReturn(operadorDTO);

            // Act
            ApiResponse<List<OperadorDTO>> response = operadorService.findAll(pageable);

            // Assert
            assertThat(response)
                    .isNotNull()
                    .satisfies(resp -> {
                        assertThat(resp.getData())
                                .hasSize(1)
                                .first()
                                .satisfies(dto -> {
                                    assertThat(dto.getId()).isEqualTo(operadorDTO.getId());
                                    assertThat(dto.getNome()).isEqualTo(operadorDTO.getNome());
                                });

                        assertThat(resp.getPagination())
                                .satisfies(pagination -> {
                                    assertThat(pagination.getPage()).isEqualTo(pageNumber);
                                    assertThat(pagination.getSize()).isEqualTo(pageSize);
                                    assertThat(pagination.getTotalElements()).isEqualTo(totalElements);
                                    assertThat(pagination.getTotalPages()).isEqualTo(1);
                                });
                    });

            verify(operadorRepository).findAll(pageable);
            verify(modelMapper).map(any(Operador.class), eq(OperadorDTO.class));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não existem operadores")
        void findAll_QuandoNaoExistemOperadores_DeveRetornarListaVazia() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<Operador> emptyPage = new PageImpl<>(Collections.emptyList());

            when(operadorRepository.findAll(pageable)).thenReturn(emptyPage);

            // Act
            ApiResponse<List<OperadorDTO>> response = operadorService.findAll(pageable);

            // Assert
            assertThat(response)
                    .isNotNull()
                    .satisfies(resp -> {
                        assertThat(resp.getData()).isEmpty();
                        assertThat(resp.getPagination().getTotalElements()).isZero();
                    });

            verify(operadorRepository).findAll(pageable);
            verify(modelMapper, never()).map(any(), any());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Operador")
    class AtualizacaoOperadorTests {

        @Test
        @DisplayName("Deve atualizar operador quando existe")
        void update_QuandoExiste_DeveAtualizarOperador() {
            // Arrange
            when(operadorRepository.findById(1L)).thenReturn(Optional.of(operador));
            when(modelMapper.map(any(OperadorDTO.class), eq(Operador.class))).thenReturn(operador);
            when(modelMapper.map(any(Operador.class), eq(OperadorDTO.class))).thenReturn(operadorDTO);
            when(operadorRepository.save(any(Operador.class))).thenReturn(operador);

            // Act
            OperadorDTO resultado = operadorService.update(1L, operadorDTO);

            // Assert
            assertThat(resultado)
                    .isNotNull()
                    .satisfies(dto -> {
                        assertThat(dto.getId()).isEqualTo(operadorDTO.getId());
                        assertThat(dto.getNome()).isEqualTo(operadorDTO.getNome());
                    });

            verify(operadorRepository).findById(1L);
            verify(operadorRepository).save(any(Operador.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar operador inexistente")
        void update_QuandoNaoExiste_DeveLancarExcecao() {
            // Arrange
            when(operadorRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> operadorService.update(1L, operadorDTO))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Operador não encontrado");

            verify(operadorRepository).findById(1L);
            verify(operadorRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Exclusão de Operador")
    class ExclusaoOperadorTests {

        @Test
        @DisplayName("Deve excluir operador quando existe")
        void delete_QuandoExiste_DeveExcluirOperador() {
            // Arrange
            when(operadorRepository.existsById(1L)).thenReturn(true);
            doNothing().when(operadorRepository).deleteById(1L);

            // Act
            operadorService.deleteById(1L);

            // Assert
            verify(operadorRepository).existsById(1L);
            verify(operadorRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao excluir operador inexistente")
        void delete_QuandoNaoExiste_DeveLancarExcecao() {
            // Arrange
            when(operadorRepository.existsById(1L)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> operadorService.deleteById(1L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Operador não encontrado");

            verify(operadorRepository).existsById(1L);
            verify(operadorRepository, never()).deleteById(any());
        }
    }
}