package com.ClinicSys.api.models.entity;

public record LoginMedico(
        Long id,
        Medico idMedico,
        String login,
        String senha
) {
}
