DROP TABLE IF EXISTS item_receituario CASCADE;
DROP TABLE IF EXISTS prontuario CASCADE;
DROP TABLE IF EXISTS receituario CASCADE;
DROP TABLE IF EXISTS medicamento CASCADE;
DROP TABLE IF EXISTS consulta CASCADE;
DROP TABLE IF EXISTS login_medico CASCADE;
DROP TABLE IF EXISTS medico CASCADE;
DROP TABLE IF EXISTS paciente CASCADE;
DROP TABLE IF EXISTS endereco_paciente CASCADE;

CREATE TABLE endereco_paciente(
    id BIGSERIAL PRIMARY KEY,
    cep VARCHAR(9),
    rua VARCHAR(255),
    bairro VARCHAR(255),
    municipio VARCHAR(255),
    uf CHAR(2)
);

CREATE TABLE paciente(
    id BIGSERIAL PRIMARY KEY,
    cpf CHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(255),
    id_endereco BIGINT UNIQUE,
    email VARCHAR(255),
    numero VARCHAR(11),
    plano_saude VARCHAR(255),

    CONSTRAINT fk_endereco
        FOREIGN KEY(id_endereco)
        REFERENCES endereco_paciente(id)
);

CREATE TABLE medico(
    id BIGSERIAL PRIMARY KEY,
    crm VARCHAR(10) UNIQUE NOT NULL,
    nome VARCHAR(255),
    especialidade VARCHAR(255),
    email VARCHAR(255),
    numero VARCHAR(11)
);

CREATE TABLE login_medico(
    id BIGSERIAL PRIMARY KEY,
    id_medico BIGINT UNIQUE NOT NULL,
    login VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(100),

    CONSTRAINT fk_login_medico
        FOREIGN KEY(id_medico)
        REFERENCES medico(id)
        ON DELETE CASCADE
);

CREATE TABLE consulta(
    id BIGSERIAL PRIMARY KEY,
    codigo INTEGER UNIQUE NOT NULL,
    data_hora TIMESTAMP,
    data_hora_volta TIMESTAMP,
    observacao VARCHAR(255),

    id_paciente BIGINT NOT NULL,
    id_medico BIGINT NOT NULL,

    CONSTRAINT fk_consulta_paciente
        FOREIGN KEY(id_paciente)
        REFERENCES paciente(id)
        ON DELETE CASCADE ,

    CONSTRAINT fk_consulta_medico
        FOREIGN KEY(id_medico)
        REFERENCES medico(id)
        ON DELETE CASCADE
);

CREATE TABLE medicamento(
    id BIGSERIAL PRIMARY KEY,
    codigo INTEGER UNIQUE NOT NULL,
    nome VARCHAR(255),
    dosagem INTEGER,
    tipo_dosagem VARCHAR(5),
    descricao VARCHAR(255),
    observacao VARCHAR(255)
);

CREATE TABLE receituario(
    id BIGSERIAL PRIMARY KEY,
    codigo INTEGER UNIQUE NOT NULL,
    observacao VARCHAR(255)
);

CREATE TABLE prontuario(
    id BIGSERIAL PRIMARY KEY,
    descricao TEXT,
    observacao VARCHAR(255),

    id_consulta BIGINT UNIQUE NOT NULL,
    id_receituario BIGINT UNIQUE,

    CONSTRAINT fk_prontuario_consulta
        FOREIGN KEY(id_consulta)
        REFERENCES consulta(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_prontuario_receituario
        FOREIGN KEY(id_receituario)
        REFERENCES receituario(id)
);

CREATE TABLE item_receituario(
    id BIGSERIAL PRIMARY KEY,
    intervalo_doses INTEGER,
    observacao VARCHAR(255),

    id_medicamento BIGINT NOT NULL,
    id_receituario BIGINT NOT NULL,

    CONSTRAINT fk_item_medicamento
        FOREIGN KEY(id_medicamento)
        REFERENCES medicamento(id),

    CONSTRAINT fk_item_receituario
        FOREIGN KEY(id_receituario)
        REFERENCES receituario(id)
        ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION delete_receituario()
RETURNS TRIGGER AS
$$
BEGIN
    IF OLD.id_receituario IS NOT NULL THEN
        DELETE FROM receituario
        WHERE id = OLD.id_receituario;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_delete_receituario
AFTER DELETE ON prontuario
FOR EACH ROW
EXECUTE FUNCTION delete_receituario();