package com.ramalho.library.database.repository;

import com.ramalho.library.database.model.BooksEntity;
import com.ramalho.library.database.model.EnumStatusRent;
import com.ramalho.library.database.model.RentEntity;
import com.ramalho.library.database.model.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface IRentRepository extends JpaRepository<RentEntity, Long> {
    Optional<RentEntity> findByUsers(UsersEntity users);

    boolean existsByUsersAndBooksAndStatusRentIn(
            UsersEntity users,
            BooksEntity books,
            Collection<EnumStatusRent> statuses
    );

    Optional<RentEntity> findFirstByUsersAndBooksOrderByOrderDateDesc(
            UsersEntity users,
            BooksEntity books
    );

    Page<RentEntity> findAllByStatusRent(EnumStatusRent statusRent, Pageable pageable);

    Page<RentEntity> findAllByUsersOrderByOrderDateDesc(UsersEntity users, Pageable pageable);
}
