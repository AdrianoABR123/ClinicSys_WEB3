package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.Paciente;
import com.ClinicSys.api.models.repository.interfaces.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class PacienteRepository implements CrudRepository<Paciente> {

    private final JdbcTemplate jdbcTemplate;

    public PacienteRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createEntity(Paciente entidade) {

        String sql = """
                INSERT INTO paciente
                (cpf, nome, id_endereco, email, numero, plano_saude)
                VALUES(?,?,?,?,?,?)
                """;
        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    entidade.cpf(),
                    entidade.nome(),
                    entidade.id_endereco().id(),
                    entidade.email(),
                    entidade.numero(),
                    entidade.plano_saude()
            );

            return linhas > 0;
        }catch (Exception e){
            log.error("Theme: Insert Paciente\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    @Override
    public Paciente readEntityId(Long id) {
        String sql = """
                SELECT * FROM paciente WHERE id = ?
                """;

        try {
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) ->
                       new Paciente(
                                rs.getLong("id"),
                                rs.getString("cpf"),
                                rs.getString("nome"),
                                null,
                                rs.getString("email"),
                                rs.getString("numero"),
                                rs.getString("plano_saude")
                        ),
                    id
            ).get(0);
        } catch (Exception e) {
            log.error("Theme: Read Paciente Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    @Override
    public boolean updateEntity(Paciente entidadeModificada) {
        String sql = """
                UPDATE paciente
                SET
                    cpf = ?,
                    nome = ?,
                    email = ?,
                    numero = ?,
                    plano_saude = ?
                WHERE
                    id = ?
                """;
        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    entidadeModificada.cpf(),
                    entidadeModificada.nome(),
                    entidadeModificada.email(),
                    entidadeModificada.numero(),
                    entidadeModificada.plano_saude(),
                    entidadeModificada.id()
                    );

            return  linhas > 0;
        }catch (Exception e){
            log.error("Theme: Update Paciente Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    @Override
    public boolean deleteEntity(Long id) {
        String sql = """
                DELETE FROM paciente WHERE id = ?
                """;

        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    id
            );

            return linhas > 0;
        }catch (Exception e){
            log.error("Theme: Delete Paciente Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    public List<Paciente> readAll(){
        String sql = "SELECT * FROM paciente";

        try{
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) ->
                       new Paciente(
                                rs.getLong("id"),
                                rs.getString("cpf"),
                                rs.getString("nome"),
                                null,
                                rs.getString("email"),
                                rs.getString("numero"),
                                rs.getString("plano_saude")
                        )
            );
        }catch (Exception e) {
            log.error("Theme: ReadAll Paciente Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

}
