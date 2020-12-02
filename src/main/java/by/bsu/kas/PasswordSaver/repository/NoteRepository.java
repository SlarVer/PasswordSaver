package by.bsu.kas.PasswordSaver.repository;

import by.bsu.kas.PasswordSaver.entity.Note;
import by.bsu.kas.PasswordSaver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUser(User user);
    List<Note> findByService(String setvice);
}
