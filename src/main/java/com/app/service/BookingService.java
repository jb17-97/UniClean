package com.app.service;

import com.app.dto.AppResponse;
import com.app.dto.BookingDTO;
import com.app.dto.ListVo;
import com.app.dto.ReviewDTO;
import com.app.model.Booking;
import com.app.model.Review;
import com.app.model.User;
import com.app.repository.BookingRepository;
import com.app.repository.ReviewRepository;
import com.app.repository.UserRepository;
import com.app.util.CommonUtil;
import com.app.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ListVo> getCleaningTypeList() {
        List<ListVo> list = new ArrayList<>();
        list.add(new ListVo("", "--select one--"));
        list.add(new ListVo("Standard Cleaning", "Standard Cleaning"));
        list.add(new ListVo("Deep Cleaning", "Deep Cleaning"));
        list.add(new ListVo("Bathroom Cleaning", "Bathroom Cleaning"));
        list.add(new ListVo("Kitchen Cleaning", "Kitchen Cleaning"));

        return list;
    }

    public List<ListVo> getServiceTypeList() {
        List<ListVo> list = new ArrayList<>();
        list.add(new ListVo("", "--select one--"));
        list.add(new ListVo("Pay-per-service", "Pay-per-service"));
        list.add(new ListVo("subscription", "subscription"));
        return list;
    }

    @Transactional
    public AppResponse addBooking(BookingDTO bookingDTO, User loggedInUser) {
        System.out.println("Cleaning Time : "+bookingDTO.getCleaningTime());
        try {
            if(!RequestValidator.isBookingAddRequestValid(bookingDTO)){
                return new AppResponse(false, "Invalid value provided !");
            }

            Optional<User> cleanerOptional = userRepository.findById(Long.valueOf(bookingDTO.getCleaner()));
            if(cleanerOptional.isEmpty()){
                return new AppResponse(false, "Cleaner not found !");
            }

            if(bookingDTO.getServiceType().equals("Pay-per-service")) {

               /* Booking bookingCheck = bookingRepository.findFirstByCleaningDateAndCleaner(CommonUtil.getDateFromString(bookingDTO.getCleaningDate()), cleanerOptional.get());
                if(bookingCheck != null) {
                    return new AppResponse(false, "Cleaner already booked on : "+bookingDTO.getCleaningDate());
                }*/

                Booking booking = new Booking();
                booking.setCleaner(cleanerOptional.get());
                booking.setHour(Double.parseDouble(bookingDTO.getHour()));
                booking.setCleaningType(bookingDTO.getCleaningType());
                booking.setCleaningDate(CommonUtil.getDateFromString(bookingDTO.getCleaningDate()));
                booking.setCleaningTime(bookingDTO.getCleaningTime());
                booking.setServiceType(bookingDTO.getServiceType());
                booking.setTotalPrice(Double.parseDouble(bookingDTO.getTotalPrice()));

                booking.setBookedBy(loggedInUser);
                booking.setBookedAt(new Timestamp(System.currentTimeMillis()));

                booking.setStatus("Pending"); //initially pending. Cleaner will accept later
                bookingRepository.save(booking);
            }
            else if(bookingDTO.getServiceType().equals("subscription")) {
                if(bookingDTO.getSpecificService().equals("weekly")) { //7 days
                    Date nextCleaningDate = CommonUtil.getDateFromString(bookingDTO.getCleaningDate());
                     // 3 entries
                    for(int i=1; i<=3; i++) {

                        if(i != 1) {
                            nextCleaningDate = CommonUtil.getCustomDate(nextCleaningDate, 7);
                        }

                       /* Booking bookingCheck = bookingRepository.findFirstByCleaningDateAndCleaner(nextCleaningDate, cleanerOptional.get());
                        if(bookingCheck != null) {
                            return new AppResponse(false, "Cleaner already booked on : "+bookingDTO.getCleaningDate());
                        }*/

                        Booking booking = new Booking();
                        booking.setCleaner(cleanerOptional.get());
                        booking.setHour(Double.parseDouble(bookingDTO.getHour()));
                        booking.setCleaningType(bookingDTO.getCleaningType());
                        booking.setCleaningDate(nextCleaningDate);
                        booking.setCleaningTime(bookingDTO.getCleaningTime());
                        booking.setServiceType(bookingDTO.getServiceType());
                        booking.setTotalPrice(Double.parseDouble(bookingDTO.getTotalPrice()));

                        booking.setBookedBy(loggedInUser);
                        booking.setBookedAt(new Timestamp(System.currentTimeMillis()));

                        booking.setStatus("Pending"); //initially pending. Cleaner will accept later
                        bookingRepository.save(booking);

                    }
                }
                else if(bookingDTO.getSpecificService().equals("fortnightly")) { //every 2 weeks - 14 days
                    // 3 entries
                    Date nextCleaningDate = CommonUtil.getDateFromString(bookingDTO.getCleaningDate());
                    for(int i=1; i<=3; i++) {

                        if(i != 1) {
                            nextCleaningDate = CommonUtil.getCustomDate(nextCleaningDate, 14);
                        }

                      /*  Booking bookingCheck = bookingRepository.findFirstByCleaningDateAndCleaner(nextCleaningDate, cleanerOptional.get());
                        if(bookingCheck != null) {
                            return new AppResponse(false, "Cleaner already booked on : "+bookingDTO.getCleaningDate());
                        }*/

                        Booking booking = new Booking();
                        booking.setCleaner(cleanerOptional.get());
                        booking.setHour(Double.parseDouble(bookingDTO.getHour()));
                        booking.setCleaningType(bookingDTO.getCleaningType());
                        booking.setCleaningDate(nextCleaningDate);
                        booking.setCleaningTime(bookingDTO.getCleaningTime());
                        booking.setServiceType(bookingDTO.getServiceType());
                        booking.setTotalPrice(Double.parseDouble(bookingDTO.getTotalPrice()));

                        booking.setBookedBy(loggedInUser);
                        booking.setBookedAt(new Timestamp(System.currentTimeMillis()));

                        booking.setStatus("Pending"); //initially pending. Cleaner will accept later
                        bookingRepository.save(booking);

                    }
                }
                else if(bookingDTO.getSpecificService().equals("monthly")) {
                    // 3 entries
                    Date nextCleaningDate = CommonUtil.getDateFromString(bookingDTO.getCleaningDate());
                    // 3 entries
                    for(int i=1; i<=3; i++) {

                        if(i != 1) {
                            nextCleaningDate = CommonUtil.getCustomDateByAddingMonth(nextCleaningDate, 1);
                        }

                        Booking booking = new Booking();
                        booking.setCleaner(cleanerOptional.get());
                        booking.setHour(Double.parseDouble(bookingDTO.getHour()));
                        booking.setCleaningType(bookingDTO.getCleaningType());
                        booking.setCleaningDate(nextCleaningDate);
                        booking.setCleaningTime(bookingDTO.getCleaningTime());
                        booking.setServiceType(bookingDTO.getServiceType());
                        booking.setTotalPrice(Double.parseDouble(bookingDTO.getTotalPrice()));

                        booking.setBookedBy(loggedInUser);
                        booking.setBookedAt(new Timestamp(System.currentTimeMillis()));

                        booking.setStatus("Pending"); //initially pending. Cleaner will accept later
                        bookingRepository.save(booking);

                    }
                }
            }
            return new AppResponse(true, "Booking Added Successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AppResponse(false, "Failed to add booking !");
        }
    }


    public List<BookingDTO> getBookingList(User loggedInuser) {
        try {
            List<Booking> bookings = bookingRepository.findByBookedBy(loggedInuser);
            return prepareBookingList(bookings);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public AppResponse deleteBookingInfo(Long id) {
        bookingRepository.deleteById(id);
        return new AppResponse(true, "Booking info deleted");
    }

    public BookingDTO fetchBookingInfoById(Long id) {
        try {
            Optional<Booking> bookingOptional = bookingRepository.findById(id);
            if(bookingOptional.isPresent()){
                return processBookingInfo(bookingOptional.get());
            }
            return new BookingDTO();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new BookingDTO();
        }
    }

    public AppResponse updateBookingInfo(BookingDTO bookingDTO, User loggedInUser) {
        try {
            if(!RequestValidator.isBookingAddRequestValid(bookingDTO)){
                return new AppResponse(false, "Invalid value provided !");
            }
            if(!CommonUtil.isValueNotNullAndEmpty(bookingDTO.getId())) {
                return new AppResponse(false, "Invalid value provided !");
            }

            Optional<Booking> bookingOptional = bookingRepository.findById(Long.valueOf(bookingDTO.getId()));
            if(bookingOptional.isEmpty()) {
                return new AppResponse(false, "Invalid value provided !");
            }

            Optional<User> userOptional = userRepository.findById(Long.valueOf(bookingDTO.getCleaner()));
            if(userOptional.isEmpty()) {
                return new AppResponse(false, "Invalid value provided !");
            }

            Booking existingBookingInfo = bookingOptional.get();
            existingBookingInfo.setCleaner(userOptional.get());
            existingBookingInfo.setHour(Double.parseDouble(bookingDTO.getHour()));
            existingBookingInfo.setTotalPrice(Double.parseDouble(bookingDTO.getTotalPrice()));
            existingBookingInfo.setCleaningDate(CommonUtil.getDateFromString(bookingDTO.getCleaningDate()));
            existingBookingInfo.setCleaningType(bookingDTO.getCleaningType());

            existingBookingInfo.setUpdatedBy(loggedInUser.getUserId());
            existingBookingInfo.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            bookingRepository.save(existingBookingInfo);

            return new AppResponse(true, "Booking info updated successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AppResponse(false, "Failed to update booking info !");
        }
    }

    public List<BookingDTO> getOrderListForCleaner(User loggedInuser) {
        try {
            List<Booking> bookings = bookingRepository.findByCleaner(loggedInuser);
            return prepareBookingList(bookings);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public AppResponse acceptOrCancelOrder(BookingDTO bookingDTO, User loggedInUser) {
        try {
            if(!CommonUtil.isValueNotNullAndEmpty(bookingDTO.getId())) {
                return new AppResponse(false, "Invalid value provided !");
            }

            Optional<Booking> bookingOptional = bookingRepository.findById(Long.valueOf(bookingDTO.getId()));
            if(bookingOptional.isEmpty()) {
                return new AppResponse(false, "Invalid value provided !");
            }

            Booking existingBookingInfo = bookingOptional.get();
            existingBookingInfo.setStatus(bookingDTO.getActionStatus());

            existingBookingInfo.setUpdatedBy(loggedInUser.getUserId());
            existingBookingInfo.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            bookingRepository.save(existingBookingInfo);

            /*
            try {
                String bookedByEmail = loggedInUser.getEmail();
                String bookedCleanerEmail = bookingDTO.getEmail();

                //send email
                emailService.sendEmail(bookedByEmail, "order status", bookingDTO.getActionStatus());
                emailService.sendEmail(bookedCleanerEmail, "order status", bookingDTO.getActionStatus());
            }
            catch (Exception e){
                e.printStackTrace();
            }
             */

            return new AppResponse(true, "Booking info updated successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AppResponse(false, "Failed to update booking info !");
        }
    }

    private List<BookingDTO> prepareBookingList(List<Booking> bookings) {
        if(bookings == null || bookings.isEmpty()) {
            return new ArrayList<>();
        }
        List<BookingDTO> bookingList = new ArrayList<>();
        int counter = 1;
        for(Booking booking : bookings) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setSl(String.valueOf(counter++));
            bookingDTO.setId(String.valueOf(booking.getId()));
            bookingDTO.setCleaner(CommonUtil.getFormattedName(booking.getCleaner()));
            bookingDTO.setHour(String.valueOf(booking.getHour()));
            bookingDTO.setTotalPrice(String.valueOf(booking.getTotalPrice()));
            bookingDTO.setCleaningType(booking.getCleaningType());
            bookingDTO.setCleaningDate(String.valueOf(booking.getCleaningDate()));
            bookingDTO.setStatus(booking.getStatus());

            User bookedBy = booking.getBookedBy();
            bookingDTO.setBookedBy(bookedBy.getFirstName() + " " + bookedBy.getLastName());
            bookingDTO.setAddress(bookedBy.getAddress());
            bookingDTO.setCity(bookedBy.getCity());
            bookingDTO.setTelephone(bookedBy.getTelephone());
            bookingDTO.setEmail(bookedBy.getEmail());

            bookingList.add(bookingDTO);
        }
        return bookingList;
    }

    private BookingDTO processBookingInfo(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(String.valueOf(booking.getId()));
        bookingDTO.setCleaningDate(String.valueOf(booking.getCleaningDate()));
        bookingDTO.setCleaningType(booking.getCleaningType());
        bookingDTO.setHour(String.valueOf(booking.getHour()));
        bookingDTO.setTotalPrice(String.valueOf(booking.getTotalPrice()));
        bookingDTO.setCleaner(String.valueOf(booking.getCleaner().getUserId()));

        User bookedBy = booking.getBookedBy();
        bookingDTO.setBookedBy(bookedBy.getFirstName() + " " + bookedBy.getLastName());
        bookingDTO.setAddress(bookedBy.getAddress());
        bookingDTO.setCity(bookedBy.getCity());
        bookingDTO.setTelephone(bookedBy.getTelephone());
        bookingDTO.setEmail(bookedBy.getEmail());

        return bookingDTO;
    }


    public AppResponse addReview(ReviewDTO reviewDTO, User loggedInUser) {
        try {
            if(!CommonUtil.isValueNotNullAndEmpty(reviewDTO.getBookingId()) || !CommonUtil.isValueNotNullAndEmpty(reviewDTO.getReview())){
                return new AppResponse(false, "Invalid value provided !");
            }

            Long bookingId = Long.valueOf(reviewDTO.getBookingId());
            Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
            if(bookingOptional.isEmpty()) {
                return new AppResponse(false, "Invalid value provided !");
            }

            Review review = new Review();
            review.setReview(reviewDTO.getReview());
            review.setBookingId(bookingId);
            review.setReviewBy(loggedInUser);
            review.setReviewAt(new Timestamp(System.currentTimeMillis()));

            reviewRepository.save(review);

            return new AppResponse(true, "Review Added Successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AppResponse(false, "Failed to add review !");
        }
    }

    public List<ReviewDTO> getReviewListByBookingId(Long id) {
        try {
            List<ReviewDTO> reviewList = new ArrayList<>();
            List<Review> list = reviewRepository.findByBookingId(id);
            int counter = 1;
            for(Review review : list){
                ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.setSl(String.valueOf(counter++));
                reviewDTO.setReview(review.getReview());
                reviewDTO.setReviewTime(review.getReviewAt().toString());
                reviewDTO.setReviewBy(CommonUtil.getFormattedName(review.getReviewBy()));
                reviewList.add(reviewDTO);
            }
            return reviewList;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String getBookingStatistics (User loggedInUser) {
        StringBuilder statistics = new StringBuilder();
        try {
            List<Booking> bookings = new ArrayList<>();
            if(loggedInUser.getRole().equals("CLEANER")) {
                bookings = bookingRepository.findByCleaner(loggedInUser);
            }
            else if(loggedInUser.getRole().equals("USER")) {
                bookings = bookingRepository.findByBookedBy(loggedInUser);
            }
            else {
                return "";
            }
            //bookings = bookingRepository.findAll();
            int totalBooking = bookings.size();
            int completedBooking = 0;
            int cancelledBooking = 0;

            for (Booking booking : bookings) {
                String status = booking.getStatus();
                if(status.equals("Accepted By Cleaner")){
                    completedBooking = completedBooking + 1;
                }
                else if(status.equals("Rejected By Cleaner")){
                    cancelledBooking = cancelledBooking + 1;
                }
                statistics.append(totalBooking).append("#").append(completedBooking).append("#").append(cancelledBooking);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            statistics = new StringBuilder("0#0#0");
        }
        return statistics.toString();
    }
}
