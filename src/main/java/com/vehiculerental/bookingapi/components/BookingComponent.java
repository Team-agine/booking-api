package com.vehiculerental.bookingapi.components;

import com.vehiculerental.bookingapi.dao.BookingDao;
import com.vehiculerental.bookingapi.models.Booking;
import com.vehiculerental.bookingapi.models.VehicleForm;
import com.vehiculerental.bookingapi.models.VehiclesAvailableForm;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class BookingComponent {

    public static VehicleForm[] findVehiclesAvailable(VehiclesAvailableForm vehiclesAvailableForm, RestTemplate restTemplate, BookingDao bookingDao) {
        Integer ageOfUser = getAgeOfUser(vehiclesAvailableForm.getUserId(), restTemplate);

        VehicleForm[] allVehiclesAuthorised = getAllVehiclesAuthorised(ageOfUser, restTemplate);
        Map<String, VehicleForm> vehiclesAvailable = Arrays.asList((VehicleForm[]) allVehiclesAuthorised)
                .stream()
                .collect(Collectors.toMap(e -> e.getId(), e -> e));

        try {
            Date startDate = vehiclesAvailableForm.getStartDate();
            Date endDate = vehiclesAvailableForm.getEndDate();

            Iterable<Booking> iterator = bookingDao.findAll();
            iterator.forEach(booking -> {
                if ((booking.getStartDate().compareTo(startDate) < 0 && startDate.compareTo(booking.getEndDate()) < 0)
                    || (booking.getStartDate().compareTo(endDate) < 0 && endDate.compareTo(booking.getEndDate()) < 0)) {
                    //vehiclesAvailable.remove(booking.getVehicleId());
                }
                System.out.println(booking.getVehicleId());
            });
        } catch (Exception e) {
            System.out.println("booking.getVehicleId()");
            return allVehiclesAuthorised;
        }
        //allVehiclesAuthorised = (VehicleForm[]) vehiclesAvailable.values().toArray();
        return allVehiclesAuthorised;
    }

    private static Integer getAgeOfUser(String id, RestTemplate restTemplate) {
        HttpEntity<String> entity = new HttpEntity<>(id);
        ResponseEntity<String> result = restTemplate.exchange("http://users-api/users/age", HttpMethod.POST, entity, String.class);
        return Integer.parseInt(result.getBody());
    }

    private static VehicleForm[] getAllVehiclesAuthorised(Integer ageOfUser, RestTemplate restTemplate) {
        HttpEntity<Integer> entity;
        ResponseEntity<VehicleForm[]> results;
        if (ageOfUser < 21) {
            entity = new HttpEntity<>(8);
            results = restTemplate.exchange("http://vehicles-api/vehicles/maximum-horse-power", HttpMethod.POST, entity, VehicleForm[].class);
        } else if (ageOfUser < 25) {
            entity = new HttpEntity<>(13);
            results = restTemplate.exchange("http://vehicles-api/vehicles/maximum-horse-power", HttpMethod.POST, entity, VehicleForm[].class);
        } else {
            results = restTemplate.exchange("http://vehicles-api/vehicles", HttpMethod.GET, null, VehicleForm[].class);
        }
        return results.getBody();
    }
}
