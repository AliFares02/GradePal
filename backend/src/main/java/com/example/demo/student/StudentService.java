package com.example.demo.student;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.classRoom.ClassRoom;
import com.example.demo.classRoom.ClassRoomRepo;

@Service
public class StudentService {

  private final StudentRepo studentRepo;
  private final ClassRoomRepo classRoomRepo;

  @Autowired
  public StudentService(StudentRepo studentRepo, ClassRoomRepo classRoomRepo) {
    this.studentRepo = studentRepo;
    this.classRoomRepo = classRoomRepo;
  }

  public List<Student> getAllStudents() {
    return studentRepo.findAll();
  }

  public void addStudent(String classId, Student student) {
    ClassRoom classExists = classRoomRepo.findByClassId(classId);
    if (classExists == null) {
      throw new IllegalStateException("Cant add student because class does not exist");
    }
    // if (studentRepo.findByStudentId(student.getStudentId()) != null) {
    // throw new IllegalStateException("Student already exists");
    // }
    student.setClassRoom(classExists);
    studentRepo.save(student);

  }

  public ResponseEntity<?> updateStudent(String classId, String studentId, Student updatedStudent) {
    ClassRoom classExists = classRoomRepo.findByClassId(classId);
    if (classExists == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classroom not found");
    }
    Student existingStudent = studentRepo.findById(studentId)
        .orElseThrow(() -> new NoSuchElementException("Cant update student. Student not found"));

    if (updatedStudent != null) {
      if (updatedStudent.getAge() != null) {
        existingStudent.setAge(updatedStudent.getAge());
      }
      if (updatedStudent.getGrade() != null) {
        existingStudent.setGrade(updatedStudent.getGrade());
      }
      if (updatedStudent.getName() != null) {
        existingStudent.setName(updatedStudent.getName());
      }
      studentRepo.save(existingStudent);
    } else {
      throw new IllegalArgumentException("Updated student information is null");
    }
    return ResponseEntity.ok(existingStudent);
  }

  public ResponseEntity<?> deleteStudent(String studentId) {
    Student existingStudent = studentRepo.findById(studentId)
        .orElseThrow(() -> new NoSuchElementException("Cant delete student. Student not found"));
    studentRepo.delete(existingStudent);
    return ResponseEntity.status(HttpStatus.OK).body(studentId + " deleted");
  }
}
