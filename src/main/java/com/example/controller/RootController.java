package com.example.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController implements ErrorController{
    
    @GetMapping("/error")
    public String errorGET(HttpServletRequest request, HttpSession httpSession, Model model){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int type = (int) httpSession.getAttribute("type");
        System.out.println(type);
        System.out.println(status);
        System.out.println(request.getRequestURI());
        
        if (type == 1) {
            return "forward:/vue/teacher/index.html";
        }
        if (type == 0) {
            if (status.equals(404)) {
                return "redirect:/adhome";
            }
            else{
                model.addAttribute("status", status);
                return "admin/test";
            }
        }
        return "forward:/vue/student/index.html";

    }
}
