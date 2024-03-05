package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private String sl;
    private String bookingId;
    private String review;
    private String reviewTime;
    private String reviewBy;
}
