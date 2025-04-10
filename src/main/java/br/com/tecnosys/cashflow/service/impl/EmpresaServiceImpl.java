package br.com.tecnosys.cashflow.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.tecnosys.cashflow.domain.Empresa;
import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.EmpresaDTO;
import br.com.tecnosys.cashflow.repository.EmpresaRepository;
import br.com.tecnosys.cashflow.service.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, ModelMapper modelMapper) {
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EmpresaDTO save(EmpresaDTO empresa) {
        Empresa savedEmpresa = empresaRepository.save(convertToEntity(empresa));
        return convertToDTO(savedEmpresa);
    }

    @Override
    public Optional<EmpresaDTO> findById(Long id) {
        return empresaRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public ApiResponse<List<EmpresaDTO>> findAll(Pageable pageable) {
        Page<Empresa> empresaPage = empresaRepository.findAll(pageable);
        List<EmpresaDTO> empresas = empresaPage.map(this::convertToDTO).getContent();
        ApiResponse.Pagination pagination = new ApiResponse.Pagination(
                empresaPage.getNumber(),
                empresaPage.getSize(),
                empresaPage.getTotalElements(),
                empresaPage.getTotalPages());
        return new ApiResponse<>(empresas, pagination);
    }

    @Override
    public EmpresaDTO update(Long id, EmpresaDTO empresa) {
        if (!empresaRepository.existsById(id)) {
            throw new RuntimeException("Empresa não encontrada");
        }
        empresa.setId(id);
        Empresa updatedEmpresa = empresaRepository.save(convertToEntity(empresa));
        return convertToDTO(updatedEmpresa);
    }

    @Override
    public void deleteById(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new RuntimeException("Empresa não encontrada");
        }
        empresaRepository.deleteById(id);
    }

    private EmpresaDTO convertToDTO(Empresa empresa) {
        return modelMapper.map(empresa, EmpresaDTO.class);
    }

    private Empresa convertToEntity(EmpresaDTO empresaDTO) {
        return modelMapper.map(empresaDTO, Empresa.class);
    }
}
