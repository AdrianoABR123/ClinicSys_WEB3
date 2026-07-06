package com.ClinicSys.api.controller;

import com.ClinicSys.api.models.entity.*;
import com.ClinicSys.api.models.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/medico")
public class MedicoAreaController {

    private final ConsultaRepository consultaRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final ReceituarioRepository receituarioRepository;
    private final ItemReceituarioRepository itemReceituarioRepository;
    private final MedicamentoRespository medicamentoRepository;

    public MedicoAreaController(
            ConsultaRepository consultaRepository,
            ProntuarioRepository prontuarioRepository,
            ReceituarioRepository receituarioRepository,
            ItemReceituarioRepository itemReceituarioRepository,
            MedicamentoRespository medicamentoRepository
    ) {
        this.consultaRepository = consultaRepository;
        this.prontuarioRepository = prontuarioRepository;
        this.receituarioRepository = receituarioRepository;
        this.itemReceituarioRepository = itemReceituarioRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    private Medico getMedicoLogado(HttpSession session) {
        return (Medico) session.getAttribute("medicoLogado");
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Medico medico = getMedicoLogado(session);
        if (medico == null) {
            return "redirect:/medico/login";
        }

        List<Consulta> consultas = consultaRepository.findPendentesByMedicoId(medico.id());
        model.addAttribute("medico", medico);
        model.addAttribute("consultas", consultas);
        return "medico/dashboard";
    }

    @GetMapping("/consulta/{id}")
    public String consultaDetalhe(@PathVariable Long id, HttpSession session, Model model) {
        Medico medico = getMedicoLogado(session);
        if (medico == null) {
            return "redirect:/medico/login";
        }

        Consulta consulta = consultaRepository.readEntityId(id);
        if (consulta == null) {
            return "redirect:/medico/dashboard";
        }

        List<Medicamento> medicamentos = medicamentoRepository.readAll();
        model.addAttribute("medico", medico);
        model.addAttribute("consulta", consulta);
        model.addAttribute("medicamentos", medicamentos);
        return "medico/consulta-detalhe";
    }

    @PostMapping("/consulta/{id}/prontuario")
    public String cadastrarProntuario(
            @PathVariable Long id,
            @RequestParam String descricao,
            @RequestParam(required = false) String observacao,
            @RequestParam(required = false) String incluirReceituario,
            @RequestParam(required = false) String obsReceituario,
            @RequestParam(required = false) List<Long> medicamentoIds,
            @RequestParam(required = false) List<Integer> intervaloDoses,
            @RequestParam(required = false) List<String> obsItem,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Medico medico = getMedicoLogado(session);
        if (medico == null) {
            return "redirect:/medico/login";
        }

        Long idReceituario = null;

        // Cria receituário se checkbox marcado
        if ("on".equals(incluirReceituario) && medicamentoIds != null && !medicamentoIds.isEmpty()) {
            Integer codigoReceituario = receituarioRepository.getNextCodigo();
            Receituario receituario = new Receituario(null, codigoReceituario, obsReceituario);
            idReceituario = receituarioRepository.createEntityReturningId(receituario);

            if (idReceituario != null) {
                for (int i = 0; i < medicamentoIds.size(); i++) {
                    Long medId = medicamentoIds.get(i);
                    Integer intervalo = (intervaloDoses != null && i < intervaloDoses.size()) ? intervaloDoses.get(i) : null;
                    String obs = (obsItem != null && i < obsItem.size()) ? obsItem.get(i) : null;
                    itemReceituarioRepository.createEntity(idReceituario, medId, intervalo, obs);
                }
            }
        }

        // Cria prontuário
        boolean sucesso = prontuarioRepository.createEntity(descricao, observacao, id, idReceituario);

        if (sucesso) {
            redirectAttributes.addFlashAttribute("mensagem", "Prontuário cadastrado com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao cadastrar prontuário.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/medico/dashboard";
    }

    @GetMapping("/realizadas")
    public String realizadas(HttpSession session, Model model) {
        Medico medico = getMedicoLogado(session);
        if (medico == null) {
            return "redirect:/medico/login";
        }

        List<Consulta> consultas = consultaRepository.findRealizadasByMedicoId(medico.id());
        model.addAttribute("medico", medico);
        model.addAttribute("consultas", consultas);
        return "medico/realizadas";
    }

    @GetMapping("/consulta/{id}/prontuario")
    public String verProntuario(@PathVariable Long id, HttpSession session, Model model) {
        Medico medico = getMedicoLogado(session);
        if (medico == null) {
            return "redirect:/medico/login";
        }

        Consulta consulta = consultaRepository.readEntityId(id);
        if (consulta == null) {
            return "redirect:/medico/realizadas";
        }

        Prontuario prontuario = prontuarioRepository.findByConsultaId(id);
        Receituario receituario = null;
        List<ItemReceituario> itensReceituario = null;

        if (prontuario != null) {
            Long idReceituario = prontuarioRepository.getReceituarioIdByConsultaId(id);
            if (idReceituario != null) {
                receituario = receituarioRepository.readEntityId(idReceituario);
                itensReceituario = itemReceituarioRepository.findByReceituarioId(idReceituario);
            }
        }

        model.addAttribute("medico", medico);
        model.addAttribute("consulta", consulta);
        model.addAttribute("prontuario", prontuario);
        model.addAttribute("receituario", receituario);
        model.addAttribute("itensReceituario", itensReceituario);
        return "medico/prontuario-detalhe";
    }
}
