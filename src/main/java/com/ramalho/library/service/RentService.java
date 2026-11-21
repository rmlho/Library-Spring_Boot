package com.ramalho.library.service;

import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.database.model.EnumStatusRent;
import com.ramalho.library.database.model.RentEntity;
import com.ramalho.library.database.model.UsersEntity;
import com.ramalho.library.database.repository.IBooksRepository;
import com.ramalho.library.database.repository.IRentRepository;
import com.ramalho.library.database.repository.IUsersRepository;
import com.ramalho.library.dto.RentRequestEntityDTO;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.exception.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class RentService {

    private final IRentRepository rentRepository;
    private final IBooksRepository booksRepository;
    private final IUsersRepository usersRepository;

    public RentEntity rentRequest(RentRequestEntityDTO rentRequest) throws NotFoundException {
        log.info("Starting the rental application process.");

        UsersEntity nameUser = usersRepository.findByNameUser(rentRequest.getNameUser())
                .orElseThrow(() -> {
                    log.warn("Name user not found in database.");
                    return new NotFoundException("Name user not found!");
                });

        UsersEntity user = usersRepository.findByEmail(rentRequest.getEmailUser())
                .orElseThrow(() -> {
                    log.warn("Email user not found in database.");
                    return new NotFoundException("Email not found!");
                });

        if (!Objects.equals(nameUser.getNameUser(), user.getNameUser())) {
            log.warn("The email and the user's name are not the same object.");
            throw new UnprocessableEntityException("The email address does not match the username!");
        }

        BooksEntity book = booksRepository.findByName(rentRequest.getNameBook())
                .orElseThrow(() -> {
                    log.warn("Book not found in database.");
                    return new NotFoundException("Book not found!");
                });

        RentEntity savedRent = RentEntity.builder()
                .users(user)
                .books(book)
                .orderDate(LocalDateTime.now())
                .returnedAt(LocalDate.now().plusDays(7))
                .statusRent(EnumStatusRent.ACTIVE)
                .build();

        RentEntity rent = rentRepository.save(savedRent);
        log.info("Rental {} completed successfully.", rent.getUsers().getNameUser());
        return rent;
    }



}
