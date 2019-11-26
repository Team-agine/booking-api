package com.vehiculerental.bookingapi.dao;

import com.vehiculerental.bookingapi.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDao extends JpaRepository<Booking, String> {

}
