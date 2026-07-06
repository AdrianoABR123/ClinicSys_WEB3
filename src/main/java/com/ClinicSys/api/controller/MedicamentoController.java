package com.ClinicSys.api.controller;

import com.ClinicSys.api.models.entity.Medicamento;
import com.ClinicSys.api.models.repository.MedicamentoRespository;
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
@RequestMapping("/atendimento/medicamentos")
public class MedicamentoController {

    private final MedicamentoRespository medicamentoRepository;

    public MedicamentoController(MedicamentoRespository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        List<Medicamento> medicamentos = medicamentoRepository.readAll();
        model.addAttribute("medicamentos", medicamentos);
        return "atendimento/medicamentos/lista";
    }

    @GetMapping("/novo")
    public String novoForm() {
        return "atendimento/medicamentos/formulario";
    }

    @PostMapping
    public String salvar(
            @RequestParam Integer codigo,
            @RequestParam String nome,
            @RequestParam Integer dosagem,
            @RequestParam String tipo_dosagem,
            @RequestParam String descricao,
            @RequestParam String observacao,
            RedirectAttributes redirectAttributes
    ) {
        Medicamento medicamento = new Medicamento(null, codigo, nome, dosagem, tipo_dosagem, descricao, observacao);
        boolean sucesso = medicamentoRepository.createEntity(medicamento);

        if (sucesso) {
            redirectAttributes.addFlashAttribute("mensagem", "Medicamento cadastrado com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao cadastrar medicamento.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/medicamentos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Medicamento medicamento = medicamentoRepository.readEntityId(id);
        if (medicamento == null) {
            return "redirect:/atendimento/medicamentos";
        }
        model.addAttribute("medicamento", medicamento);
        return "atendimento/medicamentos/formulario";
    }

    @PostMapping("/editar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @RequestParam Integer codigo,
            @RequestParam String nome,
            @RequestParam Integer dosagem,
            @RequestParam String tipo_dosagem,
            @RequestParam String descricao,
            @RequestParam String observacao,
            RedirectAttributes redirectAttributes
    ) {
        Medicamento medicamento = new Medicamento(id, codigo, nome, dosagem, tipo_dosagem, descricao, observacao);
        boolean sucesso = medicamentoRepository.updateEntity(medicamento);

        if (sucesso) {
            redirectAttributes.addFlashAttribute("mensagem", "Medicamento atualizado com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao atualizar medicamento.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/medicamentos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean sucesso = medicamentoRepository.deleteEntity(id);

        if (sucesso) {
            redirectAttributes.addFlashAttribute("mensagem", "Medicamento excluído com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir medicamento.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }

        return "redirect:/atendimento/medicamentos";
    }
}
