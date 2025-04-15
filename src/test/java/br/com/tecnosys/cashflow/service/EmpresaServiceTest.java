package br.com.tecnosys.cashflow.service;

import br.com.tecnosys.cashflow.domain.Empresa;
import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.EmpresaDTO;
import br.com.tecnosys.cashflow.repository.EmpresaRepository;
import br.com.tecnosys.cashflow.service.impl.EmpresaServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    private EmpresaDTO empresaDTO;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        // Arrange - Configuração base
        empresaDTO = criarEmpresaDTO();
        empresa = criarEmpresa();
    }

    // Métodos auxiliares para criar objetos de teste
    private EmpresaDTO criarEmpresaDTO() {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(1L);
        dto.setNome("Empresa Teste");
        dto.setCnpj("12345678901234");
        return dto;
    }

    private Empresa criarEmpresa() {
        Empresa emp = new Empresa();
        emp.setId(1L);
        emp.setNome("Empresa Teste");
        emp.setCnpj("12345678901234");
        return emp;
    }

    @Nested
    @DisplayName("Testes de Criação de Empresa")
    class CriacaoEmpresaTests {

        @Test
        @DisplayName("Deve criar uma empresa com sucesso quando dados válidos")
        void save_QuandoDadosValidos_DeveRetornarEmpresaCriada() {
            // Arrange
            when(modelMapper.map(any(EmpresaDTO.class), eq(Empresa.class))).thenReturn(empresa);
            when(modelMapper.map(any(Empresa.class), eq(EmpresaDTO.class))).thenReturn(empresaDTO);
            when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

            // Act
            EmpresaDTO resultado = empresaService.save(empresaDTO);

            // Assert
            assertThat(resultado)
                    .isNotNull()
                    .satisfies(dto -> {
                        assertThat(dto.getId()).isEqualTo(empresaDTO.getId());
                        assertThat(dto.getNome()).isEqualTo(empresaDTO.getNome());
                        assertThat(dto.getCnpj()).isEqualTo(empresaDTO.getCnpj());
                    });

            verify(empresaRepository).save(any(Empresa.class));
            verify(modelMapper).map(any(EmpresaDTO.class), eq(Empresa.class));
            verify(modelMapper).map(any(Empresa.class), eq(EmpresaDTO.class));
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Empresa")
    class BuscaEmpresaTests {

        @Test
        @DisplayName("Deve retornar empresa quando ID existente")
        void findById_QuandoExiste_DeveRetornarEmpresa() {
            // Arrange
            when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
            when(modelMapper.map(any(Empresa.class), eq(EmpresaDTO.class))).thenReturn(empresaDTO);

            // Act
            Optional<EmpresaDTO> resultado = empresaService.findById(1L);

            // Assert
            assertThat(resultado)
                    .isPresent()
                    .hasValueSatisfying(dto -> {
                        assertThat(dto.getId()).isEqualTo(empresaDTO.getId());
                        assertThat(dto.getNome()).isEqualTo(empresaDTO.getNome());
                        assertThat(dto.getCnpj()).isEqualTo(empresaDTO.getCnpj());
                    });

            verify(empresaRepository).findById(1L);
            verify(modelMapper).map(any(Empresa.class), eq(EmpresaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar vazio quando ID não existe")
        void findById_QuandoNaoExiste_DeveRetornarVazio() {
            // Arrange
            when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            Optional<EmpresaDTO> resultado = empresaService.findById(1L);

            // Assert
            assertThat(resultado).isEmpty();
            verify(empresaRepository).findById(1L);
            verify(modelMapper, never()).map(any(), any());
        }
    }

    @Nested
    @DisplayName("Testes de Listagem de Empresas")
    class ListagemEmpresasTests {

        @Test
        @DisplayName("Deve retornar lista paginada de empresas")
        void findAll_DeveRetornarListaPaginada() {
            // Arrange
            int pageNumber = 0;
            int pageSize = 10;
            long totalElements = 1;

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            List<Empresa> empresas = Collections.singletonList(empresa);
            Page<Empresa> page = new PageImpl<>(empresas, pageable, totalElements);

            when(empresaRepository.findAll(pageable)).thenReturn(page);
            when(modelMapper.map(any(Empresa.class), eq(EmpresaDTO.class))).thenReturn(empresaDTO);

            // Act
            ApiResponse<List<EmpresaDTO>> response = empresaService.findAll(pageable);

            // Assert
            assertThat(response)
                    .isNotNull()
                    .satisfies(resp -> {
                        assertThat(resp.getData())
                                .hasSize(1)
                                .first()
                                .satisfies(dto -> {
                                    assertThat(dto.getId()).isEqualTo(empresaDTO.getId());
                                    assertThat(dto.getNome()).isEqualTo(empresaDTO.getNome());
                                    assertThat(dto.getCnpj()).isEqualTo(empresaDTO.getCnpj());
                                });

                        assertThat(resp.getPagination())
                                .satisfies(pagination -> {
                                    assertThat(pagination.getPage()).isEqualTo(pageNumber);
                                    assertThat(pagination.getSize()).isEqualTo(pageSize);
                                    assertThat(pagination.getTotalElements()).isEqualTo(totalElements);
                                    assertThat(pagination.getTotalPages()).isEqualTo(1);
                                });
                    });

            verify(empresaRepository).findAll(pageable);
            verify(modelMapper).map(any(Empresa.class), eq(EmpresaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não existem empresas")
        void findAll_QuandoNaoExistemEmpresas_DeveRetornarListaVazia() {
            // Arrange
            int pageNumber = 0;
            int pageSize = 10;
            long totalElements = 0;

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Empresa> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, totalElements);

            when(empresaRepository.findAll(pageable)).thenReturn(emptyPage);

            // Act
            ApiResponse<List<EmpresaDTO>> response = empresaService.findAll(pageable);

            // Assert
            assertThat(response)
                    .isNotNull()
                    .satisfies(resp -> {
                        assertThat(resp.getData()).isEmpty();
                        assertThat(resp.getPagination())
                                .satisfies(pagination -> {
                                    assertThat(pagination.getPage()).isEqualTo(pageNumber);
                                    assertThat(pagination.getSize()).isEqualTo(pageSize);
                                    assertThat(pagination.getTotalElements()).isZero();
                                    assertThat(pagination.getTotalPages()).isZero();
                                });
                    });

            verify(empresaRepository).findAll(pageable);
            verify(modelMapper, never()).map(any(), any());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Empresa")
    class AtualizacaoEmpresaTests {

        @Test
        @DisplayName("Deve atualizar empresa quando existe")
        void update_QuandoExiste_DeveAtualizarEmpresa() {
            // Arrange
            when(empresaRepository.existsById(1L)).thenReturn(true);
            when(modelMapper.map(any(EmpresaDTO.class), eq(Empresa.class))).thenReturn(empresa);
            when(modelMapper.map(any(Empresa.class), eq(EmpresaDTO.class))).thenReturn(empresaDTO);
            when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

            // Act
            EmpresaDTO resultado = empresaService.update(1L, empresaDTO);

            // Assert
            assertThat(resultado)
                    .isNotNull()
                    .satisfies(dto -> {
                        assertThat(dto.getId()).isEqualTo(empresaDTO.getId());
                        assertThat(dto.getNome()).isEqualTo(empresaDTO.getNome());
                        assertThat(dto.getCnpj()).isEqualTo(empresaDTO.getCnpj());
                    });

            verify(empresaRepository).existsById(1L);
            verify(empresaRepository).save(any(Empresa.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar empresa inexistente")
        void update_QuandoNaoExiste_DeveLancarExcecao() {
            // Arrange
            when(empresaRepository.existsById(1L)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> empresaService.update(1L, empresaDTO))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Empresa não encontrada");

            verify(empresaRepository).existsById(1L);
            verify(empresaRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Exclusão de Empresa")
    class ExclusaoEmpresaTests {

        @Test
        @DisplayName("Deve excluir empresa quando existe")
        void delete_QuandoExiste_DeveExcluirEmpresa() {
            // Arrange
            when(empresaRepository.existsById(1L)).thenReturn(true);
            doNothing().when(empresaRepository).deleteById(1L);

            // Act
            empresaService.deleteById(1L);

            // Assert
            verify(empresaRepository).existsById(1L);
            verify(empresaRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao excluir empresa inexistente")
        void delete_QuandoNaoExiste_DeveLancarExcecao() {
            // Arrange
            when(empresaRepository.existsById(1L)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> empresaService.deleteById(1L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Empresa não encontrada");

            verify(empresaRepository).existsById(1L);
            verify(empresaRepository, never()).deleteById(any());
        }
    }
}