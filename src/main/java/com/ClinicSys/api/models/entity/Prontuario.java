package com.ClinicSys.api.models.entity;

public record Prontuario(
        Long id,
        String descricao,
        String observacao,
        Consulta idConsulta,
        Receituario idReceituario
) {
}
