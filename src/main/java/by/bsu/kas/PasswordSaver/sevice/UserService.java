package by.bsu.kas.PasswordSaver.sevice;

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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;

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
}
