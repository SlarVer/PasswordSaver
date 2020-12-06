package by.bsu.kas.PasswordSaver.sevice;

import by.bsu.kas.PasswordSaver.crypto.Aes;
import by.bsu.kas.PasswordSaver.crypto.asymmetric.Constants;
import by.bsu.kas.PasswordSaver.crypto.asymmetric.MainAlgo;
import by.bsu.kas.PasswordSaver.entity.Note;
import by.bsu.kas.PasswordSaver.entity.User;
import by.bsu.kas.PasswordSaver.repository.NoteRepository;
import by.bsu.kas.PasswordSaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static by.bsu.kas.PasswordSaver.crypto.asymmetric.SupportAlgo.bigIntegerToString;
import static by.bsu.kas.PasswordSaver.crypto.asymmetric.SupportAlgo.stringToBigInteger;

@Service
public class NoteService {
    NoteRepository noteRepository;

    @Autowired
    public void setNoteRepository(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public boolean saveNote(Note note, User user) {
        if (noteRepository.findByServiceAndUsername(note.getService(), note.getUsername()) != null) {
            return false;
        } else{
            BigInteger decryptedAESKey = MainAlgo.decrypt(stringToBigInteger(user.getOpenKey()),
                    stringToBigInteger(user.getSecretKey()),
                    stringToBigInteger(user.getAesKey()));
            if (user.getAesKeySign() < 0) {
                decryptedAESKey = decryptedAESKey.negate();
            }
            note.setPassword(Aes.encrypt(note.getPassword(), bigIntegerToString(decryptedAESKey)));
            note.setUser(user);
            noteRepository.save(note);
            return true;
        }
    }
}
