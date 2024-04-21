package com.app.util;

import com.app.dto.BookingOptionDTO;
import com.app.dto.CalculationDTO;
import com.app.dto.ListVo;
import com.app.model.User;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {
    public static Boolean isValueNotNullAndEmpty(Object key) {
        return key != null && !key.toString().trim().isEmpty();
    }

    public static String getEncodedPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static Boolean isPasswordValid(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static User getUserFromSession(HttpSession httpSession){
        return (User) httpSession.getAttribute("user");
    }

    public static Date getDateFromString (String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Double calculateTotalCleaningPrice(CalculationDTO calculationDTO) {
        try {
            Double totalPrice = (double) 0;
            Double hour = Double.parseDouble(calculationDTO.getHour());
            String cleaningType = calculationDTO.getCleaningType();

            if(cleaningType.equals("Standard Cleaning")){
                totalPrice += (Constants.STANDARD_CLEANING_PRICE_PER_HOUR*hour);
            }
            else if(cleaningType.equals("Deep Cleaning")){
                totalPrice += (Constants.DEEP_CLEANING_PRICE_PER_HOUR*hour);
            }
            else if(cleaningType.equals("Bathroom Cleaning")){
                totalPrice += (Constants.BATHROOM_CLEANING_PRICE_PER_HOUR*hour);
            }
            else if(cleaningType.equals("Kitchen Cleaning")){
                totalPrice += (Constants.KITCHEN_CLEANING_PRICE_PER_HOUR*hour);
            }

            // 10% discount
            if(calculationDTO.getServiceType().equals("subscription")) {
                totalPrice = totalPrice - (totalPrice*(0.10));
            }

            return totalPrice;
        }
        catch (Exception e) {
            e.printStackTrace();
            return (double) 0;
        }
    }

    public static String getFormattedName(User user) {
        String name = user.getFirstName();
        if(CommonUtil.isValueNotNullAndEmpty(user.getLastName())) {
            name += " " + user.getLastName();
        }
        name += "(" + user.getGender() + ")";
        return name;
    }

    public static List<ListVo> getStatusList() {
        List<ListVo> list = new ArrayList<>();
        list.add(new ListVo("", "--select one--"));
        list.add(new ListVo("Pending", "Pending"));
        list.add(new ListVo("Approved", "Approved"));
        return list;
    }

    public static Date getCustomDate(Date customDate, int day) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(customDate);

            // Add the specified number of days
            calendar.add(Calendar.DAY_OF_MONTH, day);

            // Return the modified date
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getCustomDateByAddingMonth(Date customDate, int counter) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(customDate);

            // Add the no. of month
            calendar.add(Calendar.MONTH, counter);

            // Return the modified date
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getAllBookingOptions(BookingOptionDTO bookingOptionDTO) {
        String bookingOptions = "";
        try {
            if(bookingOptionDTO.getSpecificService().equals("weekly")) {
                Date dateOne = getDateFromString(bookingOptionDTO.getCleaningDate());
                Date dateTwo = getCustomDate(dateOne, 7);
                Date dateThree = getCustomDate(dateTwo, 7);
                bookingOptions += getReadableDateFormat(dateOne) + "#" + getReadableDateFormat(dateTwo) + "#" + getReadableDateFormat(dateThree);
                return  bookingOptions;
            }
            else if(bookingOptionDTO.getSpecificService().equals("fortnightly")) {
                Date dateOne = getDateFromString(bookingOptionDTO.getCleaningDate());
                Date dateTwo = getCustomDate(dateOne, 14);
                Date dateThree = getCustomDate(dateTwo, 14);
                bookingOptions += getReadableDateFormat(dateOne) + "#" + getReadableDateFormat(dateTwo) + "#" + getReadableDateFormat(dateThree);
                return  bookingOptions;
            }
            else if(bookingOptionDTO.getSpecificService().equals("monthly")) {
                Date dateOne = getDateFromString(bookingOptionDTO.getCleaningDate());
                Date dateTwo = getCustomDateByAddingMonth(dateOne, 1);
                Date dateThree = getCustomDateByAddingMonth(dateTwo, 1);
                bookingOptions += getReadableDateFormat(dateOne) + "#" + getReadableDateFormat(dateTwo) + "#" + getReadableDateFormat(dateThree);
                return  bookingOptions;
            }

            return  bookingOptions;
        } catch (Exception e) {
            e.printStackTrace();
            return bookingOptions;
        }
    }

    private static String getReadableDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        return formatter.format(date);
    }

}
