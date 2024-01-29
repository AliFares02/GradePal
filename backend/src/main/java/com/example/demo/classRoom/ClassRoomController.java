package com.example.demo.classRoom;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.student.Student;
import com.example.demo.user.ApplicationUser;
import com.example.demo.user.UserRepository;

@RestController
@RequestMapping(path = "api/classroom")
@CrossOrigin(origins = "http://localhost:3000")
public class ClassRoomController {

  @Autowired
  private final ClassRoomService classRoomService;

  @Autowired
  private ClassRoomRepo classRoomRepo;

  @Autowired
  private UserRepository userRepository;

  public ClassRoomController(ClassRoomService classRoomService) {
    this.classRoomService = classRoomService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getClassrooms(@PathVariable("userId") Integer userId, Authentication authentication) {
    if (!authentication.isAuthenticated() || authentication == null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }
    try {
      ApplicationUser findUser = userRepository.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      if (!findUser.getUsername().equals(authentication.getName())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      return ResponseEntity.ok(findUser.getClassRooms());
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving user for verification");
    }
  }

  @GetMapping(path = "/{userId}/{classId}")
  public ResponseEntity<?> getAClassroom(@PathVariable("userId") Integer userId,
      @PathVariable("classId") String classId, Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }
    try {
      ApplicationUser findUser = userRepository.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      if (!findUser.getUsername().equals(authentication.getName())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      ClassRoom classToGet = null;
      for (ClassRoom classroom : findUser.getClassRooms()) {
        if (classroom.getClassId().equals(classId))
          classToGet = classroom;
      }
      if (classToGet == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classroom not found");
      }
      double gradeTotoal = 0.0;
      for (Student student : classToGet.getStudents()) {
        gradeTotoal += student.getGrade();
      }
      classToGet.setClassAvg(gradeTotoal / classToGet.getStudents().size());
      return ResponseEntity.ok(classToGet);
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PostMapping("/{userId}/add-classroom")
  public ResponseEntity<?> addClassRoom(@PathVariable("userId") Integer userId,
      @RequestBody ClassRoom classRoom,
      Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    try {
      ApplicationUser userToAddClassTo = userRepository.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found for userId: " + userId));

      if (userToAddClassTo.getUsername().equals(authentication.getName())) {
        if (classRoomRepo.existsById(classRoom.getClassId())) {
          return ResponseEntity.status(HttpStatus.CONFLICT)
              .body("A class with classId: " + classRoom.getClassId() + " already exists");
        }
        classRoom.setApplicationUser(userToAddClassTo);
        userToAddClassTo.getClassRooms().add(classRoom);
        userRepository.save(userToAddClassTo);
        classRoomRepo.save(classRoom);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body("Classroom successfully added to user: " + userToAddClassTo.getUsername());
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("User not authorized to add classroom to this user.");
      }
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during classroom addition");
    }
  }

  @PatchMapping(path = "/{userId}/{classId}")
  public ResponseEntity<?> updateClassroom(@PathVariable("userId") Integer userId,
      @PathVariable("classId") String classId, @RequestBody ClassRoom updatedClassroom, Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }
    try {
      ApplicationUser findUser = userRepository.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      if (!findUser.getUsername().equals(authentication.getName())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      ClassRoom classToUpdate = null;
      for (ClassRoom classroom : findUser.getClassRooms()) {
        if (classroom.getClassId().equals(classId))
          classToUpdate = classroom;
      }
      if (classToUpdate == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classroom not found");
      }
      return classRoomService.updateClassroom(classId, updatedClassroom);
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping(path = "/{userId}/{classId}")
  public ResponseEntity<?> deleteClassroom(@PathVariable("userId") Integer userId,
      @PathVariable("classId") String classId, Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }
    try {
      ApplicationUser findUser = userRepository.findById(userId)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      if (!findUser.getUsername().equals(authentication.getName())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      ClassRoom classToDelete = null;
      for (ClassRoom classroom : findUser.getClassRooms()) {
        if (classroom.getClassId().equals(classId)) {
          classToDelete = classroom;

        }
      }
      if (classToDelete == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classroom not found");
      }
      findUser.getClassRooms().remove(classToDelete);
      classRoomRepo.delete(classToDelete);
      return ResponseEntity.status(HttpStatus.ACCEPTED).body("Classroom: " + classId + " successfully deleted");
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occured while deleting user");
    }
  }

}
