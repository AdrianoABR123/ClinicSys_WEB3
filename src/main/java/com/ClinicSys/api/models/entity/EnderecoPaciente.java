package com.ClinicSys.api.models.entity;

public record EnderecoPaciente(
        Long id,
        String cep,
        String rua,
        String bairro,
        String municipio,
        String uf
) {
}
