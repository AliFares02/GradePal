package com.example.demo.student;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classRoom.ClassRoom;
import com.example.demo.classRoom.ClassRoomRepo;
import com.example.demo.user.ApplicationUser;
import com.example.demo.user.UserRepository;

@RestController
@RequestMapping(path = "api/students")
public class StudentController {

  @Autowired
  private final StudentService studentService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private StudentRepo studentRepo;

  @Autowired
  private ClassRoomRepo classRoomRepo;

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @GetMapping("/{userId}/{classId}")
  public ResponseEntity<?> getStudents(@PathVariable("userId") Integer userId, @PathVariable("classId") String classId,
      Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }
    try {
      ApplicationUser findUser = userRepository.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      if (!findUser.getUsername().equals(authentication.getName())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User unauthorized");
      }
      ClassRoom classroom = null;
      for (ClassRoom room : findUser.getClassRooms()) {
        if (room.getClassId().equals(classId))
          classroom = room;
      }
      if (classroom == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Classroom: " + classId + " does not belong to this user");
      }
      return ResponseEntity.ok(classroom.getStudents());
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving students of: " + classId);
    }
  }

  @PostMapping(path = "/{userId}/{classId}/add-student")
  public ResponseEntity<?> addStudent(@PathVariable("userId") Integer userId, @PathVariable("classId") String classId,
      @RequestBody Student student, Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }
    try {
      ApplicationUser findUser = userRepository.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      if (!findUser.getUsername().equals(authentication.getName())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User unauthorized");
      }
      ClassRoom classroom = null;
      for (ClassRoom room : findUser.getClassRooms()) {
        if (room.getClassId().equals(classId))
          classroom = room;
      }
      if (classroom == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Classroom: " + classId + " does not belong to this user");
      }
      for (Student aStudent : classroom.getStudents()) {
        if (aStudent.getStudentId().equals(student.getStudentId()))
          return ResponseEntity.status(HttpStatus.CONFLICT)
              .body("Student with id: " + student.getStudentId() + " already exists in this classroom");
      }
      classroom.getStudents().add(student);
      student.setClassRoom(classroom);
      classRoomRepo.save(classroom);
      studentRepo.save(student);
      return ResponseEntity.ok(student);
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving students of: " + classId);
    }
  }

  @PatchMapping(path = "/{userId}/{classId}/{studentId}/update-student")
  public ResponseEntity<?> updateStudent(@PathVariable("userId") Integer userId,
      @PathVariable("classId") String classId, @PathVariable("studentId") String studentId,
      @RequestBody Student updatedStudent, Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }
    try {
      ApplicationUser findUser = userRepository.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      if (!findUser.getUsername().equals(authentication.getName())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User unauthorized");
      }
      ClassRoom classroom = null;
      for (ClassRoom room : findUser.getClassRooms()) {
        if (room.getClassId().equals(classId))
          classroom = room;
      }
      if (classroom == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Classroom: " + classId + " does not belong to this user");
      }
      Student studentToUpdate = null;
      for (Student aStudent : classroom.getStudents()) {
        if (aStudent.getStudentId().equals(studentId))
          studentToUpdate = aStudent;
      }

      if (studentToUpdate == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with id: " + studentId + " not found");
      }

      return studentService.updateStudent(classId, studentId, updatedStudent);
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating student: " + studentId);
    }
  }

  @DeleteMapping(path = "/{userId}/{classId}/{studentId}/delete-student")
  public ResponseEntity<?> deleteStudent(@PathVariable("userId") Integer userId,
      @PathVariable("classId") String classId, @PathVariable("studentId") String studentId,
      Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }
    try {
      ApplicationUser findUser = userRepository.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      if (!findUser.getUsername().equals(authentication.getName())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User unauthorized");
      }
      ClassRoom classroom = null;
      for (ClassRoom room : findUser.getClassRooms()) {
        if (room.getClassId().equals(classId))
          classroom = room;
      }
      if (classroom == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Classroom: " + classId + " does not belong to this user");
      }
      Student studentToDelete = null;
      for (Student aStudent : classroom.getStudents()) {
        if (aStudent.getStudentId().equals(studentId))
          studentToDelete = aStudent;
      }
      if (studentToDelete == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with id: " + studentId + " not found");
      }
      classroom.getStudents().remove(studentToDelete);
      studentRepo.delete(studentToDelete);
      classRoomRepo.save(classroom);
      return ResponseEntity.status(HttpStatus.OK).body(studentId + " successfully deleted");
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating student: " + studentId);
    }
  }
}
