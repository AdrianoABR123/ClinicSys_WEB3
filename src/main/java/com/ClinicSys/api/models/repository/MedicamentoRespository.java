package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.Medicamento;
import com.ClinicSys.api.models.repository.interfaces.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MedicamentoRespository implements CrudRepository<Medicamento> {

    private final JdbcTemplate jdbcTemplate;

    public MedicamentoRespository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createEntity(Medicamento entidade) {
        String sql = """
                INSERT INTO medicamento
                (codigo, nome, dosagem, tipo_dosagem, descricao, observacao)
                VALUES(?,?,?,?,?,?)
                """;

        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    entidade.codigo(),
                    entidade.nome(),
                    entidade.dosagem(),
                    entidade.tipo_dosagem(),
                    entidade.descricao(),
                    entidade.observacao()
            );

            return linhas > 0;
        }catch (Exception e){
            log.error("Theme: Insert Medicamento\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    @Override
    public Medicamento readEntityId(Long id) {
        String sql = """
                SELECT * FROM medicamento WHERE id = ?
                """;
        try {
            Medicamento medicamento = jdbcTemplate.query(
                    sql,
                    (rs, rowNum) ->
                            new Medicamento(
                                    rs.getLong("id"),
                                    rs.getInt("codigo"),
                                    rs.getString("nome"),
                                    rs.getInt("dosagem"),
                                    rs.getString("tipo_dosagem"),
                                    rs.getString("descricao"),
                                    rs.getString("observacao")
                            ),
                    id
            ).get(0);
            return medicamento;
        } catch (Exception e) {
            log.error("Theme: Read Medicamento Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    @Override
    public boolean updateEntity(Medicamento entidadeModificada) {
        String sql = """
                UPDATE medicamento
                SET
                    codigo = ?,
                    nome = ?,
                    dosagem = ?,
                    tipo_dosagem = ?,
                    descricao = ?,
                    observacao = ?
                WHERE
                    id = ?
                """;
        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    entidadeModificada.codigo(),
                    entidadeModificada.nome(),
                    entidadeModificada.dosagem(),
                    entidadeModificada.tipo_dosagem(),
                    entidadeModificada.descricao(),
                    entidadeModificada.observacao(),
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
                DELETE FROM medicamento WHERE id = ?
                """;
        try {
            int linhas = jdbcTemplate.update(
                    sql,
                    id
            );

            return linhas > 0;
        }catch (Exception e){
            log.error("Theme: Delete Medicamento Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    public List<Medicamento> readAll(){
        String sql = "SELECT * FROM medicamento";

        try{
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) ->
                            new Medicamento(
                                    rs.getLong("id"),
                                    rs.getInt("codigo"),
                                    rs.getString("nome"),
                                    rs.getInt("dosagem"),
                                    rs.getString("tipo_dosagem"),
                                    rs.getString("descricao"),
                                    rs.getString("observacao")
                            )
            );
        }catch (Exception e) {
            log.error("Theme: ReadAll Medicamento Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }
}
