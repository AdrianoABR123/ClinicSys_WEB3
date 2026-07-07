package com.ClinicSys.api.models.entity;

import java.io.Serializable;

public record ItemReceituario(
        Long id,
        Integer intervaloDoses,
        String observacao,
        Medicamento idMedicamento,
        Receituario idReceituario
) implements Serializable {
}
