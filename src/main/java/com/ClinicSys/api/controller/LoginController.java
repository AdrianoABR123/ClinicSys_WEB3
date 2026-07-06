package com.ClinicSys.api.controller;

import com.ClinicSys.api.models.entity.LoginMedico;
import com.ClinicSys.api.models.repository.LoginMedicoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/medico")
public class LoginController {

    private final LoginMedicoRepository loginMedicoRepository;

    public LoginController(LoginMedicoRepository loginMedicoRepository) {
        this.loginMedicoRepository = loginMedicoRepository;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "medico/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String login,
            @RequestParam String senha,
            HttpSession session,
            Model model
    ) {
        LoginMedico loginMedico = loginMedicoRepository.findByLogin(login);

        if (loginMedico != null && loginMedico.senha().equals(senha)) {
            session.setAttribute("medicoLogado", loginMedico.idMedico());
            return "redirect:/medico/dashboard";
        }

        model.addAttribute("erro", "Login ou senha inválidos.");
        return "medico/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
