package br.com.tecnosys.cashflow.service;

import br.com.tecnosys.cashflow.dto.ViaCepDTO;

public interface EnderecoService {
    ViaCepDTO getEndereco(String cep);
}
