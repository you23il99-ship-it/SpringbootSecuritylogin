package com.shin.springbootsecuritylogin.blog.controller;

import com.shin.springbootsecuritylogin.blog.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginRequest request,
            BindingResult bindingResult,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse,
            Model model) {

        // 1. 입력값 검증
        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    "아이디 또는 비밀번호 형식을 확인해주세요.");
            return "login";
        }

        try {
            // 2. 사용자 인증
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );

            // 3. 인증 정보 저장
            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(
                    context, httpRequest, httpResponse
            );

            return "redirect:/";

        } catch (AuthenticationException e) {

            model.addAttribute("error",
                    "아이디 또는 비밀번호가 올바르지 않습니다.");

            return "login";
        }
    }
}