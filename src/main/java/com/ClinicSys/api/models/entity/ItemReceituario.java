package com.ClinicSys.api.models.entity;

public record ItemReceituario(
        Long id,
        Integer intervaloDoses,
        String observacao,
        Medicamento idMedicamento,
        Receituario idReceituario
) {
}
