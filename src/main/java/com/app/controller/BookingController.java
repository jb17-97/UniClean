package com.app.controller;

import com.app.dto.AppResponse;
import com.app.dto.BookingDTO;
import com.app.dto.ReviewDTO;
import com.app.repository.ReviewRepository;
import com.app.service.BookingService;
import com.app.service.UserService;
import com.app.util.CommonUtil;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/book-service")
public class BookingController {
    private final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final String ADD_PAGE = "book-service/add";
    private final String EDIT_PAGE = "book-service/edit";

    private final String REVIEW_PAGE = "book-service/review";

    private final String LIST_PAGE = "book-service/list";
    private final String REDIRECT_TO_LIST_PAGE = "redirect:/book-service";
    private final String ACTIVE_MENU = "book-service";

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.GET)
    public String loadListPage(Model model, HttpSession httpSession) {
        log.info("Entering loadListPage() method");
        model.addAttribute("pageTitle", "Booking List");

        model.addAttribute("bookingList", bookingService.getBookingList(CommonUtil.getUserFromSession(httpSession)));
        model.addAttribute("am", ACTIVE_MENU);

        log.info("Exiting loadListPage() method");
        return LIST_PAGE;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String loadAddPage(Model model, HttpSession httpSession) {
        log.info("Entering loadAddPage() method");

        model.addAttribute("pageTitle", "Booking Add");
        model.addAttribute("bookingDTO", new BookingDTO());

        model.addAttribute("cleanerList", userService.getActiveCleanerList());
        model.addAttribute("cleaningTypeList", bookingService.getCleaningTypeList());

        model.addAttribute("serviceTypeList", bookingService.getServiceTypeList());

        model.addAttribute("am", ACTIVE_MENU);
        log.info("Exiting loadAddPage() method");
        return ADD_PAGE;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBooking(Model model, HttpSession httpSession, @ModelAttribute BookingDTO bookingDTO,
                             final RedirectAttributes redirectAttributes) {
        log.info("Entering addBooking() method");
        log.info("bookingDTO request:"+bookingDTO);

        AppResponse appResponse = bookingService.addBooking(bookingDTO, CommonUtil.getUserFromSession(httpSession));
        if(appResponse.getStatus()){
            redirectAttributes.addFlashAttribute("activity_msg", appResponse.getMessage());
            log.info("Exiting addBooking() method");
            return REDIRECT_TO_LIST_PAGE;
        }
        else{
            model.addAttribute("msg", appResponse.getMessage());
            model.addAttribute("bookingDTO", bookingDTO);

            model.addAttribute("cleanerList", userService.getActiveCleanerList());
            model.addAttribute("cleaningTypeList", bookingService.getCleaningTypeList());
            model.addAttribute("serviceTypeList", bookingService.getServiceTypeList());
            log.info("Exiting addBooking() method");
            return ADD_PAGE;
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String loadBookingEditPage(@PathVariable("id") Long id, Model model, HttpSession httpSession) {
        log.info("Entering loadBookingEditPage() method");
        log.info("Booking ID : "+id);

        BookingDTO bookingDTO = bookingService.fetchBookingInfoById(id);

        model.addAttribute("pageTitle", "Edit Booking Info");
        model.addAttribute("bookingDTO", bookingDTO);
        model.addAttribute("am", ACTIVE_MENU);

        model.addAttribute("cleanerList", userService.getActiveCleanerList());
        model.addAttribute("cleaningTypeList", bookingService.getCleaningTypeList());

        log.info("Exiting loadBookingEditPage() method");
        return EDIT_PAGE;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editBookingInfo(Model model, @ModelAttribute BookingDTO bookingDTO, HttpSession httpSession,
                              final RedirectAttributes redirectAttributes) {
        log.info("Entering editBookingInfo() method");
        System.out.println("bookingDTO : "+bookingDTO);

        AppResponse appResponse = bookingService.updateBookingInfo(bookingDTO, CommonUtil.getUserFromSession(httpSession));
        if(appResponse.getStatus()){
            redirectAttributes.addFlashAttribute("activity_msg", appResponse.getMessage());
            log.info("Exiting editBookingInfo() method");
            return REDIRECT_TO_LIST_PAGE;
        }
        else{
            model.addAttribute("msg", appResponse.getMessage());
            model.addAttribute("bookingDTO", bookingDTO);
            log.info("Exiting editBookingInfo() method");
            return EDIT_PAGE;
        }
    }

    @RequestMapping(value = "/review/{id}", method = RequestMethod.GET)
    public String loadReviewPage(@PathVariable("id") Long id, Model model, HttpSession httpSession) {
        log.info("Entering loadReviewPage() method");
        log.info("Booking ID : "+id);

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setBookingId(String.valueOf(id));

        model.addAttribute("reviewList", bookingService.getReviewListByBookingId(id));
        model.addAttribute("pageTitle", "Review");
        model.addAttribute("reviewDTO", reviewDTO);
        model.addAttribute("am", ACTIVE_MENU);

        log.info("Exiting loadReviewPage() method");
        return REVIEW_PAGE;
    }

    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public String submitReview(Model model, @ModelAttribute ReviewDTO reviewDTO, HttpSession httpSession,
                                  final RedirectAttributes redirectAttributes) {
        log.info("Entering submitReview() method");
        System.out.println("reviewDTO : "+reviewDTO);

        AppResponse appResponse = bookingService.addReview(reviewDTO, CommonUtil.getUserFromSession(httpSession));

        if(appResponse.getStatus()){
            redirectAttributes.addFlashAttribute("activity_msg", appResponse.getMessage());
            log.info("Exiting submitReview() method");
            return REDIRECT_TO_LIST_PAGE;
        }
        else{
            model.addAttribute("msg", appResponse.getMessage());
            model.addAttribute("reviewDTO", reviewDTO);
            log.info("Exiting submitReview() method");
            return EDIT_PAGE;
        }
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteBookingInfo(@PathVariable("id") Long id, HttpSession httpSession, final RedirectAttributes redirectAttributes) {
        log.info("Entering deleteBookingInfo() method");
        log.info("Booking ID : "+id);

        AppResponse appResponse = bookingService.deleteBookingInfo(id);
        redirectAttributes.addFlashAttribute("activity_msg", appResponse.getMessage());

        log.info("Exiting deleteBookingInfo() method");
        return REDIRECT_TO_LIST_PAGE;
    }

}
