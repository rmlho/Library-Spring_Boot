package com.ramalho.library.controller;

import com.ramalho.library.database.model.LateFeeEntity;
import com.ramalho.library.dto.LateFeeResponseDTO;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.service.LateFeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fine")
@Validated
@RequiredArgsConstructor
@Slf4j
public class LateFeeController {

    private final LateFeeService lateFeeService;

    public static LateFeeResponseDTO from(LateFeeEntity fine) {
        return LateFeeResponseDTO.builder()
                .id(fine.getId())
                .nameBook(fine.getRent().getBooks().getName())
                .fine(fine.getFine())
                .daysOverdue(fine.getDaysOverdue())
                .status(fine.getStatus().returnedStatus())
                .build();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LateFeeResponseDTO payFine(@PathVariable Long id) throws NotFoundException {
        log.info("The process for paying the fine has begun.");

        LateFeeEntity lateFee = lateFeeService.payFine(id);

        return from(lateFee);
    }

    @GetMapping("/{rentId}")
    @ResponseStatus(HttpStatus.OK)
    public LateFeeResponseDTO getFine(@PathVariable Long rentId, @AuthenticationPrincipal UserDetails user) throws NotFoundException {
        return lateFeeService.viewFine(rentId, user.getUsername());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LateFeeEntity> getAll() {
        return lateFeeService.getAll();
    }
}
