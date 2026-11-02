package com.shin.springbootsecuritylogin.blog.service;

import com.shin.springbootsecuritylogin.blog.dto.SignupRequest;
import com.shin.springbootsecuritylogin.blog.entity.User;
import com.shin.springbootsecuritylogin.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequest request) {

        // 비밀번호와 비밀번호 확인 검사
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException(
                    "비밀번호가 일치하지 않습니다."
            );
        }

        // 아이디 중복 검사
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "이미 사용 중인 아이디입니다."
            );
        }

        // 비밀번호 BCrypt 암호화
        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        // 사용자 정보 생성
        User user = new User(
                request.getUsername(),
                encodedPassword
        );

        try {
            // DB 저장
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            // 동시에 같은 아이디로 가입하는 경우 DB 제약조건으로 방어
            throw new IllegalArgumentException(
                    "이미 사용 중인 아이디입니다."
            );
        }
    }
}