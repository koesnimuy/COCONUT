package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Member;

@Service
public interface MemberService {
    
    // 회원가입, 수정
    public int upsertMember(Member Member);

    // 회원 1명 조회(ID)
    public Member selectOneMember(String id);

    // 회원 1명 권한 조회(배열)
    public String[] selectOneMemberRole(String id);

    // 회원 리스트 조회
    public List<Member> selectListMember();
    public String findMemberid(Member member);

    public int deleteMember(Member member);

    public int emailCheck(String email);
}
