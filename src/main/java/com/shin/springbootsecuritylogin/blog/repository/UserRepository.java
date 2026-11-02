package com.shin.springbootsecuritylogin.blog.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRepository {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{4,20}$",
            message = "아이디는 영문과 숫자 4~20자로 입력해주세요."
    )
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max = 72, message = "비밀번호가 너무 깁니다.")
    private String password;
}