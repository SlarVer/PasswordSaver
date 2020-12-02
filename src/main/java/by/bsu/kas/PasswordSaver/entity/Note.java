package by.bsu.kas.PasswordSaver.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String service;

    private String username;

    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Note))
            return false;
        Note note = (Note) o;
        return Objects.equals(this.id, note.id) && Objects.equals(this.service, note.service) &&
                Objects.equals(this.username, note.username) && Objects.equals(this.password, note.password) &&
                Objects.equals(this.user, note.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.service, this.username, this.password, this.user);
    }

    @Override
    public String toString() {
        return "Note{id=" + this.id + ", service='" + this.service + "', username='" + this.username +
                "', password='" + this.password + "', user='" + this.user.getUsername() + "'}";
    }
}
