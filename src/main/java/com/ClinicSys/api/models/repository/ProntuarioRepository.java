package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.Prontuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ProntuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProntuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean createEntity(String descricao, String observacao, Long idConsulta, Long idReceituario) {
        String sql = """
                INSERT INTO prontuario
                (descricao, observacao, id_consulta, id_receituario)
                VALUES(?,?,?,?)
                """;
        try {
            int linhas = jdbcTemplate.update(sql, descricao, observacao, idConsulta, idReceituario);
            return linhas > 0;
        } catch (Exception e) {
            log.error("Theme: Insert Prontuario\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    public Prontuario findByConsultaId(Long idConsulta) {
        String sql = "SELECT * FROM prontuario WHERE id_consulta = ?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new Prontuario(
                            rs.getLong("id"),
                            rs.getString("descricao"),
                            rs.getString("observacao"),
                            null,
                            null
                    ),
                    idConsulta
            );
        } catch (Exception e) {
            log.error("Theme: FindByConsultaId Prontuario Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public Prontuario readEntityId(Long id) {
        String sql = "SELECT * FROM prontuario WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new Prontuario(
                            rs.getLong("id"),
                            rs.getString("descricao"),
                            rs.getString("observacao"),
                            null,
                            null
                    ),
                    id
            );
        } catch (Exception e) {
            log.error("Theme: Read Prontuario Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public Long getReceituarioIdByConsultaId(Long idConsulta) {
        String sql = "SELECT id_receituario FROM prontuario WHERE id_consulta = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, idConsulta);
        } catch (Exception e) {
            return null;
        }
    }
}
