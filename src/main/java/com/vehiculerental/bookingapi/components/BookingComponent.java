package com.vehiculerental.bookingapi.components;

import com.vehiculerental.bookingapi.dao.BookingDao;
import com.vehiculerental.bookingapi.models.Booking;
import com.vehiculerental.bookingapi.models.VehicleForm;
import com.vehiculerental.bookingapi.models.FindVehicleAvailableForm;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public final class BookingComponent {

    public static VehicleForm[] findVehiclesAvailable(FindVehicleAvailableForm vehiclesAvailableForm, RestTemplate restTemplate, BookingDao bookingDao) {
        Integer ageOfUser = getAgeOfUser(vehiclesAvailableForm.getUserId(), restTemplate);

        VehicleForm[] allVehiclesAuthorised = getAllVehiclesAuthorised(ageOfUser, restTemplate);
        Map<String, VehicleForm> vehiclesAvailable = Arrays.stream((VehicleForm[]) allVehiclesAuthorised)
                .collect(Collectors.toMap(e -> e.getId(), e -> e));

        try {
            Date startDate = vehiclesAvailableForm.getStartDate();
            Date endDate = vehiclesAvailableForm.getEndDate();

            Iterable<Booking> iterator = bookingDao.findAll();
            iterator.forEach(booking -> {
                if ((startDate.after(booking.getStartDate()) && startDate.before(booking.getEndDate()))
                    || (endDate.after(booking.getStartDate()) && endDate.before(booking.getEndDate()))
                    || (startDate.before(booking.getStartDate()) && endDate.after(booking.getEndDate()))) {
                    vehiclesAvailable.remove(booking.getVehicleId());
                }
            });
        } catch (Exception e) {
            return allVehiclesAuthorised;
        }
        return vehiclesAvailable.values().toArray(new VehicleForm[0]);
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
