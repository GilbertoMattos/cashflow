package br.com.tecnosys.cashflow.service.impl;

import br.com.tecnosys.cashflow.domain.Empresa;
import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.EmpresaDTO;
import br.com.tecnosys.cashflow.repository.EmpresaRepository;
import br.com.tecnosys.cashflow.service.EmpresaService;
import br.com.tecnosys.cashflow.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, ModelMapper modelMapper) {
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EmpresaDTO save(EmpresaDTO empresa) {
        log.debug("Realizando a persistencia da empresa: {}", empresa);
        Empresa savedEmpresa = empresaRepository.save(convertToEntity(empresa));
        log.info("Persistencia da empresa {} realizada com sucesso!", empresa.getNome());
        return convertToDTO(savedEmpresa);
    }

    @Override
    public Optional<EmpresaDTO> findById(Long id) {
        log.debug("Consultando empresa com o id: {}", id);
        return empresaRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public ApiResponse<List<EmpresaDTO>> findAll(Pageable pageable) {
        log.debug("Requisitando empresas todas as empresas");
        Page<Empresa> empresaPage = empresaRepository.findAll(pageable);
        log.info("Foram encontrados {} registros de empresas", empresaPage.getNumberOfElements());
        List<EmpresaDTO> empresas = empresaPage.map(this::convertToDTO).getContent();
        ApiResponse.Pagination pagination = new ApiResponse.Pagination(
                empresaPage.getNumber(),
                empresaPage.getSize(),
                empresaPage.getTotalElements(),
                empresaPage.getTotalPages());
        return new ApiResponse<>(empresas, pagination);
    }

    @Override
    public EmpresaDTO update(Long id, EmpresaDTO empresaDTO) {
        log.debug("Update da empresa {} dados : {}", id, empresaDTO);
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Empresa {} nao encontrada para atualizar", id);
                    return new RuntimeException("Empresa não encontrada");
                });

        Utils.copyNonNullProperties(convertToEntity(empresaDTO), empresa);

        Empresa updatedEmpresa = empresaRepository.save(empresa);
        log.info("Empresa {} atualizada com sucesso", id);
        return convertToDTO(updatedEmpresa);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Delete da empresa {}", id);
        if (!empresaRepository.existsById(id)) {
            log.error("Empresa {} nao encontrada para remover", id);
            throw new RuntimeException("Empresa não encontrada");
        }
        empresaRepository.deleteById(id);
        log.info("Empresa {} removida com sucesso", id);
    }

    private EmpresaDTO convertToDTO(Empresa empresa) {
        return modelMapper.map(empresa, EmpresaDTO.class);
    }

    private Empresa convertToEntity(EmpresaDTO empresaDTO) {
        return modelMapper.map(empresaDTO, Empresa.class);
    }
}
