package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.EnderecoPaciente;
import com.ClinicSys.api.models.repository.interfaces.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Repository
public class EnderecoRepository implements CrudRepository<EnderecoPaciente> {

    private final JdbcTemplate jdbcTemplate;

    public EnderecoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createEntity(EnderecoPaciente entidade) {
        String sql = """
                INSERT INTO endereco_paciente
                (cep, rua, bairro, municipio, uf)
                VALUES(?,?,?,?,?)
                """;
        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    entidade.cep(),
                    entidade.rua(),
                    entidade.bairro(),
                    entidade.municipio(),
                    entidade.uf()
            );
            return linhas > 0;
        } catch (Exception e) {
            log.error("Theme: Insert Endereco\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    public Long createEntityReturningId(EnderecoPaciente entidade) {
        String sql = """
                INSERT INTO endereco_paciente
                (cep, rua, bairro, municipio, uf)
                VALUES(?,?,?,?,?)
                """;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entidade.cep());
                ps.setString(2, entidade.rua());
                ps.setString(3, entidade.bairro());
                ps.setString(4, entidade.municipio());
                ps.setString(5, entidade.uf());
                return ps;
            }, keyHolder);
            return keyHolder.getKey().longValue();
        } catch (Exception e) {
            log.error("Theme: Insert Endereco Returning Id\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    @Override
    public EnderecoPaciente readEntityId(Long id) {
        String sql = "SELECT * FROM endereco_paciente WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new EnderecoPaciente(
                            rs.getLong("id"),
                            rs.getString("cep"),
                            rs.getString("rua"),
                            rs.getString("bairro"),
                            rs.getString("municipio"),
                            rs.getString("uf")
                    ),
                    id
            );
        } catch (Exception e) {
            log.error("Theme: Read Endereco Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    @Override
    public boolean updateEntity(EnderecoPaciente entidadeModificada) {
        String sql = """
                UPDATE endereco_paciente
                SET
                    cep = ?,
                    rua = ?,
                    bairro = ?,
                    municipio = ?,
                    uf = ?
                WHERE
                    id = ?
                """;
        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    entidadeModificada.cep(),
                    entidadeModificada.rua(),
                    entidadeModificada.bairro(),
                    entidadeModificada.municipio(),
                    entidadeModificada.uf(),
                    entidadeModificada.id()
            );
            return linhas > 0;
        } catch (Exception e) {
            log.error("Theme: Update Endereco Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    @Override
    public boolean deleteEntity(Long id) {
        String sql = "DELETE FROM endereco_paciente WHERE id = ?";
        try {
            int linhas = jdbcTemplate.update(sql, id);
            return linhas > 0;
        } catch (Exception e) {
            log.error("Theme: Delete Endereco Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }
}
