package com.example.demo.classRoom;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClassRoomService {

  @Autowired
  private final ClassRoomRepo classRoomRepo;

  public ClassRoomService(ClassRoomRepo classRoomRepo) {
    this.classRoomRepo = classRoomRepo;
  }

  public List<ClassRoom> getClassrooms() {
    return classRoomRepo.findAll();
  }

  public ClassRoom getAClassroom(String classId) {
    ClassRoom classRoom = classRoomRepo.findByClassId(classId);
    if (classRoomRepo.findById(classId) == null) {
      throw new NoSuchElementException("Class not found");
    }
    return classRoom;
  }

  public void addClassroom(ClassRoom classRoom) {
    if (classRoomRepo.findByClassId(classRoom.getClassId()) != null) {
      throw new IllegalStateException("A classroom with classId: " + classRoom.getClassId() + " already exists");
    }
    classRoomRepo.save(classRoom);
  }

  public ResponseEntity<?> updateClassroom(String classId, ClassRoom updatedClassroom) {
    ClassRoom existingClassRoom = classRoomRepo.findByClassId(classId);
    if (existingClassRoom == null) {
      throw new IllegalStateException("Cant update classroom because class does not exist");
    }
    if (updatedClassroom.getName() != null) {
      existingClassRoom.setName(updatedClassroom.getName());
    }
    if (updatedClassroom.getStartDate() != null) {
      existingClassRoom.setStartDate(updatedClassroom.getStartDate());
    }
    if (updatedClassroom.getDescription() != null) {
      existingClassRoom.setDescription(updatedClassroom.getDescription());
    }
    classRoomRepo.save(existingClassRoom);
    return ResponseEntity.ok(existingClassRoom);
  }

  public ResponseEntity<?> deleteClassroom(String classId) {
    ClassRoom existingClassRoom = classRoomRepo.findByClassId(classId);
    if (existingClassRoom == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classroom not found");
    }
    classRoomRepo.deleteById(classId);
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }

}
