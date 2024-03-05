package com.app.util;

import com.app.dto.*;

public class RequestValidator {

    public static Boolean isSignUpRequestValid(SignupRequest signupRequest) {
        if(signupRequest == null) {
            return false;
        }
        return CommonUtil.isValueNotNullAndEmpty(signupRequest.getFirstName())
                && CommonUtil.isValueNotNullAndEmpty(signupRequest.getLastName())
                && CommonUtil.isValueNotNullAndEmpty(signupRequest.getEmail())
                && CommonUtil.isValueNotNullAndEmpty(signupRequest.getPassword())
                && CommonUtil.isValueNotNullAndEmpty(signupRequest.getGender())
                && CommonUtil.isValueNotNullAndEmpty(signupRequest.getRegistrationType());
    }

    public static Boolean isLoginUpRequestValid(LoginRequest loginRequest) {
        if(loginRequest == null) {
            return false;
        }
        return CommonUtil.isValueNotNullAndEmpty(loginRequest.getEmail())
                && CommonUtil.isValueNotNullAndEmpty(loginRequest.getPassword());
    }

    public static boolean isBookingAddRequestValid(BookingDTO bookingDTO) {
        try {
            if(bookingDTO == null) {
                return false;
            }
            if(!CommonUtil.isValueNotNullAndEmpty(bookingDTO.getCleaner())
                    || !CommonUtil.isValueNotNullAndEmpty(bookingDTO.getHour())
                    || !CommonUtil.isValueNotNullAndEmpty(bookingDTO.getCleaningDate())
                    || !CommonUtil.isValueNotNullAndEmpty(bookingDTO.getTotalPrice())
                    || !CommonUtil.isValueNotNullAndEmpty(bookingDTO.getCleaningType())) {
                return false;
            }
            Double.parseDouble(bookingDTO.getHour());
            Double.parseDouble(bookingDTO.getTotalPrice());
            CommonUtil.getDateFromString(bookingDTO.getCleaningDate());
            Long.valueOf(bookingDTO.getCleaner());

            if(Double.parseDouble(bookingDTO.getTotalPrice()) < 0) {
                return false;
            }

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isCleanerApprovalRequestValid(CleanerDTO cleanerDTO) {
        try {
            if(cleanerDTO == null) {
                return false;
            }
            if(!CommonUtil.isValueNotNullAndEmpty(cleanerDTO.getId())) {
                return false;
            }
            Long.valueOf(cleanerDTO.getId());
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isPasswordChangeRequestValid(PasswordChangeDTO passwordChangeDTO) {
        try {
            if(passwordChangeDTO == null) {
                return false;
            }
            if(!CommonUtil.isValueNotNullAndEmpty(passwordChangeDTO.getNewPassword()) || !CommonUtil.isValueNotNullAndEmpty(passwordChangeDTO.getConfirmNewPassword())) {
                return false;
            }
            if(!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())){
                return false;
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
