-- PROJETO: Sistema de Gestão de Livros
-- Integrante 1: Bruna Oppermann - Matrícula: 2106
-- Integrante 2: Débora de Farias Negri - Matrícula: 768
-- Integrante 3: Renata Bernardes Cabral - Matrícula: 698

CREATE DATABASE IF NOT EXISTS SistemaGestaoDeLivros;
USE SistemaGestaoDeLivros;

-- 1. CRIAÇÃO DAS TABELAS

CREATE TABLE Perfil_Usuario (
    idPerfilUsuario INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(100) NOT NULL UNIQUE,
    preferenciasDeLeitura TEXT NOT NULL
);

CREATE TABLE Livro (
    idLivro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    preco DECIMAL(9,2) NOT NULL,
    estoque INT NOT NULL
);

CREATE TABLE Cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(45) NOT NULL UNIQUE,
    dataNascimento DATE NOT NULL,
    fkPerfilUsuario INT NOT NULL,

    CONSTRAINT fk_cliente_perfil
        FOREIGN KEY (fkPerfilUsuario)
        REFERENCES Perfil_Usuario(idPerfilUsuario)
);

CREATE TABLE Pedido (
    idPedido INT AUTO_INCREMENT PRIMARY KEY,
    dataPedido DATETIME NOT NULL,
    fkCliente INT NOT NULL,

    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (fkCliente)
        REFERENCES Cliente(idCliente)
);

CREATE TABLE Item_Pedido (
    fkPedido INT NOT NULL,
    fkLivro INT NOT NULL,
    quantidade INT NOT NULL,
    precoUnitario DECIMAL(9,2) NOT NULL,

    PRIMARY KEY (fkPedido, fkLivro),

    CONSTRAINT fk_item_pedido
        FOREIGN KEY (fkPedido)
        REFERENCES Pedido(idPedido),

    CONSTRAINT fk_item_livro
        FOREIGN KEY (fkLivro)
        REFERENCES Livro(idLivro)
);

CREATE TABLE Pagamento (
    idPagamento INT AUTO_INCREMENT PRIMARY KEY,
    dataPagamento DATETIME NOT NULL,
    formaPagamento VARCHAR(45) NOT NULL,
    valorTotal DECIMAL(9,2) NOT NULL,
    fkPedido INT NOT NULL,

    CONSTRAINT fk_pagamento_pedido
        FOREIGN KEY (fkPedido)
        REFERENCES Pedido(idPedido)
);

-- 2. INSERÇÃO DE DADOS (DML)

INSERT INTO Perfil_Usuario (userName, preferenciasDeLeitura) VALUES
('carlos_reader', 'Ficção Científica e Fantasia'),
('ana_books', 'Romance e Drama'),
('marcos_tech', 'Tecnologia e Engenharia de Computação'),
('julia_lit', 'Literatura Clássica Brasileira'),
('lucas_sci', 'Biomedicina e Ciências da Natureza');

INSERT INTO Livro (titulo, preco, estoque) VALUES
('Estruturas de Dados e Algoritmos em Java', 150.00, 20),
('O Senhor dos Anéis: A Sociedade do Anel', 60.50, 15),
('Física Universitária: Eletromagnetismo', 210.00, 10),
('Dom Casmurro', 35.00, 30),
('Introdução à Engenharia Biomédica', 180.00, 8);

INSERT INTO Cliente (nome, email, dataNascimento, fkPerfilUsuario) VALUES
('Carlos Almeida', 'carlos@email.com', '1998-05-12', 1),
('Ana Paula Souza', 'ana@email.com', '2001-09-23', 2),
('Marcos Silva', 'marcos@email.com', '1999-11-05', 3),
('Julia Ferreira', 'julia@email.com', '2000-02-18', 4),
('Lucas Martins', 'lucas@email.com', '1997-07-30', 5);

INSERT INTO Pedido (dataPedido, fkCliente) VALUES
('2026-05-20 10:30:00', 1),
('2026-05-21 14:15:00', 2),
('2026-05-22 09:00:00', 3),
('2026-05-23 16:45:00', 4),
('2026-05-24 11:20:00', 5);

-- 3. OBJETOS PROGRAMÁVEIS

DELIMITER $$

CREATE TRIGGER trg_atualiza_estoque
BEFORE INSERT ON Item_Pedido
FOR EACH ROW
BEGIN

    DECLARE estoqueAtual INT;

    SELECT estoque
    INTO estoqueAtual
    FROM Livro
    WHERE idLivro = NEW.fkLivro;

    IF estoqueAtual < NEW.quantidade THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Estoque insuficiente';

    ELSE

        UPDATE Livro
        SET estoque = estoque - NEW.quantidade
        WHERE idLivro = NEW.fkLivro;

    END IF;

END $$

CREATE FUNCTION fn_calcula_total_pedido(p_idPedido INT)
RETURNS DECIMAL(9,2)
READS SQL DATA
BEGIN

    DECLARE v_total DECIMAL(9,2);

    SELECT SUM(quantidade * precoUnitario)
    INTO v_total
    FROM Item_Pedido
    WHERE fkPedido = p_idPedido;

    RETURN IFNULL(v_total, 0.00);

END $$

DELIMITER ;

CREATE VIEW vw_relatorio_vendas AS
SELECT
    p.idPedido,
    c.nome AS nomeCliente,
    l.titulo AS tituloLivro,
    ip.quantidade,
    ip.precoUnitario,
    (ip.quantidade * ip.precoUnitario) AS subtotal,
    p.dataPedido

FROM Pedido p
INNER JOIN Cliente c
    ON p.fkCliente = c.idCliente

INNER JOIN Item_Pedido ip
    ON p.idPedido = ip.fkPedido

INNER JOIN Livro l
    ON ip.fkLivro = l.idLivro;

-- 4. INSERT EM Item_Pedido (após a trigger, para atualizar o estoque corretamente)

INSERT INTO Item_Pedido (fkPedido, fkLivro, quantidade, precoUnitario) VALUES
(1, 2, 1, 60.50),
(2, 4, 2, 35.00),
(3, 1, 1, 150.00),
(4, 5, 1, 180.00),
(5, 3, 1, 210.00);

INSERT INTO Pagamento (dataPagamento, formaPagamento, valorTotal, fkPedido) VALUES
('2026-05-20 10:35:00', 'PIX', 60.50, 1),
('2026-05-21 14:20:00', 'Cartão de Crédito', 70.00, 2),
('2026-05-22 09:10:00', 'PIX', 150.00, 3),
('2026-05-23 16:50:00', 'Boleto', 180.00, 4),
('2026-05-24 11:25:00', 'Cartão de Débito', 210.00, 5);

-- 5. CONTROLE DE ACESSO (DCL)

CREATE ROLE IF NOT EXISTS 'role_gerente_livraria';

GRANT SELECT, INSERT, UPDATE
ON SistemaGestaoDeLivros.*
TO 'role_gerente_livraria';

CREATE USER IF NOT EXISTS 'admin_biblio1'@'localhost'
IDENTIFIED BY 'senhaForte123!';

CREATE USER IF NOT EXISTS 'admin_biblio2'@'localhost'
IDENTIFIED BY 'senhaForte456!';

GRANT 'role_gerente_livraria'
TO 'admin_biblio1'@'localhost',
   'admin_biblio2'@'localhost';

SET DEFAULT ROLE 'role_gerente_livraria'
TO 'admin_biblio1'@'localhost',
   'admin_biblio2'@'localhost';