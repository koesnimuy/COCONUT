package com.example.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Cours;
import com.example.entity.Teacher;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long>{
    
    public Long countBy();

    public long countByTitleContaining(String name);

    public Cours findByCoursno(Long coursno);

    public List<Cours> findByTeachercode(Teacher teacher);

    public List<Cours> findAllByCoursnoIn(List<Long> coursno);

    public List<Cours> findByTeachercodeOrderByCoursnoAsc(Teacher teacher, PageRequest pageRequest);
}
