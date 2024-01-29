package com.example.demo.classRoom;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.student.Student;
import com.example.demo.user.ApplicationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "classroom")
public class ClassRoom {

  @Id
  @Column(unique = true)
  private String classId;

  private String name;
  @Column(length = 300)
  private String description;

  private LocalDate startDate;

  private Double classAvg;

  public Double getClassAvg() {
    return classAvg;
  }

  public void setClassAvg(Double classAvg) {
    this.classAvg = classAvg;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
  private List<Student> students;

  @ManyToOne
  @JsonIgnore
  private ApplicationUser applicationUser;

  public ApplicationUser getApplicationUser() {
    return applicationUser;
  }

  public void setApplicationUser(ApplicationUser applicationUser) {
    this.applicationUser = applicationUser;
  }

  public ClassRoom() {
  }

  public ClassRoom(String name, String classId, String description, LocalDate startDate) {
    this.name = name;
    this.classId = classId;
    this.description = description;
    this.startDate = startDate;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public String getClassId() {
    return classId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Student> getStudents() {
    return students;
  }

  public void setStudents(List<Student> students) {
    this.students = students;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "ClassRoom [classId=" + classId + ", name=" + name + ", description=" + description + ", startDate="
        + startDate + ", classAvg=" + classAvg + ", students=" + students + ", applicationUser=" + applicationUser
        + "]";
  }

}
