package br.com.tecnosys.cashflow.http;

import br.com.tecnosys.cashflow.dto.ViaCepDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://viacep.com.br", value = "viacep", path = "ws")
public interface ViaCepFeign {

    @GetMapping("{cep}/json")
    ResponseEntity<ViaCepDTO> getViaCep(@PathVariable("cep") String cep);
}
