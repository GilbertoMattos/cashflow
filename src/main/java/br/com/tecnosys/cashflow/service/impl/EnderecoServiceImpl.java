package br.com.tecnosys.cashflow.service.impl;

import br.com.tecnosys.cashflow.dto.ViaCepDTO;
import br.com.tecnosys.cashflow.exception.BusinessException;
import br.com.tecnosys.cashflow.http.ViaCepFeign;
import br.com.tecnosys.cashflow.service.EnderecoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnderecoServiceImpl implements EnderecoService {

    private final ViaCepFeign viaCepFeign;

    public EnderecoServiceImpl(ViaCepFeign viaCepFeign) {
        this.viaCepFeign = viaCepFeign;
    }

    @Override
    public ViaCepDTO getEndereco(String cep) {
        log.info("Consultando endereco pelo cep {}", cep);
        ResponseEntity<ViaCepDTO> viaCep = viaCepFeign.getViaCep(cep);
        if (viaCep.getBody() == null) {
            throw new BusinessException("Endereco nao encontrado");
        }
        log.debug("Retorno do servico viacep foi {}", viaCep.getBody());
        return viaCep.getBody();
    }
}
