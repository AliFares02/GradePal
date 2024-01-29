package com.example.demo.classRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepo extends JpaRepository<ClassRoom, String> {
  ClassRoom findByClassId(String classId);
}
