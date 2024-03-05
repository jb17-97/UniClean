package com.app.controller;

import com.app.dto.AppResponse;
import com.app.dto.BookingDTO;
import com.app.dto.CleanerDTO;
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
@RequestMapping("/cleaner")
public class CleanerController {
    private final Logger log = LoggerFactory.getLogger(CleanerController.class);
    private final String EDIT_PAGE = "cleaner/edit";
    private final String LIST_PAGE = "cleaner/list";
    private final String REDIRECT_TO_LIST_PAGE = "redirect:/cleaner";
    private final String ACTIVE_MENU = "cleaner";

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String loadPendingCleanerPage(Model model, HttpSession httpSession) {
        log.info("Entering loadPendingCleanerPage() method");
        model.addAttribute("pageTitle", "Manage Cleaners");

        model.addAttribute("cleanerList", userService.getCleanerList());
        model.addAttribute("am", ACTIVE_MENU);

        log.info("Exiting loadPendingCleanerPage() method");
        return LIST_PAGE;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String loadCleanerEditPage(@PathVariable("id") Long id, Model model, HttpSession httpSession) {
        log.info("Entering loadCleanerEditPage() method");
        log.info("Cleaner ID : "+id);

        CleanerDTO cleanerDTO = userService.getCleanerById(id);

        model.addAttribute("pageTitle", "Approve Cleaner Info");
        model.addAttribute("cleanerDTO", cleanerDTO);
        model.addAttribute("am", ACTIVE_MENU);

        model.addAttribute("cleanerList", userService.getActiveCleanerList());
        model.addAttribute("statusList", CommonUtil.getStatusList());

        log.info("Exiting loadCleanerEditPage() method");
        return EDIT_PAGE;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String approveCleaner(Model model, @ModelAttribute CleanerDTO cleanerDTO, HttpSession httpSession,
                                  final RedirectAttributes redirectAttributes) {
        log.info("Entering approveCleaner() method");
        System.out.println("cleanerDTO : "+cleanerDTO);

        AppResponse appResponse = userService.approveCleaner(cleanerDTO, CommonUtil.getUserFromSession(httpSession));
        if(appResponse.getStatus()){
            redirectAttributes.addFlashAttribute("activity_msg", appResponse.getMessage());
            log.info("Exiting approveCleaner() method");
            return REDIRECT_TO_LIST_PAGE;
        }
        else{
            model.addAttribute("msg", appResponse.getMessage());
            model.addAttribute("cleanerDTO", cleanerDTO);
            log.info("Exiting approveCleaner() method");
            return EDIT_PAGE;
        }
    }



}
