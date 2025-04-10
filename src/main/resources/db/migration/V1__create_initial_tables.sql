CREATE TABLE empresa (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) NOT NULL,
    create_at TIMESTAMP NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    alter_at TIMESTAMP,
    alter_by VARCHAR(255),
    version INTEGER NOT NULL
);

CREATE TABLE operador (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    create_at TIMESTAMP NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    alter_at TIMESTAMP,
    alter_by VARCHAR(255),
    version INTEGER NOT NULL
);

CREATE TABLE sessao_caixa (
    id BIGSERIAL PRIMARY KEY,
    operador_id BIGINT REFERENCES operador(id) NOT NULL,
    data_abertura TIMESTAMP NOT NULL,
    data_fechamento TIMESTAMP,
    saldo_inicial DECIMAL(10,2) NOT NULL,
    saldo_final DECIMAL(10,2),
    status VARCHAR(50) NOT NULL,
    empresa_id BIGINT REFERENCES empresa(id) NOT NULL,
    create_at TIMESTAMP NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    alter_at TIMESTAMP,
    alter_by VARCHAR(255),
    version INTEGER NOT NULL
);

CREATE TABLE tipo_mov (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    operacao CHAR(1) NOT NULL,
    create_at TIMESTAMP NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    alter_at TIMESTAMP,
    alter_by VARCHAR(255),
    version INTEGER NOT NULL
);

CREATE TABLE movimentacao (
    id BIGSERIAL PRIMARY KEY,
    valor DECIMAL(10,2) NOT NULL,
    tp_mov_id BIGINT REFERENCES tipo_mov(id) NOT NULL,
    data_movimento TIMESTAMP NOT NULL,
    descricao TEXT,
    sessao_caixa_id BIGINT REFERENCES sessao_caixa(id) NOT NULL,
    create_at TIMESTAMP NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    alter_at TIMESTAMP,
    alter_by VARCHAR(255),
    version INTEGER NOT NULL
); 