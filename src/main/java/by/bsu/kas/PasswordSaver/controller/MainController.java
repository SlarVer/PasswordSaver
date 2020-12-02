package by.bsu.kas.PasswordSaver.controller;

import by.bsu.kas.PasswordSaver.entity.User;
import by.bsu.kas.PasswordSaver.sevice.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class MainController {
    NoteService noteService;
    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String main(Model model) {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (o != "anonymousUser") {
            User currentUser = (User) o;
            model.addAttribute("username", currentUser.getUsername());
        }
        return "main";
    }
}