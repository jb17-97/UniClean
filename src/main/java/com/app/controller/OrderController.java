package com.app.controller;

import com.app.dto.AppResponse;
import com.app.dto.BookingDTO;
import com.app.dto.ReviewDTO;
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

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final String LIST_PAGE = "order/list";
    private final String EDIT_PAGE = "order/edit";

    private final String REVIEW_PAGE = "order/review";
    private final String REDIRECT_TO_LIST_PAGE = "redirect:/order";
    private final String ACTIVE_MENU = "order";

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String loadOrderListPage(Model model, HttpSession httpSession) {
        log.info("Entering loadOrderListPage() method");
        model.addAttribute("pageTitle", "Manager Orders");

        model.addAttribute("orderList", bookingService.getOrderListForCleaner(CommonUtil.getUserFromSession(httpSession)));
        model.addAttribute("am", ACTIVE_MENU);

        log.info("Exiting loadOrderListPage() method");
        return LIST_PAGE;
    }

    @RequestMapping(value = "/review/{id}", method = RequestMethod.GET)
    public String loadReviewPage(@PathVariable("id") Long id, Model model, HttpSession httpSession) {
        log.info("Entering loadReviewPage() method");
        log.info("Booking ID : "+id);

        List<ReviewDTO> reviewList =  bookingService.getReviewListByBookingId(id);

        model.addAttribute("pageTitle", "Reviews");
        model.addAttribute("reviewList", reviewList);

        model.addAttribute("am", ACTIVE_MENU);

        log.info("Exiting loadReviewPage() method");
        return REVIEW_PAGE;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String loadOrderEditPage(@PathVariable("id") Long id, Model model, HttpSession httpSession) {
        log.info("Entering loadOrderEditPage() method");
        log.info("Booking ID : "+id);

        BookingDTO bookingDTO = bookingService.fetchBookingInfoById(id);

        model.addAttribute("pageTitle", "Edit Booking Info");
        model.addAttribute("bookingDTO", bookingDTO);
        model.addAttribute("am", ACTIVE_MENU);

        model.addAttribute("cleanerList", userService.getActiveCleanerList());
        model.addAttribute("cleaningTypeList", bookingService.getCleaningTypeList());

        log.info("Exiting loadOrderEditPage() method");
        return EDIT_PAGE;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String acceptOrCancelOrder(Model model, @ModelAttribute BookingDTO bookingDTO, HttpSession httpSession,
                                  final RedirectAttributes redirectAttributes) {
        log.info("Entering acceptOrCancelOrder() method");
        System.out.println("bookingDTO : "+bookingDTO);

        AppResponse appResponse = bookingService.acceptOrCancelOrder(bookingDTO, CommonUtil.getUserFromSession(httpSession));
        if(appResponse.getStatus()){
            redirectAttributes.addFlashAttribute("activity_msg", appResponse.getMessage());
            log.info("Exiting acceptOrCancelOrder() method");
            return REDIRECT_TO_LIST_PAGE;
        }
        else{
            model.addAttribute("msg", appResponse.getMessage());
            model.addAttribute("bookingDTO", bookingDTO);
            log.info("Exiting acceptOrCancelOrder() method");
            return EDIT_PAGE;
        }
    }
}
