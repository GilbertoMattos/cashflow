package br.com.tecnosys.cashflow.service.impl;

import br.com.tecnosys.cashflow.domain.Operador;
import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.OperadorDTO;
import br.com.tecnosys.cashflow.repository.OperadorRepository;
import br.com.tecnosys.cashflow.service.OperadorService;
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
public class OperadorServiceImpl implements OperadorService {

    private final OperadorRepository operadorRepository;
    private final ModelMapper modelMapper;

    public OperadorServiceImpl(OperadorRepository operadorRepository, ModelMapper modelMapper) {
        this.operadorRepository = operadorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OperadorDTO save(OperadorDTO operadorDTO) {
        log.debug("Realizando a persistencia do operador: {}", operadorDTO);
        Operador savedOperador = operadorRepository.save(convertToEntity(operadorDTO));
        log.info("Persistencia do operador {} realizada com sucesso!", operadorDTO.getNome());
        return convertToDTO(savedOperador);
    }

    @Override
    public Optional<OperadorDTO> findById(Long id) {
        log.debug("Consultando operador com o id: {}", id);
        return operadorRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public ApiResponse<List<OperadorDTO>> findAll(Pageable pageable) {
        log.debug("Requisitando todos os operadores");
        Page<Operador> operadorPage = operadorRepository.findAll(pageable);
        log.info("Foram encontrados {} registros de operadores", operadorPage.getNumberOfElements());
        List<OperadorDTO> operadores = operadorPage.map(this::convertToDTO).getContent();
        ApiResponse.Pagination pagination = new ApiResponse.Pagination(
                operadorPage.getNumber(),
                operadorPage.getSize(),
                operadorPage.getTotalElements(),
                operadorPage.getTotalPages());
        return new ApiResponse<>(operadores, pagination);
    }

    @Override
    public OperadorDTO update(Long id, OperadorDTO operadorDTO) {
        log.debug("Update do operador {} dados : {}", id, operadorDTO);
        Operador operador = operadorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Operador {} nao encontrado para atualizar", id);
                    return new RuntimeException("Operador não encontrado");
                });

        Utils.copyNonNullProperties(convertToEntity(operadorDTO), operador);

        Operador updatedOperador = operadorRepository.save(operador);
        log.info("Operador {} atualizado com sucesso", id);
        return convertToDTO(updatedOperador);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Delete do operador {}", id);
        if (!operadorRepository.existsById(id)) {
            log.error("Operador {} nao encontrado para remover", id);
            throw new RuntimeException("Operador não encontrado");
        }
        operadorRepository.deleteById(id);
        log.info("Operador {} removido com sucesso", id);
    }

    private OperadorDTO convertToDTO(Operador operador) {
        return modelMapper.map(operador, OperadorDTO.class);
    }

    private Operador convertToEntity(OperadorDTO operadorDTO) {
        return modelMapper.map(operadorDTO, Operador.class);
    }
}