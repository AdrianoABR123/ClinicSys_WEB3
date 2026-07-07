package com.ClinicSys.api.models.entity;

import java.io.Serializable;

public record Receituario(
        Long id,
        Integer codigo,
        String observacao
) implements Serializable {
}
