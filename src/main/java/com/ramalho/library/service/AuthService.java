package com.ramalho.library.service;

import com.ramalho.library.database.model.EnumRoles;
import com.ramalho.library.database.model.UsersEntity;
import com.ramalho.library.database.repository.IUsersRepository;
import com.ramalho.library.dto.UsersRequestDTO;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
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
public class AuthService implements UserDetailsService {

    private final IUsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email address" + username));
    }

    public void registerUsers(UsersRequestDTO requestDTO) {
        if (usersRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
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
