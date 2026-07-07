package com.ClinicSys.api.models.entity;

import java.io.Serializable;

public record EnderecoPaciente(
        Long id,
        String cep,
        String rua,
        String bairro,
        String municipio,
        String uf
) implements Serializable {
}
