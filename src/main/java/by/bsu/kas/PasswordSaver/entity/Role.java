package by.bsu.kas.PasswordSaver.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    private long id;

    private String name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Role))
            return false;
        Role role = (Role) o;
        return Objects.equals(this.id, role.id) && Objects.equals(this.name, role.name) &&
                Objects.equals(this.users, role.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.users);
    }

    @Override
    public String toString() {
        return "Role{id=" + this.id + ", name='" + this.name + '\'' +
                "users={" + this.users.stream().map(User::getUsername).collect(Collectors.joining(", ")) + "}}";
    }
}
