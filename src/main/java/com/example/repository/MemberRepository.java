package com.example.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    
    // 아이디 찾기용(이름, 이메일)
    public Member findByNameAndEmail(String name, String email);

    // 아이디 찾기용(이메일)
    public Member findByEmail(String email);

    // 이메일 체크
    public int countByEmail(String email);

    // 관리자용(회원 리스트 검색)
    public List<Member> findByIdContainingOrderByIdAsc(String id, PageRequest pageRequest);

    // 총 회원 수
    public int countByIdContaining(String id);

    // 회원 조회
    public List<Member> findByIdContainingOrEmailContainingOrNameContainingOrPhoneContainingOrderByRegdateDesc(String id, String email, String name, String phone, PageRequest pageRequest);

    public int countByIdContainingOrEmailContainingOrNameContainingOrPhoneContaining(String id, String email, String name, String phone);

}
