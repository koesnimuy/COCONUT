package com.example.repository.teacher;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Member;
import com.example.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    public Teacher findByMember(Member member);

    // 강사(신청자) 조회
    public List<Teacher> findByStepsOrderByTeachernoDesc(int steps, PageRequest pageRequest);

    // 강사(신청자) 수
    public int countBySteps(int steps);

    // 강사(신청자) 조회 검색
    public List<Teacher> findByStepsAndTeachernoContainingAndCategoryContainingAndMemberContainingOrderByTeachernoDesc( int steps, Long teacherno, String category, Member member, PageRequest pageRequest );

    // 강사(신청자) 수
    public int countByStepsAndTeachernoContainingAndCategoryContainingAndMemberContaining(int steps, Long teacherno, String category, Member member);

    // 강사 신청자 처리 내역
    public List<Teacher> findByOrderByTeachernoDesc(PageRequest pageRequest);

}
