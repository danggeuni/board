package com.board.board.controller;

import com.board.board.controller.dto.user.AddUserRequest;
import com.board.board.controller.dto.user.LoginRequest;
import com.board.board.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    public UserController(UserService userService, HttpSession session){
        this.userService = userService;
        this.session = session;
    }

    @GetMapping("/register")
    public String AddUser(Model model, String checkPwd){
        model.addAttribute("data", new AddUserRequest());
        model.addAttribute("checkPwd", checkPwd);
        return "user/register";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute AddUserRequest request, String checkPwd){
        userService.registerUser(request, checkPwd);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String loginUser(Model model){
        model.addAttribute("data", new LoginRequest());
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request){
        userService.loginUser(request);
        session.setAttribute("userId", request.getUserId());
        return "redirect:/board";
    }

    @GetMapping("/logout")
    public String logout(){
        session.invalidate();
        return "redirect:/board";
    }

}
