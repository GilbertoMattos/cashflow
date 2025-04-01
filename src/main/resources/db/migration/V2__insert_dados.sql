insert into operador (nome, create_at, create_by, version) values ('Gilberto', current_timestamp, 'DataLoad', 0);

insert into empresa (nome, create_at, create_by, version) values ('TecnoInfo', current_timestamp, 'DataLoad', 0);

insert into tipo_mov (nome, descricao, operacao, create_at, create_by, version) values ('Venda', 'Movimentacao de venda de mercadoria','E', current_timestamp, 'DataLoad', 0);
insert into tipo_mov (nome, descricao, operacao, create_at, create_by, version) values ('Deposito', 'Deposito de valor', 'S', current_timestamp, 'DataLoad', 0);