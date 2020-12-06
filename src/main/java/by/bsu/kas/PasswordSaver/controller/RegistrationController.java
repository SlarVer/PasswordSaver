package by.bsu.kas.PasswordSaver.controller;

import by.bsu.kas.PasswordSaver.entity.User;
import by.bsu.kas.PasswordSaver.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    UserService userService;

    @ModelAttribute
    public void globalPageAttributes(Model model) {
        model.addAttribute(new User());
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationProcess(@RequestParam String repeatPassword, @ModelAttribute("user") @Valid User newUser,
                                      Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "registration";
        } else if (!newUser.getPassword().equals(repeatPassword)) {
            model.addAttribute("passwordRepeatError", "Passwords don't match");
        } else {
            if (userService.saveUser(newUser)) {
                userService.generateKeys(newUser.getUsername());
                return "redirect:login";
            } else {
                model.addAttribute("userExistsError", "Username already taken");
            }
        }

        return "registration";
    }
}
