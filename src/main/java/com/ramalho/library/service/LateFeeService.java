package com.ramalho.library.service;

import com.ramalho.library.database.model.EnumFineStatus;
import com.ramalho.library.database.model.LateFeeEntity;
import com.ramalho.library.database.model.RentEntity;
import com.ramalho.library.database.model.UsersEntity;
import com.ramalho.library.database.repository.ILateFeeRepository;
import com.ramalho.library.database.repository.IRentRepository;
import com.ramalho.library.database.repository.IUsersRepository;
import com.ramalho.library.dto.LateFeeResponseDTO;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.exception.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class LateFeeService {

    private final ILateFeeRepository lateFeeRepository;
    private final IRentRepository rentRepository;
    private final IUsersRepository usersRepository;

    public void issueFine(RentEntity rent) {
        log.info("The process of issuing a fine begins.");

        LocalDate now = LocalDate.now();

        if (!now.isAfter(rent.getDueDate())) {
            return;
        }

        long diasAtraso = ChronoUnit.DAYS.between(rent.getDueDate(), now);
        BigDecimal valor = BigDecimal.valueOf(1.5)
                .multiply(BigDecimal.valueOf(diasAtraso))
                .setScale(2, RoundingMode.HALF_UP);

        LateFeeEntity lateFee = LateFeeEntity.builder()
                .rent(rent)
                .fine(valor)
                .daysOverdue(Math.toIntExact(diasAtraso))
                .dateGenerated(now)
                .paymentDay(null)
                .status(EnumFineStatus.LATE)
                .build();

        lateFeeRepository.save(lateFee);
        log.info("Fine successfully issued.");
    }

    @Transactional
    public LateFeeEntity payFine(Long id) throws NotFoundException {
        log.info("The process of paying a fine begins.");

        LateFeeEntity lateFee = lateFeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Late fee id not found in database.");
                    return new NotFoundException("Late fee not found!");
                });

        if (lateFee.getStatus() == EnumFineStatus.PAID) {
            log.warn("The fine {} paid.", id);
            throw new UnprocessableEntityException("The fine has already been paid " + lateFee.getStatus().returnedStatus());
        }

        lateFee.setPaymentDay(LocalDate.now());
        lateFee.setStatus(EnumFineStatus.PAID);
        log.info("Fine successfully paid.");

        return lateFee;
    }

    @Transactional(readOnly = true)
    public LateFeeResponseDTO viewFine(Long rentId, String email) throws NotFoundException {
        log.info("Checking the fine for rent {}.", rentId);

        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Authenticated user not found in database.");
                    return new NotFoundException("User not found!");
                });

        rentRepository.findByIdAndUsers(rentId, user)
                .orElseThrow(() -> {
                    log.warn("Rent {} not found for authenticated user.", rentId);
                    return new NotFoundException("Rent not found!");
                });

        LateFeeEntity lateFee = lateFeeRepository.findByRentId(rentId)
                .orElseThrow(() -> {
                    log.warn("Fine not found for rent {}.", rentId);
                    return new NotFoundException("Fine not found!");
                });

        return LateFeeResponseDTO.builder()
                .id(lateFee.getId())
                .nameBook(lateFee.getRent().getBooks().getName())
                .fine(lateFee.getFine())
                .daysOverdue(lateFee.getDaysOverdue())
                .status(lateFee.getStatus().returnedStatus())
                .build();
    }

    public List<LateFeeEntity> getAll() {
        return lateFeeRepository.findAll();
    }
}
