package by.bsu.kas.PasswordSaver.controller;

import by.bsu.kas.PasswordSaver.entity.Note;
import by.bsu.kas.PasswordSaver.entity.User;
import by.bsu.kas.PasswordSaver.sevice.NoteService;
import by.bsu.kas.PasswordSaver.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class NotesController {
    UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    NoteService noteService;
    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes")
    public String servicesPageShow(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("serviceList", noteService.getServicesByUser(currentUser));
        return "notes";
    }

    @GetMapping("/notes/{service}")
    public String notesPageShow(@PathVariable(value = "service") String service, Model model) {
        model.addAttribute("username", ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        List<Note> currentNotes = noteService.getNotesByServiceName(service);
        model.addAttribute("notes", currentNotes);
        return "note";
    }
}
