package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.LoginMedico;
import com.ClinicSys.api.models.entity.Medico;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class LoginMedicoRepository {

    private final JdbcTemplate jdbcTemplate;

    public LoginMedicoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean createEntity(Long idMedico, String login, String senha) {
        String sql = """
                INSERT INTO login_medico
                (id_medico, login, senha)
                VALUES(?,?,?)
                """;
        try {
            int linhas = jdbcTemplate.update(sql, idMedico, login, senha);
            return linhas > 0;
        } catch (Exception e) {
            log.error("Theme: Insert LoginMedico\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    public LoginMedico findByLogin(String login) {
        String sql = """
                SELECT lm.id, lm.id_medico, lm.login, lm.senha,
                       m.id as m_id, m.crm, m.nome, m.especialidade, m.email, m.numero
                FROM login_medico lm
                JOIN medico m ON m.id = lm.id_medico
                WHERE lm.login = ?
                """;
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new LoginMedico(
                            rs.getLong("id"),
                            new Medico(
                                    rs.getLong("m_id"),
                                    rs.getString("crm"),
                                    rs.getString("nome"),
                                    rs.getString("especialidade"),
                                    rs.getString("email"),
                                    rs.getString("numero")
                            ),
                            rs.getString("login"),
                            rs.getString("senha")
                    ),
                    login
            );
        } catch (Exception e) {
            log.error("Theme: FindByLogin LoginMedico Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public LoginMedico findByMedicoId(Long idMedico) {
        String sql = "SELECT * FROM login_medico WHERE id_medico = ?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new LoginMedico(
                            rs.getLong("id"),
                            null,
                            rs.getString("login"),
                            rs.getString("senha")
                    ),
                    idMedico
            );
        } catch (Exception e) {
            log.error("Theme: FindByMedicoId LoginMedico Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public boolean updateByMedicoId(Long idMedico, String login, String senha) {
        String sql = """
                UPDATE login_medico
                SET login = ?, senha = ?
                WHERE id_medico = ?
                """;
        try {
            int linhas = jdbcTemplate.update(sql, login, senha, idMedico);
            return linhas > 0;
        } catch (Exception e) {
            log.error("Theme: Update LoginMedico Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    public boolean deleteByMedicoId(Long idMedico) {
        String sql = "DELETE FROM login_medico WHERE id_medico = ?";
        try {
            int linhas = jdbcTemplate.update(sql, idMedico);
            return linhas > 0;
        } catch (Exception e) {
            log.error("Theme: Delete LoginMedico Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }
}
