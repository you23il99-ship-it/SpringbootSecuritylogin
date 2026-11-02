package com.shin.springbootsecuritylogin.blog.controller;

import com.shin.springbootsecuritylogin.blog.dto.SignupRequest;
import com.shin.springbootsecuritylogin.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    // 회원가입 화면
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(
            @Valid @ModelAttribute SignupRequest request,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "error",
                    "입력 정보를 확인해주세요."
            );
            return "signup";
        }

        try {
            userService.signup(request);

            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }
}