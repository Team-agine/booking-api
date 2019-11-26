package com.vehiculerental.bookingapi.models;

import com.vehiculerental.bookingapi.helpers.Generate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name="bookings")
public class Booking {
    @Column(name = "id")
    @Size(min = 36, max = 36)
    @Id
    private String id;

    @Column(name = "vehicle_id")
    @Size(min = 7, max = 7)
    @NotNull
    private String vehicleId;

    @Column(name = "user_id")
    @Size(min = 36, max = 36)
    @NotNull
    private String userId;

    @Column(name = "start_date")
    @NotNull
    private Date startDate;

    @Column(name = "end_date")
    @NotNull
    private Date endDate;

    @Column(name = "estimated_km")
    @NotNull
    private Integer estimatedKm;

    @Column(name = "km_price")
    @NotNull
    private Float kmPrice;

    @Column(name = "base_price")
    @NotNull
    private Float basePrice;

    @Column(name = "order_is_confirmed")
    @NotNull
    private Boolean orderIsConfirmed;

    // if orderIsConfirmed is true, this is the date of the confirmation
    // if orderIsConfirmed is false, the booking will be deleted if it is not confirmed on this date
    @Column(name = "confirmation_date")
    @NotNull
    private Date confirmationDate;

    @Column(name="created_date")
    @CreatedDate
    private Date createdDate;

    @Column(name="updated_date")
    @LastModifiedDate
    private Date updatedDate;

    public void initializeId() {
        this.id = Generate.id();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getEstimatedKm() {
        return estimatedKm;
    }

    public void setEstimatedKm(int estimatedKm) {
        this.estimatedKm = estimatedKm;
    }

    public Float getKmPrice() {
        return kmPrice;
    }

    public void setKmPrice(float kmPrice) {
        this.kmPrice = kmPrice;
    }

    public Float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float bookingPrice) {
        this.basePrice = bookingPrice;
    }

    public Boolean isOrderIsConfirmed() {
        return orderIsConfirmed;
    }

    public void setOrderIsConfirmed(boolean orderIsConfirmed) {
        this.orderIsConfirmed = orderIsConfirmed;
    }

    public Boolean getOrderIsConfirmed() {
        return orderIsConfirmed;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }
}
