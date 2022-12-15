package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Member;
import com.example.entity.Role;
import com.example.repository.MemberRepository;
import com.example.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    final MemberRepository memberRepository;
    final RoleRepository roleRepository;

    @Override
    public int upsertMember(Member member) {
        try {
            memberRepository.save(member);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;   
        }
    }

    @Override
    public Member selectOneMember(String id) {
        try {
            return memberRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;   
        }
    }

    @Override
    public String[] selectOneMemberRole(String id) {
        try {
            Member member = memberRepository.findById(id).orElse(null);
            List<Role> list = roleRepository.findByUserid(member);
            String[] strRole = new String[list.size()];
            int count = 0;
            for( Role role : list){
                strRole[count] = role.getName();
                System.out.println(strRole[count]);
                count++;   
            }
            return strRole;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Override
    public List<Member> selectListMember() {
        try{
            List<Member> list = memberRepository.findAll();
            return list;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public String findMemberid(Member member) {
        try {
            Member user = memberRepository.findByEmail(member.getEmail());
            return user.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int deleteMember(Member member) {
        
        try {
            memberRepository.deleteById(member.getId());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int emailCheck(String email) {
        try {
            if(memberRepository.countByEmail(email) > 0){
                return 1;
            }
            else return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
}
