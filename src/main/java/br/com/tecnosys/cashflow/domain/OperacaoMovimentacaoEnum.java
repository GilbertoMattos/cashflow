package br.com.tecnosys.cashflow.domain;

import lombok.Getter;

@Getter
enum OperacaoMovimentacaoEnum {

    E("Entrada"),
    S("Saida");

    private String descricao;

    OperacaoMovimentacaoEnum(String descricao) {
        this.descricao = descricao;
    }
}
