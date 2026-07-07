package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.Medico;
import com.ClinicSys.api.models.repository.interfaces.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MedicoRepository implements CrudRepository<Medico> {

    private final JdbcTemplate jdbcTemplate;

    public MedicoRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createEntity(Medico entidade) {
        String sql = """
                INSERT INTO medico
                (crm, nome, especialidade, email, numero)
                VALUES(?,?,?,?,?)
                """;

        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    entidade.crm(),
                    entidade.nome(),
                    entidade.especialidade(),
                    entidade.email(),
                    entidade.numero()
            );

            return linhas > 0;
        }catch (Exception e){
            log.error("Theme: Insert Medico\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    @Override
    public Medico readEntityId(Long id) {

        String sql = """
                SELECT * FROM medico WHERE id = ?
                """;

        try {
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) ->
                            new Medico(
                                    rs.getLong("id"),
                                    rs.getString("crm"),
                                    rs.getString("nome"),
                                    rs.getString("especialidade"),
                                    rs.getString("email"),
                                    rs.getString("numero")
                            ),
                    id
            ).get(0);
        } catch (Exception e) {
            log.error("Theme: Read Medico Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    @Override
    public boolean updateEntity(Medico entidadeModificada) {
        String sql = """
                UPDATE medico
                SET
                    crm = ?,
                    nome = ?,
                    especialidade = ?,
                    email = ?,
                    numero = ?
                WHERE
                    id = ?
                """;
        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    entidadeModificada.crm(),
                    entidadeModificada.nome(),
                    entidadeModificada.especialidade(),
                    entidadeModificada.email(),
                    entidadeModificada.numero(),
                    entidadeModificada.id()
            );

            return  linhas > 0;
        }catch (Exception e){
            log.error("Theme: Update Medico Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    @Override
    public boolean deleteEntity(Long id) {

        String sql = """
                DELETE FROM medico WHERE id = ?
                """;

        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    id
            );

            return linhas > 0;
        }catch (Exception e){
            log.error("Theme: Delete medico Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    public List<Medico> readALL(){
        String sql = "SELECT * FROM medico";

        try{
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) ->
                            new Medico(
                                    rs.getLong("id"),
                                    rs.getString("crm"),
                                    rs.getString("nome"),
                                    rs.getString("especialidade"),
                                    rs.getString("email"),
                                    rs.getString("numero")
                            )
            );
        }catch (Exception e) {
            log.error("Theme: ReadAll Paciente Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }
}
