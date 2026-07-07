package com.ClinicSys.api.models.entity;

import java.io.Serializable;

public record Prontuario(
        Long id,
        String descricao,
        String observacao,
        Consulta idConsulta,
        Receituario idReceituario
) implements Serializable {
}
