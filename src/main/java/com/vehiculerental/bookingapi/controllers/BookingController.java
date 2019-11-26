package com.vehiculerental.bookingapi.controllers;

import com.google.common.collect.Iterables;
import com.vehiculerental.bookingapi.dao.BookingDao;
import com.vehiculerental.bookingapi.models.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@RestController
public class BookingController {

    @Autowired
    private BookingDao bookingDao;

    @GetMapping(value = "/bookings")
    public ResponseEntity<Booking[]> findAll() {
        try {
            Iterable<Booking> iterator = bookingDao.findAll();
            Booking[] results = Iterables.toArray(iterator, Booking.class);
            return ResponseEntity.ok().body(results);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/bookings/{id}")
    public ResponseEntity<Booking> findById(@PathVariable String id, HttpServletResponse response) {
        try {
            Booking result = bookingDao.findById(id).orElseThrow(EntityNotFoundException::new);
            return ResponseEntity.ok().body(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/bookings")
    public ResponseEntity<Booking> save(@RequestBody Booking newBooking) {
        try {
            newBooking.initializeId();
            Booking result = bookingDao.save(newBooking);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(result.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/bookings/{id}")
    public ResponseEntity<Booking> update(@PathVariable String id, @RequestBody Booking bookingForUpdate, HttpServletResponse response) {
        try {
            Booking result = bookingDao.findById(id).orElseThrow(EntityNotFoundException::new);
            if (bookingForUpdate.getVehicleId() != null) {
                result.setVehicleId(bookingForUpdate.getVehicleId());
            }
            if (bookingForUpdate.getUserId() != null) {
                result.setUserId(bookingForUpdate.getUserId());
            }
            if (bookingForUpdate.getStartDate() != null) {
                result.setStartDate(bookingForUpdate.getStartDate());
            }
            if (bookingForUpdate.getEndDate() != null) {
                result.setEndDate(bookingForUpdate.getEndDate());
            }
            if (bookingForUpdate.getEstimatedKm() != null) {
                result.setEstimatedKm(bookingForUpdate.getEstimatedKm());
            }
            if (bookingForUpdate.getKmPrice() != null) {
                result.setKmPrice(bookingForUpdate.getKmPrice());
            }
            if (bookingForUpdate.getBasePrice() != null) {
                result.setBasePrice(bookingForUpdate.getBasePrice());
            }
            if (bookingForUpdate.getOrderIsConfirmed() != null) {
                result.setOrderIsConfirmed(bookingForUpdate.getOrderIsConfirmed());
            }
            if (bookingForUpdate.getConfirmationDate() != null) {
                result.setConfirmationDate(bookingForUpdate.getConfirmationDate());
            }
            bookingDao.flush();
            return ResponseEntity.ok().body(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(value = "/bookings/{id}")
    public void deleteById(@PathVariable String id) {
        bookingDao.deleteById(id);
    }
}
