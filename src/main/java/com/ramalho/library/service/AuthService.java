package com.ramalho.library.service;

import com.ramalho.library.database.model.EnumRoles;
import com.ramalho.library.database.model.UsersEntity;
import com.ramalho.library.database.repository.IUsersRepository;
import com.ramalho.library.dto.UsersRequestDTO;
import com.ramalho.library.exception.NotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
@Builder
@Slf4j
public class AuthService implements UserDetailsService {

    private final IUsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("The process of load username the user begins!");

        return usersRepository.findByEmail(username)
                .orElseThrow(() -> {
                   log.warn("User email/username not found.");
                   return new UsernameNotFoundException("User not found with this email address " + username);
                });
    }

    public void registerUsers(UsersRequestDTO requestDTO) {
        log.info("The process of register users begins!");

        if (usersRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            log.warn("Email of user currently in use.");
            throw new RuntimeException("Email currently in use!");
        }

        UsersEntity usersEntity = UsersEntity.builder()
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .roles(EnumRoles.READER)
                .build();

        usersRepository.save(usersEntity);
    }
}
