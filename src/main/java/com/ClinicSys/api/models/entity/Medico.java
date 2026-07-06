package com.ClinicSys.api.models.entity;

public record Medico(
        Long id,
        String crm,
        String nome,
        String especialidade,
        String email,
        String numero
) {
}
