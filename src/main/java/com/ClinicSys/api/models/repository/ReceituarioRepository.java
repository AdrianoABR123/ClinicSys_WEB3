package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.Receituario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Slf4j
@Repository
public class ReceituarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReceituarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createEntityReturningId(Receituario entidade) {
        String sql = """
                INSERT INTO receituario
                (codigo, observacao)
                VALUES(?,?)
                """;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, entidade.codigo());
                ps.setString(2, entidade.observacao());
                return ps;
            }, keyHolder);
            return keyHolder.getKey().longValue();
        } catch (Exception e) {
            log.error("Theme: Insert Receituario\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public Receituario readEntityId(Long id) {
        String sql = "SELECT * FROM receituario WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new Receituario(
                            rs.getLong("id"),
                            rs.getInt("codigo"),
                            rs.getString("observacao")
                    ),
                    id
            );
        } catch (Exception e) {
            log.error("Theme: Read Receituario Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public Integer getNextCodigo() {
        String sql = "SELECT COALESCE(MAX(codigo), 0) + 1 FROM receituario";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            log.error("Theme: GetNextCodigo Receituario Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return 1;
        }
    }
}
