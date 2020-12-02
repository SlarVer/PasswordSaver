package by.bsu.kas.PasswordSaver.sevice;

import by.bsu.kas.PasswordSaver.entity.Note;
import by.bsu.kas.PasswordSaver.entity.User;
import by.bsu.kas.PasswordSaver.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NoteService {
    NoteRepository noteRepository;

    @Autowired
    public void setNoteRepository(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotesByUser(User user) {
        return noteRepository.findAllByUser(user);
    }

    public List<Note> getNotesByServiceName(String service) {
        return noteRepository.findByService(service);
    }

    public Set<String> getServicesByUser(User user) {
        return getAllNotesByUser(user).stream().map(Note::getService).collect(Collectors.toSet());
    }
}
