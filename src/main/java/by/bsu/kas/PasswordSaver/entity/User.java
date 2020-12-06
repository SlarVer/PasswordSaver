package by.bsu.kas.PasswordSaver.entity;

import by.bsu.kas.PasswordSaver.validation.ValidPassword;
import com.sun.istack.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 6, max = 15, message = "Length must be between 6 and 15 characters")
    private String username;

    @ValidPassword             //Custom annotation
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @NotNull
    @Size(min = 2, max = 40, message = "Length must be between 2 and 40 characters")
    private String name;

    @NotNull
    private Date birthDate;

    private String profilePic;

    private String aesKey;

    private int aesKeySign;

    private String secretKey;

    private String openKey;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Note> notes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public String getRolesToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Role role: getRoles()){
            stringBuilder.append(new StringBuilder(role.getName()).delete(0, 5)).append(" ");
        }
        return stringBuilder.toString().trim();
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getOpenKey() {
        return openKey;
    }

    public void setOpenKey(String openKey) {
        this.openKey = openKey;
    }

    public int getAesKeySign() {
        return aesKeySign;
    }

    public void setAesKeySign(int aesKeySign) {
        this.aesKeySign = aesKeySign;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public User(){
    }

    public User(String username, String password, String name, Date birthDate) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return Objects.equals(this.id, user.id) && Objects.equals(this.username, user.username) &&
                Objects.equals(this.password, user.password) && Objects.equals(this.name, user.name) &&
                Objects.equals(this.birthDate, user.birthDate) && Objects.equals(this.profilePic, user.profilePic) &&
                Objects.equals(this.notes, user.notes) && Objects.equals(this.roles, user.roles) &&
                Objects.equals(this.aesKey, user.aesKey) && Objects.equals(this.openKey, user.openKey) &&
                Objects.equals(this.secretKey, user.secretKey) && Objects.equals(this.aesKeySign, user.aesKeySign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.username, this.password, this.name, this.birthDate,
                this.profilePic, this.notes, this.roles, this.aesKey, this.openKey, this.secretKey, this.aesKeySign);
    }

    @Override
    public String toString() {
        return "User{id=" + this.id + ", username='" + this.username + '\'' +
                ", password='" + this.password + '\'' + ", name='" + this.name + '\'' +
                ", birthDate='" + this.birthDate + '\'' + ", profilePic='" + this.profilePic + '\'' +
                ", notes={" + this.notes.stream().map(Note::getService).collect(Collectors.joining(", ")) + '}' +
                ", roles={" + this.roles.stream().map(Role::getName).collect(Collectors.joining(", ")) + "}}";
    }

}
