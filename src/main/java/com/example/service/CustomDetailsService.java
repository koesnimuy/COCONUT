package com.example.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.dto.CustomUser;
import com.example.entity.Member;
import com.example.entity.Role;
import com.example.repository.MemberRepository;
import com.example.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CustomDetailsService implements UserDetailsService{
    final String format = "SECURITY => {}";

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(format, username); 
        // 아이디가 전송되면 UserDetails 타입으로 변환해서 리턴
        Member member = memberRepository.findById(username).orElse(null);
        System.out.println("*****detailService****");
        if(member != null){
            // 권한 여러개 처리를 위해
            List<Role> list = roleRepository.findByUserid(member);

            String[] strRole = new String[list.size()];
            int count = 0;
            for( Role role : list){
                strRole[count] = role.getName();
                System.out.println(strRole[count]);
                count++;   
            }
            Collection<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(strRole);

            // 아이디, 암호, 권한을 컬렉션 타입
            CustomUser user = new CustomUser(member.getId(), member.getPw(), roles );
            System.out.println(user.toString());         
           return user;
        }
        else { // 로그인 실패 처리
            String[] strRole = { "_" };
            Collection<GrantedAuthority> role = AuthorityUtils.createAuthorityList(strRole);
            // 아이디, 암호, 권한을 컬렉션 타입            
            return new User(username, "", role);
        }
    }

}
