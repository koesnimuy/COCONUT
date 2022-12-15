package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Member;
import com.example.entity.Role;
import com.example.entity.Teacher;
import com.example.repository.MemberRepository;
import com.example.repository.teacher.TeacherRepository;
import com.example.service.MemberService;
import com.example.service.RoleService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class HomeController {

    final PasswordEncoder passwordEncoder;
    final MemberService memberService;
    final RoleService roleService;
    final MemberRepository memberRepository;
    final TeacherRepository teacherRepository;

    @GetMapping(value = {"/", "/main" })
    public String basichome(HttpSession httpSession){
        httpSession.setAttribute("type", 2);
        return "forward:/vue/student/index.html";
        
    }

    @GetMapping(value="/teacher.do")
    public String teacher(HttpSession httpSession) {       
        System.out.println("티쳐겟");
        httpSession.setAttribute("type", 1);
        return "forward:/vue/teacher/index.html";
    }

    @GetMapping(value = "/adhome")
    public String home(
        @AuthenticationPrincipal User user,
        Model model, HttpSession httpSession
    ) {
        httpSession.setAttribute("type", 0);
        System.out.println(user);
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping(value="/page403")
    public String page403GET() {
        return "page403";
    }
    
    
    @GetMapping("/login.do")
    public String login() {
        return "login";
    }

    @GetMapping("/kakao")
    public String kakaoPayGet() {
        return "kakaopay/test";
    }

    @GetMapping("/adjoin")
    public String adjoin() {
        return "join";
    }

    @PostMapping("/adjoin.do")
    public String adjoinPOST(
        Model model,
        @RequestParam(name = "id") String id,
        @RequestParam(name = "pw") String pw,
        @RequestParam(name = "code") String code
    ) {
        
            if(code.equals("COCONUT")){
                Member member = new Member();        
                member.setId(id);
                String hashpw = passwordEncoder.encode(pw);
                member.setPw(hashpw);

                // 멤버 아이디가 존재하지 않을 경우 저장
                if(!memberRepository.findById(member.getId()).isPresent()){
                    List<Role> list = new ArrayList<>();

                    Role role = new Role();
                    Role role1 = new Role();
                    Role role2 = new Role();
                    Teacher teacher = new Teacher();
                    role.setName("STUDENT");
                    role.setUserid(member);
                    role1.setName("TEACHER");
                    role1.setUserid(member);
                    role2.setName("ADMIN");
                    role2.setUserid(member);
                    teacher.setCategory("운영");
                    teacher.setIntro("");
                    teacher.setSteps(2);
                    teacher.setMember(member);

                    list.add(role);
                    list.add(role1);
                    list.add(role2);
                    
                    memberService.upsertMember(member);
                    roleService.insertAdminRole(list);
                    teacherRepository.save(teacher);
                    return "redirect:/adhome";
                }
                return "redirect:/adjoin";
            }
            else return "redirect:/adjoin";
        }
   
}
