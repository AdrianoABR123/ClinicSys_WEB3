package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.Consulta;
import com.ClinicSys.api.models.entity.Paciente;
import com.ClinicSys.api.models.repository.interfaces.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ConsultaRepository implements CrudRepository<Consulta> {

    private final JdbcTemplate jdbcTemplate;
    private final PacienteRepository pacienteRepository;

    public ConsultaRepository(JdbcTemplate jdbcTemplate, PacienteRepository pacienteRepository){
        this.jdbcTemplate = jdbcTemplate;
        this.pacienteRepository = pacienteRepository;
    }


    @Override
    public boolean createEntity(Consulta entidade) {
        String sql = """
                INSERT INTO consulta
                (codigo, data_hora, data_hora_volta, observacao, id_paciente, id_medico)
                VALUES(?,?,?,?,?,?)
                """;

        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    entidade.codigo(),
                    entidade.data_hora(),
                    entidade.data_hora_volta(),
                    entidade.observacao(),
                    entidade.id_paciente().id(),
                    entidade.id_medico().id()
            );

            return linhas > 0;
        }catch (Exception e){
            log.error("Theme: Insert Consult\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    @Override
    public Consulta readEntityId(Long id) {
        return null;
    }

    @Override
    public boolean updateEntity(Consulta entidadeModificada) {
        return false;
    }

    @Override
    public boolean deleteEntity(Long id) {
        return false;
    }

    public List<Consulta> readAll(){
        String sql = "SELECT * FROM consulta";

        try{
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) ->
                            new Consulta(
                                    rs.getLong("id"),
                                    rs.getInt("codigo"),
                                    rs.getTimestamp("data_hora"),
                                    rs.getTimestamp("data_hora_volta"),
                                    rs.getString("observacao"),
                                    null,
                                    null
                            )
            );
        }catch (Exception e) {
            log.error("Theme: ReadAll Paciente Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }
}
