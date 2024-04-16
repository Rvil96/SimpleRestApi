package ru.kata.spring.boot_security.demo.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.CascadeType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", unique = true)
    @NotEmpty(message = "Name can't be empty")
    @Size(min = 2, max = 24, message = "Min size name 2, max size name 24")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9_-]{3,16}$", message = "Does not correspond to the format")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Surname can't be empty")
    @Size(min = 2, max = 24, message = "Min size surname 2, max size surname 24")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Does not correspond to the format")
    private String surname;

    @Column(name = "age")
    @Min(value = 0)
    private int age;

    @Column(name = "password")
    @NotEmpty
    @Size(min = 2, max = 24, message = "Min size password 2, max size password 24")
    private String password;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private  Set<Role> roles;

    public User() {
    }

    public User(String name, String surname, int age, Set<Role> roles, String password) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.roles = roles;
        this.password = password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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
}
