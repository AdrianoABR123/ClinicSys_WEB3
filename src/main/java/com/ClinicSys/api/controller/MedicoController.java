package com.ClinicSys.api.controller;

import com.ClinicSys.api.models.entity.Medico;
import com.ClinicSys.api.models.repository.LoginMedicoRepository;
import com.ClinicSys.api.models.repository.MedicoRepository;
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
@RequestMapping("/atendimento/medicos")
public class MedicoController {

    private final MedicoRepository medicoRepository;
    private final LoginMedicoRepository loginMedicoRepository;

    public MedicoController(MedicoRepository medicoRepository, LoginMedicoRepository loginMedicoRepository) {
        this.medicoRepository = medicoRepository;
        this.loginMedicoRepository = loginMedicoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        List<Medico> medicos = medicoRepository.readALL();
        model.addAttribute("medicos", medicos);
        return "atendimento/medicos/lista";
    }

    @GetMapping("/novo")
    public String novoForm() {
        return "atendimento/medicos/formulario";
    }

    @PostMapping
    public String salvar(
            @RequestParam String crm,
            @RequestParam String nome,
            @RequestParam String especialidade,
            @RequestParam String email,
            @RequestParam String numero,
            @RequestParam String login,
            @RequestParam String senha,
            RedirectAttributes redirectAttributes
    ) {
        Medico medico = new Medico(null, crm, nome, especialidade, email, numero);
        boolean sucesso = medicoRepository.createEntity(medico);

        if (sucesso) {
            // Buscar o médico recém-criado pelo CRM para pegar o ID
            List<Medico> medicos = medicoRepository.readALL();
            Medico medicoCriado = medicos.stream()
                    .filter(m -> m.crm().equals(crm))
                    .findFirst()
                    .orElse(null);

            if (medicoCriado != null) {
                loginMedicoRepository.createEntity(medicoCriado.id(), login, senha);
            }

            redirectAttributes.addFlashAttribute("mensagem", "Médico cadastrado com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao cadastrar médico.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/medicos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Medico medico = medicoRepository.readEntityId(id);
        if (medico == null) {
            return "redirect:/atendimento/medicos";
        }

        var loginMedico = loginMedicoRepository.findByMedicoId(id);
        model.addAttribute("medico", medico);
        model.addAttribute("loginMedico", loginMedico);
        return "atendimento/medicos/formulario";
    }

    @PostMapping("/editar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @RequestParam String crm,
            @RequestParam String nome,
            @RequestParam String especialidade,
            @RequestParam String email,
            @RequestParam String numero,
            @RequestParam String login,
            @RequestParam String senha,
            RedirectAttributes redirectAttributes
    ) {
        Medico medico = new Medico(id, crm, nome, especialidade, email, numero);
        boolean sucesso = medicoRepository.updateEntity(medico);
        loginMedicoRepository.updateByMedicoId(id, login, senha);

        if (sucesso) {
            redirectAttributes.addFlashAttribute("mensagem", "Médico atualizado com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao atualizar médico.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/medicos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        loginMedicoRepository.deleteByMedicoId(id);
        boolean sucesso = medicoRepository.deleteEntity(id);

        if (sucesso) {
            redirectAttributes.addFlashAttribute("mensagem", "Médico excluído com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir médico.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/medicos";
    }
}
