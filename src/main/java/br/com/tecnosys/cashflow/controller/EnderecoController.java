package br.com.tecnosys.cashflow.controller;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import br.com.tecnosys.cashflow.dto.ViaCepDTO;
import br.com.tecnosys.cashflow.service.EnderecoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/endereco")
public class EnderecoController {

    private final EnderecoService viaCepService;

    public EnderecoController(EnderecoService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("{cep}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<ViaCepDTO>> getEndereco(@PathVariable String cep) {
        return ResponseEntity.ok(new ApiResponse<>(viaCepService.getEndereco(cep)));
    }
}
