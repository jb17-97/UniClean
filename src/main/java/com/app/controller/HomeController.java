package com.app.controller;

import com.app.model.Review;
import com.app.repository.ReviewRepository;
import com.app.service.BookingService;
import com.app.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class HomeController {
	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	private final String HOME_PAGE = "home";
	private final String DASHBOARD_PAGE = "dashboard";
	private final String ABOUT_US_PAGE = "about-us";

	private final String ACTIVE_MENU = "home";

	@Autowired
	private BookingService bookingService;

	@Autowired
	private ReviewRepository reviewRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String loadHomePage(Model model, HttpSession httpSession, HttpServletRequest httpServletRequest) {
		log.info("Entering loadHomePage() method");

		if(CommonUtil.getUserFromSession(httpSession) != null) {
			return "redirect:/dashboard";
		}

		String queryParamValue = httpServletRequest.getParameter("showMore");
		if(CommonUtil.isValueNotNullAndEmpty(queryParamValue) && queryParamValue.equals("reviews")) {
			List<Review> reviews = reviewRepository.findAll();
			model.addAttribute("reviews", reviews);
		}
		else {
			List<Review> reviewList = reviewRepository.findAll();
			List<Review> firstThreeReviews = reviewList.subList(0, Math.min(reviewList.size(), 3));
			model.addAttribute("reviews", firstThreeReviews);
		}

		model.addAttribute("title", "Home");
		model.addAttribute("am", ACTIVE_MENU);

		log.info("Exiting loadHomePage() method");
		return HOME_PAGE;
    }

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String loadDashboard(Model model, HttpSession httpSession) {
		log.info("Entering loadDashboard() method");

		model.addAttribute("title", "Home");
		model.addAttribute("am", ACTIVE_MENU);

		String statistics = bookingService.getBookingStatistics(CommonUtil.getUserFromSession(httpSession));

		if(CommonUtil.isValueNotNullAndEmpty(statistics)) {
			String[] statisticsArr = statistics.split("#");
			model.addAttribute("total", statisticsArr[0]);
			model.addAttribute("completed", statisticsArr[1]);
			model.addAttribute("cancelled", statisticsArr[2]);
		}
		else {
			model.addAttribute("total", 0);
			model.addAttribute("completed", 0);
			model.addAttribute("cancelled", 0);
		}

		log.info("Exiting loadDashboard() method");
		return DASHBOARD_PAGE;
	}

	@RequestMapping(value = "/about-us", method = RequestMethod.GET)
	public String loadAboutUsPage(Model model, HttpSession httpSession) {
		log.info("Entering loadAboutUsPage() method");

		model.addAttribute("title", "About Us");

		log.info("Exiting loadAboutUsPage() method");
		return ABOUT_US_PAGE;
	}


}
