package com.example.demo.user;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.classRoom.ClassRoom;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class ApplicationUser implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer userId;
  @Column(unique = true)
  private String username;
  private String password;

  @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL)
  private List<ClassRoom> classRooms;

  public List<ClassRoom> getClassRooms() {
    return classRooms;
  }

  public void setClassRooms(List<ClassRoom> classRooms) {
    this.classRooms = classRooms;
  }

  public ApplicationUser() {
  }

  public ApplicationUser(Integer userId, String username, String password) {
    this.userId = userId;
    this.username = username;
    this.password = password;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
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
  public String toString() {
    return "ApplicationUser [userId=" + userId + ", username=" + username + ", password=" + password + ", classRooms="
        + classRooms + "]";
  }

}
