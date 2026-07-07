package com.ClinicSys.api.models.entity;

import java.io.Serializable;

public record Medicamento(
        Long id,
        Integer codigo,
        String nome,
        Integer dosagem,
        String tipo_dosagem,
        String descricao,
        String observacao
) implements Serializable {
}
