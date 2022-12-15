package com.example.repository.teacher;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Education;
import com.example.entity.Teacher;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    public List<Education> findByTeacher(Teacher teacher);
        
}
