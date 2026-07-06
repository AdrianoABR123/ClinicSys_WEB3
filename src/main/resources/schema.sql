DROP ALL OBJECTS;

CREATE TABLE endereco_paciente(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cep VARCHAR(9),
    rua VARCHAR(255),
    bairro VARCHAR(255),
    municipio VARCHAR(255),
    uf CHAR(2)
);

CREATE TABLE paciente(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cpf CHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(255),
    id_endereco BIGINT UNIQUE,
    email VARCHAR(255),
    numero VARCHAR(11),
    plano_saude VARCHAR(255),

    FOREIGN KEY(id_endereco)
        REFERENCES endereco_paciente(id)
);

CREATE TABLE medico(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    crm VARCHAR(10) UNIQUE NOT NULL,
    nome VARCHAR(255),
    especialidade VARCHAR(255),
    email VARCHAR(255),
    numero VARCHAR(11)
);

CREATE TABLE login_medico(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_medico BIGINT UNIQUE NOT NULL,
    login VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(100),

    FOREIGN KEY(id_medico)
        REFERENCES medico(id)
        ON DELETE CASCADE
);

CREATE TABLE consulta(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo INT UNIQUE NOT NULL,
    data_hora TIMESTAMP,
    data_hora_volta TIMESTAMP,
    observacao VARCHAR(255),

    id_paciente BIGINT NOT NULL,
    id_medico BIGINT NOT NULL,

    FOREIGN KEY(id_paciente)
        REFERENCES paciente(id),

    FOREIGN KEY(id_medico)
        REFERENCES medico(id),
);

CREATE TABLE medicamento(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo INT UNIQUE NOT NULL,
    nome VARCHAR(255),
    dosagem INT,
    tipo_dosagem VARCHAR(5),
    descricao VARCHAR(255),
    observacao VARCHAR(255)
);

CREATE TABLE receituario(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo INT UNIQUE NOT NULL,
    observacao VARCHAR(255)
);

CREATE TABLE prontuario(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao CLOB,
    observacao VARCHAR(255),

    id_consulta BIGINT UNIQUE NOT NULL,
    id_receituario BIGINT UNIQUE,

    FOREIGN KEY(id_consulta)
        REFERENCES consulta(id)
        ON DELETE CASCADE,

    FOREIGN KEY(id_receituario)
        REFERENCES receituario(id)
);

CREATE TABLE item_receituario(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    intervalo_doses INT,
    observacao VARCHAR(255),

    id_medicamento BIGINT NOT NULL,
    id_receituario BIGINT NOT NULL,

    FOREIGN KEY(id_medicamento)
        REFERENCES medicamento(id),

    FOREIGN KEY(id_receituario)
        REFERENCES receituario(id)
        ON DELETE CASCADE
);