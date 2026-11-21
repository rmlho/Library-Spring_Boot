package com.ramalho.library.service;

import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.database.model.EnumStatusRent;
import com.ramalho.library.database.model.RentEntity;
import com.ramalho.library.database.model.UsersEntity;
import com.ramalho.library.database.repository.IBooksRepository;
import com.ramalho.library.database.repository.IRentRepository;
import com.ramalho.library.database.repository.IUsersRepository;
import com.ramalho.library.dto.*;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.exception.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class RentService {

    private final IRentRepository rentRepository;
    private final IBooksRepository booksRepository;
    private final IUsersRepository usersRepository;

    @Transactional
    public RentEntity rentRequest(RentRequestEntityDTO rentRequest) throws NotFoundException{
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

        if (book.getQuantity() == null || book.getQuantity() <= 0) {
            throw new UnprocessableEntityException("The book is out of stock.");
        }

        if (rentRepository.existsByUsersAndBooksAndStatusRentIn(
                user,
                book,
                List.of(EnumStatusRent.ACTIVE, EnumStatusRent.OVERDUE))) {
            throw new UnprocessableEntityException("You already have this book rented.");
        }

        book.setQuantity(book.getQuantity() - 1);

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

    @Transactional(noRollbackFor = UnprocessableEntityException.class)
    public RentResponseDTO bookReturn(String nameUser, BookUserDTO book) throws NotFoundException {
        log.info("The process of returning the book begins.");

        BooksEntity bookSearch = booksRepository.findByName(book.getName())
                .orElseThrow(() -> {
                    log.warn("Book not found in database.");
                    return new NotFoundException("Book not found!");
                });

        UsersEntity user = usersRepository.findByNameUser(nameUser)
                .orElseThrow(() -> {
                    log.warn("User not found in database.");
                    return new NotFoundException("User not found!");
                });

        RentEntity rentEntity = rentRepository.findFirstByUsersAndBooksOrderByOrderDateDesc(user, bookSearch)
                .orElseThrow(() -> {
                    log.warn("No user checkouts found for this book.");
                    return new NotFoundException("User's rental not found");
                });

        if (rentEntity.getStatusRent() != EnumStatusRent.ACTIVE) {
            throw new UnprocessableEntityException("The book has already been returned or the rental is overdue.");
        }

        if (LocalDate.now().isAfter(rentEntity.getReturnedAt())) {
            rentEntity.setStatusRent(EnumStatusRent.OVERDUE);
            throw new UnprocessableEntityException("Book is overdue, pay the fine.");
        }

        rentEntity.setStatusRent(EnumStatusRent.RETURNED);
        rentEntity.getBooks().setQuantity(rentEntity.getBooks().getQuantity() + 1);
        log.info("Book successfully returned.");

        RentResponseDTO rent = RentResponseDTO.builder()
                .nameUser(nameUser)
                .nameBook(book.getName())
                .statusRent(rentEntity.getStatusRent().returnedStatus())
                .build();

        return rent;
    }

    public Page<RentEntity> listRentals(Pageable pageable) {
        log.info("The process of listing everything begins.");

        Page<RentEntity> rentEntityPage = rentRepository.findAll(pageable);
        log.info("The process of listing everything was completed successfully.");
        return rentEntityPage;
    }

    public void deleteRent(Long id) throws NotFoundException {
        log.info("Start the process of deleting a rental.");

        RentEntity rent = rentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rent not found in database");
                    return new NotFoundException("Rent not found!");
                });

        rentRepository.delete(rent);
    }

    public Page<RentEntity> listAllOverdue(Pageable pageable) {
        log.info("The process of listing all overdue rent payments begins.");

        Page<RentEntity> listOverdue = rentRepository.findAllByStatusRent(EnumStatusRent.OVERDUE, pageable);
        log.info("Process completed successfully.");
        return listOverdue;
    }

    @Transactional(noRollbackFor = UnprocessableEntityException.class)
    public RentEntity updateRent(Long id, RentRequestEntityDTO requestEntityDTO) throws NotFoundException {
        log.info("The process of updating a rent payment has begun.");

        RentEntity rent = rentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rent not found in database.");
                    return new NotFoundException("Rent not found!");
                });

        UsersEntity user = usersRepository.findByNameUser(requestEntityDTO.getNameUser())
                .orElseThrow(() -> {
                    log.warn("User not found in database.");
                    return new NotFoundException("User not found!");
                });

        UsersEntity userByEmail = usersRepository.findByEmail(requestEntityDTO.getEmailUser())
                .orElseThrow(() -> {
                    log.warn("Email user not found in database.");
                    return new NotFoundException("Email not found!");
                });

        if (!Objects.equals(user.getNameUser(), userByEmail.getNameUser())) {
            log.warn("The email and the user's name do not match.");
            throw new UnprocessableEntityException("The email address does not match the username!");
        }

        BooksEntity book = booksRepository.findByName(requestEntityDTO.getNameBook())
                .orElseThrow(() -> {
                    log.warn("Book not found in database.");
                    return new NotFoundException("Book not found!");
                });

        if (rent.getStatusRent() != EnumStatusRent.ACTIVE) {
            throw new UnprocessableEntityException("The book has already been returned or the rental is overdue.");
        }

        if (LocalDate.now().isAfter(rent.getReturnedAt())) {
            rent.setStatusRent(EnumStatusRent.OVERDUE);
            throw new UnprocessableEntityException("Book is overdue, pay the fine.");
        }

        BooksEntity oldBook = rent.getBooks();
        if (oldBook.getId() != book.getId()) {
            if (book.getQuantity() == null || book.getQuantity() <= 0) {
                throw new UnprocessableEntityException("The book is out of stock.");
            }

            oldBook.setQuantity(oldBook.getQuantity() + 1);
            book.setQuantity(book.getQuantity() - 1);
        }

        rent.setUsers(user);
        rent.setBooks(book);
        rent.setReturnedAt(LocalDate.now().plusDays(7));
        log.info("Rent successfully updated");

        return rent;
    }

    public Page<RentResponseDTO> findAllRentByUser(UsersRequestDTO requestDTO, Pageable pageable) throws NotFoundException {
        log.info("The process of listing the user's rentals has begun");

        UsersEntity user = usersRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> {
                    log.warn("User not found in database.");
                    return new NotFoundException("User not found!");
                });

        Page<RentEntity> rents = rentRepository.findAllByUsersOrderByOrderDateDesc(user, pageable);

        log.info("The user's rentals were listed successfully.");
        return rents.map(rent -> RentResponseDTO.builder()
                .nameUser(rent.getUsers().getNameUser())
                .nameBook(rent.getBooks().getName())
                .statusRent(rent.getStatusRent().returnedStatus())
                .build());
    }

}
