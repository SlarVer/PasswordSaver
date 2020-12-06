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
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

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
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        List<Note> currentNotes = noteService.getNotesByServiceName(service).stream().map(e -> e.getOriginalPassword(currentUser)).collect(Collectors.toList());
        model.addAttribute("notes", currentNotes);
        return "note";
    }

    @GetMapping("/notes/add")
    public String addNoteWithNewService(Model model) {
        model.addAttribute(new Note());
        return "addWithNewService";
    }

    @PostMapping("/notes/add")
    public String addNoteWithNewServiceProcess(@ModelAttribute("note") Note newNote, Model model) {
        if (noteService.saveNote(newNote, (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            model.addAttribute("successAddMessage", "Account successfully added");
        } else {
            model.addAttribute("errorAddMessage", "Account with this username is already linked with current strvice");
        }
        return "addWithNewService";
    }

    @GetMapping("/notes/{service}/add")
    public String addNote(Model model, @PathVariable String service) {
        model.addAttribute(new Note());
        return "add";
    }

    @PostMapping("/notes/{service}/add")
    public String addNoteProcess(@PathVariable(value = "service") String service,
                                  @ModelAttribute("note") Note newNote, Model model) {
            if (noteService.saveNote(newNote, (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
                model.addAttribute("successAddMessage", "Account successfully added");
            } else {
                model.addAttribute("errorAddMessage", "Account with this username is alredy linked with current strvice");
            }
        return "add";
    }
}
