package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.ApiExamMemberProfile;
import com.example.dto.MemberPw;
import com.example.dto.email.EmailAuthRequestDto;
import com.example.entity.Chat;
import com.example.entity.Member;
import com.example.entity.MemberImage;
import com.example.entity.Role;
import com.example.entity.Teacher;
import com.example.entity.Token;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberImageRepository;
import com.example.repository.MemberRepository;
import com.example.repository.RoleRepository;
import com.example.repository.chat.ChatRepository;
import com.example.repository.teacher.TeacherRepository;
import com.example.service.MemberService;
import com.example.service.RoleService;
import com.example.service.TokenService;
import com.example.service.email.EmailService;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping(value = "/api/member")
@RequiredArgsConstructor
public class MemberRestController {
    
    final RoleRepository roleRepository;
    final TeacherRepository teacherRepository;
    final JwtUtil jwtUtil;
    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;
    final MemberService memberService;
    final RoleService roleService;
    final TokenService tokenService;
    final MemberImageRepository memberImageRepository;
    final ResourceLoader resourceLoader;
    final MemberRepository memberRepository;
    final ApiExamMemberProfile apiExamMemberProfile;
    final EmailService emailService;
    final ChatRepository chatRepository;

    @GetMapping(value = "/idcheck.json")
    public Map<String, Object> idcheckGET(
        @RequestParam(name="id")String id) {
            Map<String, Object> map = new HashMap<>();
            try {
                boolean ret = memberRepository.existsById(id);
                map.put("status", 200);
                map.put("result", ret); // ????????? true, ????????? false
            }
            catch(Exception e) {
                map.put("status", -1);
                map.put("result", e.getMessage());
            }
            return map;
    }

    // 127.0.0.1:8081/COCO/api/member/emailcheck
    // email??? ???????????? result = 1 ???????????? ????????? result = 0
    @GetMapping(value = "/emailcheck")
    public Map<String, Object> emailcheckGET(
        @RequestParam(name="email") String email) {
            Map<String, Object> map = new HashMap<>();
            try {
                map.put("status", 200);
                map.put("result", memberService.emailCheck(email));
            }
            catch(Exception e) {
                map.put("status", -1);
                map.put("result", e.getMessage());
            }
            return map;
    }


    // ????????????
    // 127.0.0.1:8081/COCO/api/member/join.json
    @PostMapping(value="/join.json")
    public Map<String, Object> joinPOST(@RequestBody Member member) {
        // System.out.println(member.toString());
        Map<String, Object> map = new HashMap<>();
        try {
            String hashpw = passwordEncoder.encode(member.getPw());
            member.setPw(hashpw);

            map.put("status", 0);
            // ?????? ???????????? ???????????? ?????? ?????? ??????
            if(!memberRepository.findById(member.getId()).isPresent()){
                if(memberService.emailCheck(member.getEmail()) == 0){
                    Role role = new Role();
                    role.setName("STUDENT");
                    role.setUserid(member);
                    
                    memberService.upsertMember(member);
                    roleService.upsertRole(role);

                    Chat chat = new Chat();
                    chat.setSender("coconut");
                    chat.setContent("COCONUT ???????????? ???????????? ?????? ???????????????!");
                    chat.setReceiver(member.getId());
                    chat.setMember(member);
                    chatRepository.save(chat);

                    map.put("status", 200);
                }
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ????????????(????????????)
    // 127.0.0.1:8081/COCO/api/member/adminjoin.json
    @PostMapping(value="/adminjoin.json")
    public Map<String, Object> joinAdminPOST(
        @RequestBody Member member
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String hashpw = passwordEncoder.encode(member.getPw());
            member.setPw(hashpw);

            map.put("status", 0);
            // ?????? ???????????? ???????????? ?????? ?????? ??????
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
                teacher.setCategory("??????");
                teacher.setIntro("");
                teacher.setSteps(2);
                teacher.setMember(member);

                list.add(role);
                list.add(role1);
                list.add(role2);
                
                memberService.upsertMember(member);
                roleService.insertAdminRole(list);
                teacherRepository.save(teacher);
            
                map.put("status", 200);
            }
            
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ?????????
    // 127.0.0.1:8081/COCO/api/member/login.json
    @PostMapping(value = "/login.json")
    public Map<String, Object> loginPOST(
                @RequestBody Member member){
        Map<String, Object> map = new HashMap<>();
        try {
            if(memberRepository.findById(member.getId()).orElse(null).getBlock() == 0){
                // 1. ?????? ?????? ????????? ?????? ????????????
                String[] strRole = memberService.selectOneMemberRole(member.getId());
                // for(int i=0; i<strRole.length; i++){
                //     System.out.println(strRole[i]);
                // }
                // 2. ??????????????? Collection???????????? ??????
                Collection<GrantedAuthority> roles
                    = AuthorityUtils.createAuthorityList(strRole);

                // 3. ???????????? ????????? ??????, detailsService??? ?????? ??????
                UsernamePasswordAuthenticationToken upat = 
                    new UsernamePasswordAuthenticationToken(
                        member.getId(), member.getPw(), roles);
                
                authenticationManager.authenticate(upat);
                String token = jwtUtil.generateToken(member.getId(), strRole);

                // ?????? ??????
                // System.out.println(Arrays.asList(strRole).contains("TEACHER")); 
                
                Token tokenDB = new Token();
                tokenDB.setMemberid(member.getId());
                tokenDB.setToken(token);

                // tokenDB.setStudenturl("http://127.0.0.1:9090");
                // if(Arrays.asList(strRole).contains("TEACHER")){
                //     tokenDB.setTeacherurl("http://127.0.0.1:9091?token="+token);
                // }
                
                tokenService.upsertToken(tokenDB);

                map.put("status", 200);
                map.put("result", token);
            }
            else{
                map.put("status", 0);
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 127.0.0.1:8081/COCO/api/member/logout.json
    @PostMapping(value = "/logout.json")
    public Map<String, Object> logoutPOST(
        HttpServletRequest request){
            Map<String, Object> map = new HashMap<>();
            try {
                // ?????????????????? (??????, ?????????)
                String username = (String) request.getAttribute("username");
                // System.out.println(username);
                int ret = tokenService.deleteToken(username);

                map.put("status", 200);
                map.put("result", ret);
            }
            catch(Exception e) {
                map.put("status", -1);
                map.put("result", e.getMessage());
            }
            return map;
        }

    // ?????? ?????????
    // 127.0.0.1:8081/COCO/api/member/teacherlogin.json
    @PostMapping(value = "/teacherlogin.json")
    public Map<String, Object> teacherloginPOST(
                @RequestBody Member member){
        Map<String, Object> map = new HashMap<>();
        try {
            // 1. ?????? ?????? ????????? ?????? ????????????
            String[] strRole = memberService.selectOneMemberRole(member.getId());

            // 2. ??????????????? Collection???????????? ??????
            Collection<GrantedAuthority> roles
                = AuthorityUtils.createAuthorityList(strRole);

            // 3. ???????????? ????????? ??????, detailsService??? ?????? ??????
            UsernamePasswordAuthenticationToken upat = 
                new UsernamePasswordAuthenticationToken(
                    member.getId(), member.getPw(), roles);
            
            authenticationManager.authenticate(upat);
            String token = jwtUtil.generateToken(member.getId(), strRole);

            // ?????? ??????
            // System.out.println(Arrays.asList(strRole).contains("TEACHER")); 
            
            Token tokenDB = new Token();
            tokenDB.setMemberid(member.getId());
            tokenDB.setToken(token);

            map.put("status", 0);
            if(Arrays.asList(strRole).contains("TEACHER")){
                tokenService.upsertToken(tokenDB);
                map.put("status", 200);
                map.put("result", token);    
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ?????? ?????????
    // 127.0.0.1:8081/COCO/api/member/tokenlogin.json
    @PostMapping(value = "/tokenlogin.json")
    public Map<String, Object> tokenloginPOST(
                HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        try {
            // ???????????? ????????? ??????
            String username = (String) request.getAttribute("username");
            // System.out.println(username);

            // ???????????? ?????? ?????? ??????
            Member member = memberService.selectOneMember(username);
            // System.out.println(member.toString());

            // ?????? ?????? ??????
            String[] strRole = memberService.selectOneMemberRole(member.getId());
            map.put("status", 0);
            if(Arrays.asList(strRole).contains("TEACHER")){
                // Token token = tokenService.selectOne(username);
                // map.put("result", token.getTeacherurl());
                map.put("status", 200);
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ???????????? ????????? ?????? ?????? ?????? ??????
    // 127.0.0.1:8081/COCO/api/member/update.json
    @PutMapping(value="/update.json")
    public Map<String, Object> updatePUT(
                HttpServletRequest request,
                @RequestBody Member member){
        Map<String, Object> map = new HashMap<>();
        // System.out.println(request.toString());
        try {
            // ?????????????????? (??????, ?????????)
            String username = (String) request.getAttribute("username");
            // System.out.println(username);
            Member updateMember = memberService.selectOneMember(username);
            updateMember.setName(member.getName());
            updateMember.setPhone(member.getPhone());
            updateMember.setBirthday(member.getBirthday());
            updateMember.setEmail(member.getEmail());
            int ret = memberService.upsertMember(updateMember);

            map.put("status", 200);
            map.put("update", updateMember.toString());
            map.put("result", ret);
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ???????????? ??????
    // 127.0.0.1:8081/COCO/api/member/updatepw.json
    @PutMapping(value="/updatepw.json")
    public Map<String, Object> updatepwPUT(
                @RequestHeader(name = "TOKEN") String token,
                @RequestBody MemberPw member){
        Map<String, Object> map = new HashMap<>();
        // System.out.println(request.toString());
        try {
            // ?????????????????? (????????????)
            String username = jwtUtil.extractUsername(token);
            // String username = (String) request.getAttribute("username");
            member.setId(username);
            member.setUpdatepw(passwordEncoder.encode(member.getUpdatepw()));

            // System.out.println(member.toString());

            // ???????????? ???????????? ?????? ????????? ?????????.
            Member updateMember = memberService.selectOneMember(username);
            // System.out.println(updateMember.toString());
            // ????????? ??????, hash??? DB?????? ??????
            map.put("result", -1);
            if(passwordEncoder.matches(member.getPw(), updateMember.getPw())){
                updateMember.setPw(member.getUpdatepw());
                int ret = memberService.upsertMember(updateMember);
                map.put("result", ret);
            }
            map.put("status", 200);
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ???????????? ??????(?????? ????????? 1???)
    // 127.0.0.1:8081/COCO/api/member/selectone.json
    @GetMapping(value="/selectone.json")
    public Map<String, Object> selectoneGET(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) request.getAttribute("username");
            Member member = memberService.selectOneMember(username);
            if(memberImageRepository.countByUseridContaining(username) > 0) {
                String imgurl = memberImageRepository.findByUserid(username).getNo()+"";
                member.setImg(imgurl);
            }
            map.put("status", 200);
            // System.out.println(member.toString());
            map.put("result", member);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // ???????????? ??????(?????????)
    // 127.0.0.1:8081/COCO/api/member/selectlist.json
    @GetMapping(value="/selectlist.json")
    public Map<String, Object> selectlistGET() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Member> member = memberService.selectListMember();
            map.put("status", 200);
            map.put("result", member);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // ????????????
    // 127.0.0.1:8081/COCO/api/member/delete.json
    @PostMapping(value = "/delete.json")
    public Map<String, Object> delete(@RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            Member member = memberService.selectOneMember(username);
            member.setBlock(1);
            member.setBirthday(null);
            member.setEmail(null);
            member.setName(null);
            member.setPhone(null);
            memberRepository.save(member);
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ?????? ?????? ?????? (????????? ?????????)
    // 127.0.0.1:8081/COCO/api/member/selectonerole.json
    @GetMapping(value="/selectonerole.json")
    public Map<String, Object> selectoneroleGET(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userid = request.getAttribute("username").toString();
            String[] roles = memberService.selectOneMemberRole(userid);

            map.put("status", 200);
            map.put("result", roles);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    //<img src="/image?no=1" >
    // 127.0.0.1:8081/COCO/api/member/image?no=1
    @GetMapping(value="/image")
    public ResponseEntity<byte[]> imageGET(
        @RequestParam(name="no") Long no) throws IOException{
        
            if(no > 0L) {           
                MemberImage memberImage = memberImageRepository.findById(no).orElse(null);
                if(memberImage.getImagesize() > 0L) {
                    // ???????????? png?????? jpg?????? gif??????
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(
                        MediaType.parseMediaType( memberImage.getImagetype() ) );
                    // ????????????????????????, ?????????????????? header, status 200    
                    ResponseEntity<byte[]> response 
                        = new ResponseEntity<>(
                            memberImage.getImagedata(), headers, HttpStatus.OK);
                    return response;        
                }
                else {
                    InputStream is = resourceLoader.getResource("classpath:/static/image/default.jpg")
                        .getInputStream();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    // ????????????????????????, ?????????????????? header, status 200    
                    ResponseEntity<byte[]> response 
                        = new ResponseEntity<>(
                            is.readAllBytes(), headers, HttpStatus.OK);
                    return response;
                }
            }
            else {
                InputStream is = resourceLoader.getResource("classpath:/static/image/default.jpg")
                        .getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                // ????????????????????????, ?????????????????? header, status 200    
                ResponseEntity<byte[]> response 
                    = new ResponseEntity<>(
                        is.readAllBytes(), headers, HttpStatus.OK);
                return response;
            }
    }

    // 127.0.0.1:8081/COCO/api/member/naverlogin.json
    @PostMapping(value="/naverlogin.json")
    public Map<String, Object> naverjoinPOST(@RequestBody String navertoken) {
        JSONObject jsontoken = new JSONObject(navertoken);
        Map<String, Object> map = new HashMap<>();
        try {
            String token = jsontoken.get("token").toString(); // ????????? ????????? ?????? ??????;
            // System.out.println(token);
            String header = "Bearer " + token; // Bearer ????????? ?????? ??????

            String apiURL = "https://openapi.naver.com/v1/nid/me";

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", header);
            // String responseBody = apiExamMemberProfile.get(apiURL,requestHeaders);
            JSONObject responseBody = new JSONObject(apiExamMemberProfile.get(apiURL,requestHeaders));

            // System.out.println(responseBody.toString());
            JSONObject profile = responseBody.getJSONObject("response");
            // System.out.println(responseBody.toString());        

            // // ????????? ???????????? ?????? ????????? ??????
            String email = "naver" + profile.get("email").toString();
            String memberid = email.substring(0, email.indexOf("@"));
            // System.out.println("??????????????????" + memberid);
             
            Member member = new Member();
            member.setId(memberid);
            // System.out.println(member.toString());
            // System.out.println(!memberRepository.findById(memberid).isPresent());

            if(!memberRepository.findById(member.getId()).isPresent()){ // 
                String hashpw = passwordEncoder.encode(profile.get("id").toString());
                member.setPw(hashpw);
                member.setName(profile.get("name").toString());
                member.setPhone(profile.get("mobile").toString());
                member.setEmail(profile.get("email").toString());

                // member2.setPhone(member.get("mobile").toString());
                // System.out.println(member.toString());
                // System.out.println("??????");
                memberService.upsertMember(member);

                // ??????
                Role role = new Role();
                role.setUserid(member);
                List<Role> list = roleRepository.findByUserid(member);
                // System.out.println(memberService.selectOneMember(member.getId()).getRoles());
                
                if(list.isEmpty()){
                    role.setName("STUDENT");
                    roleService.upsertRole(role);
                } 
            }
            // System.out.println(memberService.selectOneMember(member.getId()).getRoles());
            Member member1 = memberRepository.findById(memberid).orElse(null);
            // System.out.println(member1.toString());
            // 1. ?????? ?????? ????????? ?????? ????????????
            // System.out.println(memberRepository.findById(member.getId()).isPresent());
            if(memberRepository.findById(member.getId()).isPresent()){
                String[] strRole = memberService.selectOneMemberRole(member1.getId());

                // 2. ??????????????? Collection???????????? ??????
                Collection<GrantedAuthority> roles
                    = AuthorityUtils.createAuthorityList(strRole);

                // 3. ???????????? ????????? ??????, detailsService??? ?????? ??????
                UsernamePasswordAuthenticationToken upat = 
                    new UsernamePasswordAuthenticationToken(
                        member1.getId(), profile.get("id").toString(), roles);
                
                authenticationManager.authenticate(upat);
                String token1 = jwtUtil.generateToken(member1.getId(), strRole);

                // ?????? ??????
                // System.out.println(Arrays.asList(strRole).contains("TEACHER"));
                // System.out.println(member1.toString()); 
                
                Token tokenDB = new Token();
                tokenDB.setMemberid(member1.getId());
                tokenDB.setToken(token1);
                // tokenDB.setStudenturl("http://127.0.0.1:9090");
                // if(Arrays.asList(strRole).contains("TEACHER")){
                //     tokenDB.setTeacherurl("http://127.0.0.1:9091?token="+token1);
                // }
                tokenService.upsertToken(tokenDB);
                map.put("result", token1);
            }
            map.put("status", 200);
            
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ????????? ??????
    // ?????? ?????????
    // 127.0.0.1:8081/COCO/api/member/findId.json
    @PostMapping(value="/findId.json")
    public Map<String, Object> fingIdPOST(
        @RequestBody Member member) {
        // System.out.println(member.toString());
        Map<String, Object> map = new HashMap<>();
        try {
                String userid = memberService.findMemberid(member);
                map.put("result", userid);
                map.put("status", 200);
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 127.0.0.1:8081/COCO/api/member/mailConfirm
    @PostMapping(value =  "/mailConfirm")
    public Map<String, Object> mailConfirm(
        @RequestBody EmailAuthRequestDto emailDto) throws MessagingException, UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        try {
                Member member = new Member();
                member.setEmail(emailDto.getEmail());
                map.put("status", 0);
                if(memberService.findMemberid(member) != null){
                    if(memberService.findMemberid(member).equals(emailDto.getId())){
                        Member upMember = memberService.selectOneMember(emailDto.getId());
                        String authCode = emailService.sendEmail(emailDto.getEmail());

                        upMember.setPw(passwordEncoder.encode(authCode));
                        int ret = memberService.upsertMember(upMember);
                        map.put("status", 200);
                        map.put("result", ret);
                    }           
                }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
}
