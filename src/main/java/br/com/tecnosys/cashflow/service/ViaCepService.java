package br.com.tecnosys.cashflow.service;

import br.com.tecnosys.cashflow.dto.ViaCepDTO;

public interface ViaCepService {
    ViaCepDTO getEndereco(String cep);
}
