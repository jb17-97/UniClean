package com.app.util;

import com.app.dto.CalculationDTO;
import com.app.dto.ListVo;
import com.app.model.User;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

            if(cleaningType.equals("Normal Cleaning")){
                totalPrice += (Constants.NORMAL_CLEANING_PRICE_PER_HOUR.doubleValue()*hour);
            }
            else if(cleaningType.equals("Deep Cleaning")){
                totalPrice += (Constants.DEEP_CLEANING_PRICE_PER_HOUR.doubleValue()*hour);
            }
            else {
                totalPrice += (Constants.KITCHEN_CLEANING_PRICE_PER_HOUR.doubleValue()*hour);
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
}
