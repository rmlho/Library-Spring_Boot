package com.ramalho.library.service;

import com.ramalho.library.database.model.EnumRoles;
import com.ramalho.library.database.model.UsersEntity;
import com.ramalho.library.database.repository.IUsersRepository;
import com.ramalho.library.dto.UsersRequestDTO;
import com.ramalho.library.exception.ConflictException;
import com.ramalho.library.exception.NotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
            throw new ConflictException("Email currently in use!");
        }

        UsersEntity usersEntity = UsersEntity.builder()
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .roles(EnumRoles.READER)
                .build();

        usersRepository.save(usersEntity);
    }

    @Transactional
    public void switchingRoleLibrary(Long id) throws NotFoundException {
        log.info("The method for switching the user's role for LIBRARY has begun.");

        UsersEntity user = usersRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User id not found in database.");
                    return new NotFoundException("User not found!");
                });

        if (user.getRoles() == EnumRoles.LIBRARY) {
            throw new ConflictException("The user's role is already a library " + user.getRoles().getAuthority());
        }

        user.setRoles(EnumRoles.LIBRARY);
    }

    @Transactional
    public void switchingRoleAdmin(Long id) throws NotFoundException {
        log.info("The method for switching the user's role for ADMIN has begun");

        UsersEntity user = usersRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User id not found in database.");
                    return new NotFoundException("User not found!");
                });

        if (user.getRoles() != EnumRoles.LIBRARY) {
            throw new ConflictException("The user must be a Library " + user.getRoles().getAuthority());
        }

        user.setRoles(EnumRoles.ADMIN);
    }
}
