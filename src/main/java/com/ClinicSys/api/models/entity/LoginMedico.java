package com.ClinicSys.api.models.entity;

import java.io.Serializable;

public record LoginMedico(
        Long id,
        Medico idMedico,
        String login,
        String senha
) implements Serializable {
}
