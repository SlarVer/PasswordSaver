package by.bsu.kas.PasswordSaver.sevice;

import by.bsu.kas.PasswordSaver.crypto.Aes;
import by.bsu.kas.PasswordSaver.crypto.asymmetric.Constants;
import by.bsu.kas.PasswordSaver.crypto.asymmetric.MainAlgo;
import by.bsu.kas.PasswordSaver.crypto.asymmetric.SupportAlgo;
import by.bsu.kas.PasswordSaver.entity.Note;
import by.bsu.kas.PasswordSaver.entity.Role;
import by.bsu.kas.PasswordSaver.entity.User;
import by.bsu.kas.PasswordSaver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import static by.bsu.kas.PasswordSaver.crypto.asymmetric.SupportAlgo.bigIntegerToString;
import static by.bsu.kas.PasswordSaver.crypto.asymmetric.SupportAlgo.stringToBigInteger;

@Service
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User \"" + username + "\" not found");
        }
        return user;
    }

    public boolean saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null){
            return false;
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
            user.setProfilePic("0.png");
            userRepository.save(user);
            return true;
        }
    }

    public boolean checkPassword(String username, String rawPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, userRepository.findByUsername(username).getPassword());
    }

    @Transactional
    public void changePassword(String username, String password) {
        userRepository.findByUsername(username).setPassword(bCryptPasswordEncoder.encode(password));
    }

    @Transactional
    public void changePicture(String username, String pictureLink) {
        userRepository.findByUsername(username).setProfilePic(pictureLink);
    }

    @Transactional
    public void generateKeys(String username) {
        User currentUser = userRepository.findByUsername(username);
        List<BigInteger> asymmetricKeys = MainAlgo.generateKeys(Constants.KEY_SIZE_ASYMMETRIC);
        currentUser.setOpenKey(bigIntegerToString(asymmetricKeys.get(1)));
        currentUser.setSecretKey(bigIntegerToString(asymmetricKeys.get(2)));
        try {
            BigInteger aesKey = stringToBigInteger(Aes.generateKey());
            if (aesKey.compareTo(Constants.ZERO) < 0) {
                currentUser.setAesKeySign(-1);
                aesKey = aesKey.negate();
            } else {
                currentUser.setAesKeySign(1);
            }
            currentUser.setAesKey(bigIntegerToString(MainAlgo.encrypt(asymmetricKeys.get(1), Constants.E, aesKey)));
        } catch (NoSuchAlgorithmException ignored) { }

    }
}
