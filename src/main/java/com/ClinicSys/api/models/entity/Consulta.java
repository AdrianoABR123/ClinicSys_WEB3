package com.ClinicSys.api.models.entity;

import java.sql.Timestamp;

public record Consulta(
        Long id,
        Integer codigo,
        Timestamp data_hora,
        Timestamp data_hora_volta,
        String observacao,
        Paciente id_paciente,
        Medico id_medico
) {
}
