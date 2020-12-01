package by.bsu.kas.PasswordSaver.controller;


import by.bsu.kas.PasswordSaver.entity.User;
import by.bsu.kas.PasswordSaver.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ProfileController {
    @Autowired
    UserService userService;

    @ModelAttribute
    public void globalPageAttributes(Model model, Principal principal) {
        User currentUser = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("username", principal.getName());
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/profile/password")
    public String passwordChange(Model model) {
        model.addAttribute(new User());
        return "password";
    }

    @PostMapping("/profile/password")
    public String passwordChangeAction(@RequestParam String currentPassword, @RequestParam String repeatNewPassword,
                                       @ModelAttribute("user") @Valid User tempUser, Errors errors, Model model, Principal principal) {
        if (!userService.checkPassword(principal.getName(), currentPassword)) {
            model.addAttribute("passwordCheckError", "Incorrect current password");
            return "password";
        }
        if (!tempUser.getPassword().equals(repeatNewPassword)) {
            model.addAttribute("passwordRepeatError", "Passwords don't match");
            return "password";
        }
        if (tempUser.getPassword().equals(currentPassword)) {
            model.addAttribute("passwordMatchError", "New password must be different from the current one");
            return "password";
        }
        if (errors.hasErrors()) {
            return "password";
        }
        userService.changePassword(principal.getName(), tempUser.getPassword());
        model.addAttribute("passwordUpdateSuccess", "Password cuccessfully updated");
        return "password";
    }

    @GetMapping("/profile/picture")
    public String pictureChange() {
        return "picture";
    }

    @GetMapping("/profile/picture/{id}")
    public String pictureChangeProcess(@PathVariable(value = "id") String pictureId, Principal principal) {
        userService.changePicture(principal.getName(), pictureId + ".png");
        return "redirect:/profile";
    }

}

