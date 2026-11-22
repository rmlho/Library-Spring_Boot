package com.ramalho.library.controller;

import com.ramalho.library.database.model.RentEntity;
import com.ramalho.library.dto.RentRequestEntityDTO;
import com.ramalho.library.dto.RentResponseDTO;
import com.ramalho.library.exception.NotFoundException;
import com.ramalho.library.service.RentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/rent")
@Validated
@RequiredArgsConstructor
@Slf4j
public class RentController {

    private final RentService rentService;

    public static RentResponseDTO from(RentEntity rent) {

        return RentResponseDTO.builder()
                .nameUser(rent.getUsers().getNameUser())
                .nameBook(rent.getBooks().getName())
                .statusRent(rent.getStatusRent().returnedStatus())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentResponseDTO createRent(@AuthenticationPrincipal UserDetails user,
                                      @RequestBody @Valid RentRequestEntityDTO requestEntityDTO) throws NotFoundException {
        log.info("Rent request POST begun.");
        RentEntity rent = rentService.rentRequest(user.getUsername(), requestEntityDTO);

        return from(rent);
    }

    @PostMapping("/{id}/return")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RentResponseDTO rentReturn(@PathVariable Long id,
                                      @AuthenticationPrincipal UserDetails user) throws NotFoundException {
        log.info("Request to return the book.");

        return rentService.bookReturn(id, user.getUsername());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<RentResponseDTO> getAllRents(@RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size) {

        log.info("Request to search for all rentals.");

        return rentService.listRentals(PageRequest.of(page, size))
                .map(RentController::from);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRent(@PathVariable Long id) throws NotFoundException {
        log.info("Request to delete a rent.");

        rentService.deleteRent(id);
    }

    @GetMapping("/status/overdue")
    @ResponseStatus(HttpStatus.OK)
    public Page<RentResponseDTO> getAllOverdue(@RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size) {

        log.info("Request to list all overdue rent payments.");

        return rentService.listAllOverdue(PageRequest.of(page, size))
                .map(RentController::from);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RentResponseDTO updateRentBook(@PathVariable Long id,
                                          @AuthenticationPrincipal UserDetails user,
                                          @RequestBody @Valid RentRequestEntityDTO requestEntityDTO) throws NotFoundException {
        log.info("Request to Update Rent.");

        RentEntity rent = rentService.updateRent(id, user.getUsername(), requestEntityDTO);
        return from(rent);
    }

    @GetMapping("/rentsUser")
    @ResponseStatus(HttpStatus.OK)
    public Page<RentResponseDTO> getAllRentsUser(@RequestParam(defaultValue = "0") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 @AuthenticationPrincipal UserDetails user) throws NotFoundException {

        log.info("Request to list all of the user's rentals.");

        return rentService.findAllRentByUser(user.getUsername(), PageRequest.of(page, size));
    }

}
