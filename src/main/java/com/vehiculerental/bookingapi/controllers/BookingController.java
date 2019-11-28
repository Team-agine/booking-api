package com.vehiculerental.bookingapi.controllers;

import com.google.common.collect.Iterables;
import com.vehiculerental.bookingapi.components.BookingComponent;
import com.vehiculerental.bookingapi.dao.BookingDao;
import com.vehiculerental.bookingapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

@RestController
public class BookingController {

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private RestTemplate restTemplate;

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
    @ApiIgnore
    private ResponseEntity<Booking> save(@RequestBody Booking newBooking) {
        try {
            newBooking.initializeId();
            Booking result = bookingDao.save(newBooking);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/bookings/{id}")
    @ApiIgnore
    private ResponseEntity<Booking> update(@PathVariable String id, @RequestBody Booking bookingForUpdate) {
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

    @PostMapping(value = "/bookings/vehicles-available")
    public ResponseEntity<VehicleForm[]> findVehiclesAvailable(@Valid @RequestBody FindVehicleAvailableForm findVehicleAvailableForm) {
        try {
            VehicleForm[] results = BookingComponent.findVehiclesAvailable(findVehicleAvailableForm, restTemplate, bookingDao);
            return ResponseEntity.ok().body(results);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/bookings/prepare")
    public ResponseEntity<Booking> prepareBooking(@Valid @RequestBody PrepareBookingForm prepareBookingForm) {
        VehicleForm vehicle = restTemplate.getForObject("http://vehicles-api/vehicles/" + prepareBookingForm.getVehicleId(), VehicleForm.class);
        Booking booking = new Booking();
        booking.initializeId();
        booking.setVehicleId(prepareBookingForm.getVehicleId());
        booking.setUserId(prepareBookingForm.getUserId());
        booking.setStartDate(prepareBookingForm.getStartDate());
        booking.setEndDate(prepareBookingForm.getEndDate());
        booking.setEstimatedKm(prepareBookingForm.getEstimatedKm());
        booking.setKmPrice(vehicle.getKmPrice());
        booking.setBasePrice(vehicle.getBasePrice());
        booking.setOrderIsConfirmed(false);
        booking.setConfirmationDate(new Date());

        return save(booking);
    }

    @PutMapping(value = "/bookings/validate/{id}")
    public ResponseEntity<Booking> update(@PathVariable String id) {
        try {
            Booking result = bookingDao.findById(id).orElseThrow(EntityNotFoundException::new);
            result.setOrderIsConfirmed(true);
            bookingDao.flush();
            return ResponseEntity.ok().body(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }
}
