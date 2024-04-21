package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private String sl;
    private String id;
    private String cleaner;
    //normal cleaning, deep cleaning or kitchen cleaning
    private String cleaningType;

    //Pay-per-service, subscription
    private String serviceType;
    private String specificService; // weekly, fortnightly or monthly

    private String cleaningDate;
    private LocalTime cleaningTime;
    private String hour;
    private String totalPrice;

    private String status;
    private String bookedBy;

    private String actionStatus;

    private String address;
    private String city;
    private String telephone;
    private String email;

}
