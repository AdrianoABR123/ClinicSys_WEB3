package com.ClinicSys.api.models.entity;

public record Paciente(
        Long id,
        String cpf,
        String nome,
        EnderecoPaciente id_endereco,
        String email,
        String numero,
        String plano_saude
) {
}
