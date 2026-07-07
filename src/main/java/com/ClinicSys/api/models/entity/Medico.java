package com.ClinicSys.api.models.entity;

import java.io.Serializable;

public record Medico(
        Long id,
        String crm,
        String nome,
        String especialidade,
        String email,
        String numero
) implements Serializable {
}
