package br.com.tecnosys.cashflow.service.impl;

import br.com.tecnosys.cashflow.dto.ViaCepDTO;
import br.com.tecnosys.cashflow.http.ViaCepFeign;
import br.com.tecnosys.cashflow.service.ViaCepService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ViaCepServiceImpl implements ViaCepService {

    private final ViaCepFeign viaCepFeign;

    public ViaCepServiceImpl(ViaCepFeign viaCepFeign) {
        this.viaCepFeign = viaCepFeign;
    }

    @Override
    public ViaCepDTO getEndereco(String cep) {
        log.info("Consultando endereco pelo cep {}", cep);
        ResponseEntity<ViaCepDTO> viaCep = viaCepFeign.getViaCep(cep);
        log.debug("Retorno do servico viacep foi {}", viaCep.getBody());
        return viaCep.getBody();
    }
}
