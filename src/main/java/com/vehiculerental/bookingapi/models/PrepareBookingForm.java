package com.vehiculerental.bookingapi.models;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class PrepareBookingForm {
    @NotNull
    private String userId;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private String vehicleId;

    @NotNull
    private Integer estimatedKm;

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

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getEstimatedKm() {
        return estimatedKm;
    }

    public void setEstimatedKm(Integer estimatedKm) {
        this.estimatedKm = estimatedKm;
    }
}
