package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.Consulta;
import com.ClinicSys.api.models.entity.Medico;
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

    public ConsultaRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
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
        String sql = """
                SELECT c.id, c.codigo, c.data_hora, c.data_hora_volta, c.observacao,
                       p.id as p_id, p.cpf, p.nome as p_nome, p.email as p_email, p.numero as p_numero, p.plano_saude,
                       m.id as m_id, m.crm, m.nome as m_nome, m.especialidade, m.email as m_email, m.numero as m_numero
                FROM consulta c
                JOIN paciente p ON p.id = c.id_paciente
                JOIN medico m ON m.id = c.id_medico
                WHERE c.id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new Consulta(
                            rs.getLong("id"),
                            rs.getInt("codigo"),
                            rs.getTimestamp("data_hora"),
                            rs.getTimestamp("data_hora_volta"),
                            rs.getString("observacao"),
                            new Paciente(
                                    rs.getLong("p_id"),
                                    rs.getString("cpf"),
                                    rs.getString("p_nome"),
                                    null,
                                    rs.getString("p_email"),
                                    rs.getString("p_numero"),
                                    rs.getString("plano_saude")
                            ),
                            new Medico(
                                    rs.getLong("m_id"),
                                    rs.getString("crm"),
                                    rs.getString("m_nome"),
                                    rs.getString("especialidade"),
                                    rs.getString("m_email"),
                                    rs.getString("m_numero")
                            )
                    ),
                    id
            );
        } catch (Exception e) {
            log.error("Theme: Read Consulta Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
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
        String sql = """
                SELECT c.id, c.codigo, c.data_hora, c.data_hora_volta, c.observacao,
                       p.id as p_id, p.cpf, p.nome as p_nome, p.email as p_email, p.numero as p_numero, p.plano_saude,
                       m.id as m_id, m.crm, m.nome as m_nome, m.especialidade, m.email as m_email, m.numero as m_numero
                FROM consulta c
                JOIN paciente p ON p.id = c.id_paciente
                JOIN medico m ON m.id = c.id_medico
                ORDER BY c.data_hora DESC
                """;

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
                                    new Paciente(
                                            rs.getLong("p_id"),
                                            rs.getString("cpf"),
                                            rs.getString("p_nome"),
                                            null,
                                            rs.getString("p_email"),
                                            rs.getString("p_numero"),
                                            rs.getString("plano_saude")
                                    ),
                                    new Medico(
                                            rs.getLong("m_id"),
                                            rs.getString("crm"),
                                            rs.getString("m_nome"),
                                            rs.getString("especialidade"),
                                            rs.getString("m_email"),
                                            rs.getString("m_numero")
                                    )
                            )
            );
        }catch (Exception e) {
            log.error("Theme: ReadAll Consulta Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public List<Consulta> findPendentesByMedicoId(Long idMedico) {
        String sql = """
                SELECT c.id, c.codigo, c.data_hora, c.data_hora_volta, c.observacao,
                       p.id as p_id, p.cpf, p.nome as p_nome, p.email as p_email, p.numero as p_numero, p.plano_saude,
                       m.id as m_id, m.crm, m.nome as m_nome, m.especialidade, m.email as m_email, m.numero as m_numero
                FROM consulta c
                JOIN paciente p ON p.id = c.id_paciente
                JOIN medico m ON m.id = c.id_medico
                WHERE c.id_medico = ?
                AND c.id NOT IN (SELECT pr.id_consulta FROM prontuario pr)
                ORDER BY c.data_hora ASC
                """;
        try {
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) -> new Consulta(
                            rs.getLong("id"),
                            rs.getInt("codigo"),
                            rs.getTimestamp("data_hora"),
                            rs.getTimestamp("data_hora_volta"),
                            rs.getString("observacao"),
                            new Paciente(
                                    rs.getLong("p_id"),
                                    rs.getString("cpf"),
                                    rs.getString("p_nome"),
                                    null,
                                    rs.getString("p_email"),
                                    rs.getString("p_numero"),
                                    rs.getString("plano_saude")
                            ),
                            new Medico(
                                    rs.getLong("m_id"),
                                    rs.getString("crm"),
                                    rs.getString("m_nome"),
                                    rs.getString("especialidade"),
                                    rs.getString("m_email"),
                                    rs.getString("m_numero")
                            )
                    ),
                    idMedico
            );
        } catch (Exception e) {
            log.error("Theme: FindPendentes Consulta Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public List<Consulta> findRealizadasByMedicoId(Long idMedico) {
        String sql = """
                SELECT c.id, c.codigo, c.data_hora, c.data_hora_volta, c.observacao,
                       p.id as p_id, p.cpf, p.nome as p_nome, p.email as p_email, p.numero as p_numero, p.plano_saude,
                       m.id as m_id, m.crm, m.nome as m_nome, m.especialidade, m.email as m_email, m.numero as m_numero
                FROM consulta c
                JOIN paciente p ON p.id = c.id_paciente
                JOIN medico m ON m.id = c.id_medico
                WHERE c.id_medico = ?
                AND c.id IN (SELECT pr.id_consulta FROM prontuario pr)
                ORDER BY c.data_hora DESC
                """;
        try {
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) -> new Consulta(
                            rs.getLong("id"),
                            rs.getInt("codigo"),
                            rs.getTimestamp("data_hora"),
                            rs.getTimestamp("data_hora_volta"),
                            rs.getString("observacao"),
                            new Paciente(
                                    rs.getLong("p_id"),
                                    rs.getString("cpf"),
                                    rs.getString("p_nome"),
                                    null,
                                    rs.getString("p_email"),
                                    rs.getString("p_numero"),
                                    rs.getString("plano_saude")
                            ),
                            new Medico(
                                    rs.getLong("m_id"),
                                    rs.getString("crm"),
                                    rs.getString("m_nome"),
                                    rs.getString("especialidade"),
                                    rs.getString("m_email"),
                                    rs.getString("m_numero")
                            )
                    ),
                    idMedico
            );
        } catch (Exception e) {
            log.error("Theme: FindRealizadas Consulta Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public Integer getNextCodigo() {
        String sql = "SELECT COALESCE(MAX(codigo), 0) + 1 FROM consulta";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            log.error("Theme: GetNextCodigo Consulta Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return 1;
        }
    }
}
