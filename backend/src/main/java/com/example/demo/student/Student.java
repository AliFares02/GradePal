package com.example.demo.student;

import com.example.demo.classRoom.ClassRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {

  @Id
  @Column(unique = true)
  private String studentId;

  private String name;
  private Integer age;
  private Integer grade;

  @ManyToOne
  @JsonIgnore
  private ClassRoom classRoom;

  public Student(String name, String studentId, Integer age, Integer grade) {
    this.name = name;
    this.studentId = studentId;
    this.age = age;
    this.grade = grade;
  }

  public Student() {
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public ClassRoom getClassRoom() {
    return classRoom;
  }

  public void setClassRoom(ClassRoom classRoom) {
    this.classRoom = classRoom;
  }

  public Integer getGrade() {
    return grade;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

}
