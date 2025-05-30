package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.ChangePasswordRequestDTO;
import br.edu.fatecsjc.lgnspringapi.entity.User;
import br.edu.fatecsjc.lgnspringapi.exception.InvalidCurrentPasswordException;
import br.edu.fatecsjc.lgnspringapi.exception.PasswordsDoNotMatchException;
import br.edu.fatecsjc.lgnspringapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public void changePassword(ChangePasswordRequestDTO request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Verifica senha atual
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }

        // Verifica se nova senha e confirmação são iguais
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        // Atualiza a senha
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Salva
        repository.save(user);
    }
}
