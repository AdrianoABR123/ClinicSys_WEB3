package com.ClinicSys.api.models.repository;

import com.ClinicSys.api.models.entity.ItemReceituario;
import com.ClinicSys.api.models.entity.Medicamento;
import com.ClinicSys.api.models.entity.Receituario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ItemReceituarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public ItemReceituarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean createEntity(Long idReceituario, Long idMedicamento, Integer intervaloDoses, String observacao) {
        String sql = """
                INSERT INTO item_receituario
                (intervalo_doses, observacao, id_medicamento, id_receituario)
                VALUES(?,?,?,?)
                """;
        try {
            int linhas = jdbcTemplate.update(sql, intervaloDoses, observacao, idMedicamento, idReceituario);
            return linhas > 0;
        } catch (Exception e) {
            log.error("Theme: Insert ItemReceituario\n Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    public List<ItemReceituario> findByReceituarioId(Long idReceituario) {
        String sql = """
                SELECT ir.id, ir.intervalo_doses, ir.observacao,
                       m.id as m_id, m.codigo as m_codigo, m.nome as m_nome,
                       m.dosagem as m_dosagem, m.tipo_dosagem as m_tipo_dosagem,
                       m.descricao as m_descricao, m.observacao as m_observacao
                FROM item_receituario ir
                JOIN medicamento m ON m.id = ir.id_medicamento
                WHERE ir.id_receituario = ?
                """;
        try {
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) -> new ItemReceituario(
                            rs.getLong("id"),
                            rs.getInt("intervalo_doses"),
                            rs.getString("observacao"),
                            new Medicamento(
                                    rs.getLong("m_id"),
                                    rs.getInt("m_codigo"),
                                    rs.getString("m_nome"),
                                    rs.getInt("m_dosagem"),
                                    rs.getString("m_tipo_dosagem"),
                                    rs.getString("m_descricao"),
                                    rs.getString("m_observacao")
                            ),
                            null
                    ),
                    idReceituario
            );
        } catch (Exception e) {
            log.error("Theme: FindByReceituarioId ItemReceituario Class: {}\nMessage: {}\n Traceback: {}", e.getClass(), e.getMessage(), e.getStackTrace());
            return null;
        }
    }
}
