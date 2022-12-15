package com.example.restcontroller.teacher;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.TeacherImageList;
import com.example.entity.Cours;
import com.example.entity.Education;
import com.example.entity.Link;
import com.example.entity.Member;
import com.example.entity.MemberImage;
import com.example.entity.Role;
import com.example.entity.Teacher;
import com.example.entity.TeacherImage;
import com.example.jwt.JwtUtil;
import com.example.repository.CoursRepository;
import com.example.repository.MemberImageRepository;
import com.example.repository.MemberRepository;
import com.example.repository.RoleRepository;
import com.example.repository.teacher.EducationRepository;
import com.example.repository.teacher.LinkRepository;
import com.example.repository.teacher.TeacherImageRepository;
import com.example.repository.teacher.TeacherRepository;
import com.example.service.MemberService;
import com.example.service.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/teacher")
@RequiredArgsConstructor
public class TeacherRestController {

    final ResourceLoader resourceLoader;
    final TeacherRepository teacherRepository;
    final EducationRepository educationRepository;
    final LinkRepository linkRepository;
    final TeacherImageRepository teacherImageRepository;
    final RoleRepository roleRepository;
    final MemberRepository memberRepository;
    final JwtUtil jwtUtil;
    final AuthenticationManager authenticationManager;
    final MemberService memberService;
    final TokenService tokenService;
    final CoursRepository coursRepository;
    final MemberImageRepository memberImageRepository;
    // ---------------------------------------강사신청---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/jointeacher.json
    @PostMapping(value = "/jointeacher.json")
    public Map<String, Object> jointestPOST(
            @RequestBody Teacher teacher, 
            @RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
            try {
                String username = jwtUtil.extractUsername(token);
                Member member = memberRepository.findById(username).orElse(null);
                member.setId(username);

                teacher.setMember(member);
                Teacher teacher1 = teacherRepository.save(teacher);
                map.put("status", 200);
                map.put("teacher", teacher1);
            }
            catch(Exception e) {
                map.put("status", -1);
                map.put("result", e.getMessage());
            }
        return map;
        }

    // ---------------------------------------강사업데이트---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/updateteacher.json
    @PostMapping(value="/updateteacher.json")
    public Map<String, Object> updateteacherPOST(@RequestBody Teacher teacher, @RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰 아이디 가져오기
            String username = jwtUtil.extractUsername(token);
            Member member1 = memberRepository.findById(username).orElse(null); // 기존꺼 유효
            Long teacherno = teacherRepository.findByMember(member1).getTeacherno();
            Teacher teacher1 = teacherRepository.findById(teacherno).orElse(null); // 강사 정보 추출
            
            // 토큰 아이디와 아이디가 일치하지 않은지 확인함
            if(member1.getId() != teacher1.getMember().getId()){
                map.put("status", 0);
            }
            else{
                // 기존 번호 가져오기
                teacher1.setCategory(teacher.getCategory());
                teacher1.setIntro(teacher.getIntro());
                teacher1.setSteps(teacher.getSteps());
                teacherRepository.save(teacher1);
    
                map.put("status", 200);
                map.put("teacher", teacher1);
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------강사1명조회---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/selectOneteacher.json
    @GetMapping(value="/selectOneteacher.json")
    public Map<String, Object> selectOneteacherGET(@RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null); // 기존꺼 유효
            Long selectno = teacherRepository.findByMember(member).getTeacherno();
            System.out.println(selectno);
            Teacher teacher1 = teacherRepository.findById(selectno).orElse(null); // 강사 정보 추출
            
            // 토큰 아이디와 아이디가 일치하지 않은지 확인함
            if(member.getId() != teacher1.getMember().getId()){
                map.put("status", 0);
            }
            else{
                // 기존 번호 가져오기
                Teacher teacher = teacherRepository.findById(selectno).orElse(null);
                map.put("status", 200);
                map.put("result", teacher);
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------강사1명조회---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/selectteacher.json
    @GetMapping(value="/selectteacher.json")
    public Map<String, Object> selectteacherGET(@RequestParam(name = "teachercode") Long teachercode) {
        Map<String, Object> map = new HashMap<>();
        try {
            Teacher teacher = teacherRepository.findById(teachercode).orElse(null); // 강사 정보 추출
            map.put("status", 200);
            map.put("teacher", teacher.getMember());
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------강사페이지조회---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/selectListteacher.json
    @GetMapping(value="/selectListteacher.json")
    public Map<String, Object> selectListteacherGET(@RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null); // 기존꺼 유효
            Long selectno = teacherRepository.findByMember(member).getTeacherno();
            System.out.println(selectno);
            Teacher teacher1 = teacherRepository.findById(selectno).orElse(null); // 강사 정보 추출
            List<Education> educations = educationRepository.findByTeacher(teacher1); // 학력 추출
            List<Link> links = linkRepository.findByTeacher(teacher1); // 링크정보 추출
            List<TeacherImage> images = teacherImageRepository.findByTeacher(teacher1); // 이미지정보 추출
            
            // 토큰 아이디와 아이디가 일치하지 않은지 확인함
            if(member.getId() != teacher1.getMember().getId()){
                map.put("status", 0);
            }
            else{
                // 기존 번호 가져오기
                Teacher teacher = teacherRepository.findById(selectno).orElse(null);
                map.put("status", 200);
                map.put("teacher", teacher);
                map.put("education", educations);
                map.put("link", links);
                map.put("image", images);
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------학력저장---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/joineducation.json
    @PostMapping(value="/joineducation.json")
    public Map<String, Object> joineducationPOST(@RequestBody Education education, @RequestHeader(name = "TOKEN") String token) {
        System.out.println(education.toString());
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token); // 토큰 아이디 추출
            Member member = memberRepository.findById(username).orElse(null); // 기존꺼 유효
            long no = teacherRepository.findByMember(member).getTeacherno();
            Teacher teacher1 = teacherRepository.findById(no).orElse(null); // 강사 정보 추출
            System.out.println("aa" + educationRepository.findById(no).orElse(null));

            // 토큰이 없으면 등록 불가
            if(jwtUtil.extractUsername(token) != null){
                map.put("status", -1);
            }
            // 토큰 아이디와 아이디가 일치하지 않은지 확인함
            if(member.getId() != teacher1.getMember().getId()){
                map.put("status", 0);
            }
            
            // 학력이 3개 넘으면  등록 불가
            // else if(teacherRepository.findById(no).orElse(null).getEducations().size() >= 3){
            //     map.put("status", 0);
            // }
            else{
                // 저장값
                Teacher teacher = teacherRepository.findById(no).orElse(null);
                education.setTeacher(teacher);
                educationRepository.save(education);
                map.put("status", 200);
                map.put("education", education.getTeacher());
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // ---------------------------------------학력수정---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/updateeducation.json
    @PostMapping(value="/updateeducation.json")
    public Map<String, Object> updateeducationPOST(@RequestBody Education education, @RequestHeader(name = "TOKEN") String token,
        @RequestParam(name = "educationno") Long educationno) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null);
            Long teacherno = teacherRepository.findByMember(member).getTeacherno();
            Teacher teacher = teacherRepository.findById(teacherno).orElse(null); // 강사 정보
            Education education1 = educationRepository.findById(educationno).orElse(null); // 교육 새로 저장
            
            // 토큰이 없으면 등록 불가
            if(jwtUtil.extractUsername(token) == null){
                map.put("status", -1);
            }
            // 토큰 아이디와 강사 아이디(번호) 일치하지 않으면 불가능
            else if(member.getId() != teacher.getMember().getId()){
                map.put("status", 0);
            }
            // 강사 번호와 학력 번호별 강사번호가 일치하지 않으면 불가능
            else if(teacher.getTeacherno() != education1.getTeacher().getTeacherno()){
                map.put("status", 0);
            }
            else{
                education1.setDepartment(education.getDepartment());
                education1.setName(education.getName());
                education1.setStatus(education.getStatus());
                educationRepository.save(education1);
                map.put("status", 200);
                map.put("education", education1);
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // ---------------------------------------학력삭제---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/deleteeducation.json
    @PostMapping(value="/deleteeducation.json")
    public Map<String, Object> deleteeducationPOST(@RequestHeader(name = "TOKEN") String token,
        @RequestParam(name = "educationno") Long educationno) {
        Map<String, Object> map = new HashMap<>();
        try {
            // String username = jwtUtil.extractUsername(token);
            // Member member = memberRepository.findById(username).orElse(null);
            // Long teacherno = teacherRepository.findByMember(member).getTeacherno();
            // Teacher teacher = teacherRepository.findById(teacherno).orElse(null); // 강사 정보
            // List<Education> educations = educationRepository.findByTeacher(teacher); // 강사의 학력 내용
            // Long educationno = educations.get(0).getEducationno();
            // System.out.println(educationno);
            educationRepository.deleteById(educationno);
            map.put("status", 200);
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // ---------------------------------------학력1개조회---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/selectOneeducation.json
    @GetMapping(value="/selectOneeducation.json")
    public Map<String, Object> selectOneeducationGET(@RequestHeader(name = "TOKEN") String token, 
        @RequestParam(name = "educationno") Long educationno) {
        Map<String, Object> map = new HashMap<>();
        try {
            Education education = educationRepository.findById(educationno).orElse(null); // 번호별 조회

                map.put("status", 200);
                map.put("education", education);
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // ---------------------------------------학력전체조회---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/selectListeducation.json
    @GetMapping(value="/selectListeducation.json")
    public Map<String, Object> selectListeducationGET(@RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null); // 기존꺼 유효
            Long selectno = teacherRepository.findByMember(member).getTeacherno();
            System.out.println(selectno);
            Teacher teacher1 = teacherRepository.findById(selectno).orElse(null); // 강사 정보 추출
            List<Education> education = educationRepository.findByTeacher(teacher1); // 학력 추출
            
            // 토큰 아이디와 아이디가 일치하지 않은지 확인함
            if(member.getId() != teacher1.getMember().getId()){
                map.put("status", 0);
            }
            else{
                map.put("status", 200);
                map.put("education", education);
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------외부링크저장---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/joinlink.json
    @PostMapping(value="/joinlink.json")
    public Map<String, Object> joinlinkPOST(@RequestBody Link link,@RequestHeader(name = "TOKEN") String token) {
        System.out.println(link.toString());
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null);
            // 토큰이 없으면 등록 불가
            if(jwtUtil.extractUsername(token) == null){
                map.put("status", -1);
            }
            // 3개 이상 저장 불가
            Long teacherno = teacherRepository.findByMember(member).getTeacherno();
            // long ret = teacherRepository.findById(teacherno).orElse(null).getLinks().size();
            // if(ret >= 3){
            //     map.put("status", 0);
            // }
                // 저장
                Teacher teacher = teacherRepository.findById(teacherno).orElse(null);
                link.setTeacher(teacher);
                link = linkRepository.save(link);
                map.put("status", 200);
                map.put("link", link);
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------링크수정---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/updatelink.json
    @PostMapping(value="/updatelink.json")
    public Map<String, Object> updatelinkPOST(@RequestBody Link link, @RequestHeader(name = "TOKEN") String token,
        @RequestParam(name = "linkno") long linkno) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null);
            Long teacherno = teacherRepository.findByMember(member).getTeacherno();
            Teacher teacher = teacherRepository.findById(teacherno).orElse(null); // 강사 정보
            Link link2 = linkRepository.findById(linkno).orElse(null);

            // 토큰이 없으면 등록 불가
            if(jwtUtil.extractUsername(token) == null){
                map.put("status", -1);
            }
            // 토큰 아이디와 강사 아이디(번호) 일치하지 않으면 불가능
            else if(member.getId() != teacher.getMember().getId()){
                map.put("status", 0);
            }
            // 강사 아이디에 링크 내역이 없으면 불가능
            // else if(teacher.getLinks().isEmpty()){
            //     map.put("status", 0);
            // }
            // 강사 번호와 링크 번호별 강사번호가 일치하지 않으면 불가능
            else if(teacherno != link2.getTeacher().getTeacherno()){
                map.put("status", 0);
            }
            else{
                link2.setPlatform(link.getPlatform());
                link2.setUrl(link.getUrl());
                linkRepository.save(link2);
                map.put("status", 200);
                map.put("link", link2);
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------링크전체조회---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/selectListlink.json
    @GetMapping(value="/selectListlink.json")
    public Map<String, Object> selectListlinkGET(@RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null); // 기존꺼 유효
            Long selectno = teacherRepository.findByMember(member).getTeacherno();
            Teacher teacher1 = teacherRepository.findById(selectno).orElse(null); // 강사 정보 추출7
            List<Link> link = linkRepository.findByTeacher(teacher1); // 링크 추출
            
            // 토큰 아이디와 아이디가 일치하지 않은지 확인함
            if(member.getId() != teacher1.getMember().getId()){
                map.put("status", 0);
            }
            else{
                map.put("status", 200);
                map.put("link", link);
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------링크삭제---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/deletelink.json
    @PostMapping(value="/deletelink.json")
    public Map<String, Object> deleteelinkPOST(@RequestHeader(name = "TOKEN") String token,
        @RequestParam(name = "linkno") Long linkno) {
        Map<String, Object> map = new HashMap<>();
        try {
            linkRepository.deleteById(linkno);
            map.put("status", 200);
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // ---------------------------------------이미지여러개저장---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/joinimagebatch.json
    @PostMapping(value = "/joinimagebatch.json")
    public Map<String, Object> joinimagebatchPOST(@RequestParam(name = "file") MultipartFile[] file,
    @ModelAttribute TeacherImageList list, @RequestHeader(name = "TOKEN") String token) throws IOException{
        Map<String, Object> map = new HashMap<>();
        try{
            for(int i=0; i<list.getList().size(); i++){
                    TeacherImage image = list.getList().get(i);
                    image.setImagename(file[i].getOriginalFilename());
                    image.setImagedata(file[i].getBytes());
                    image.setImagesize(file[i].getSize());
                    image.setImagetype(file[i].getContentType());
            }
            List<TeacherImage> images = teacherImageRepository.saveAll(list.getList());
            map.put("status", 200);
            map.put("image", images);
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // ---------------------------------------이미지저장---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/joinimage.json
    @PostMapping(value = "/joinimage.json")
    public Map<String, Object> joinimagePOST(@RequestParam(name = "file") MultipartFile file,
    @ModelAttribute TeacherImage list, @RequestHeader(name = "TOKEN") String token) throws IOException{
        Map<String, Object> map = new HashMap<>();
        try{
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null); // 기존꺼 유효
            Long selectno = teacherRepository.findByMember(member).getTeacherno();
            Teacher teacher = teacherRepository.findById(selectno).orElse(null);
            teacher.setTeacherno(selectno);

            list.setImagename(file.getOriginalFilename());
            list.setImagedata(file.getBytes());
            list.setImagesize(file.getSize());
            list.setImagetype(file.getContentType());
            list.setTeacher(teacher);

            teacherImageRepository.save(list);
            map.put("status", 200);
            map.put("image", list);
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------이미지조회---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/selectimage.json
    @GetMapping(value = "/selectimage.json")
    public Map<String, Object> selectimagePOST(@ModelAttribute TeacherImage list, @RequestHeader(name = "TOKEN") String token) throws IOException{
        Map<String, Object> map = new HashMap<>();
        try{
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null); // 기존꺼 유효
            Long selectno = teacherRepository.findByMember(member).getTeacherno();
            Teacher teacher1 = teacherRepository.findById(selectno).orElse(null); // 강사 정보 추출
            List<TeacherImage> images = teacherImageRepository.findByTeacher(teacher1); // 이미지 추출
            
            map.put("status", 200);
            map.put("image", images);
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
        
    // ---------------------------------------이미지삭제---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/deleteimage.json
    @PostMapping(value = "/deleteimage.json")
    public Map<String, Object> deleteimagePOST(@RequestParam(name = "teacherimageno") Long teacherimaegno,
        @RequestHeader(name = "TOKEN") String token) throws IOException{
        Map<String, Object> map = new HashMap<>();
        try{
            teacherImageRepository.deleteById(teacherimaegno);
            map.put("status", 200);
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
        
    // ---------------------------------------이미지수정---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/joinimage.json
    @PostMapping(value = "/updateimage.json")
    public Map<String, Object> updateimagePOST(@RequestParam(name = "file") MultipartFile[] file,
    @ModelAttribute TeacherImageList list, @RequestHeader(name = "TOKEN") String token) throws IOException{
        Map<String, Object> map = new HashMap<>();
        try{
            TeacherImageList list2 = new TeacherImageList();
            for(int i=0; i<list2.getList().size(); i++){
                    TeacherImage image = list2.getList().get(i);
                    image.setImagename(file[i].getOriginalFilename());
                    image.setImagedata(file[i].getBytes());
                    image.setImagesize(file[i].getSize());
                    image.setImagetype(file[i].getContentType());
            }
            List<TeacherImage> images = teacherImageRepository.saveAll(list2.getList());
            map.put("status", 200);
            map.put("result", images);
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------권한추가---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/role.json
    @PostMapping(value="/role.json") // 강사신청
    public Map<String, Object> rolePOST(@RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰 아이디 가져오기
            String username = jwtUtil.extractUsername(token);
            Member member1 = memberRepository.findById(username).orElse(null); // 기존꺼 유효
            member1.setId(username);

            // 권한 추가
            Role role = new Role();
            // List<Role> strRole = member1.getRoles();
            List<Role> strRole = roleRepository.findByUserid(member1);
            int check = 0;

            if(member1 != null){
                for(int i = 0; i < strRole.size(); i++) {
                Role role1 = strRole.get(i);
                    if (role1.getName().equals("TEACHER")) {
                        check = 1;
                        map.put("status", 0);
                    }
                }
                if(check == 0){
                    // role
                    role.setName("TEACHER");
                    role.setUserid(member1);
                    roleRepository.save(role);

                    map.put("status", 200);
                    map.put("role", role);
                }
            }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ---------------------------------------이미지조회---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/image.json
    @GetMapping(value = "/image.json")
    public ResponseEntity<byte[]> selectOneImageGET(@RequestParam(name="teacherno") Long teacherno ) throws IOException{
        TeacherImage image = teacherImageRepository.findById(teacherno).orElse(null);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> response = null;
        if(image.getImagesize() > 0L) { // DB에 파일존재O
            headers.setContentType(MediaType.parseMediaType(image.getImagetype()));
            response = new ResponseEntity<>(image.getImagedata(), headers, HttpStatus.OK);
            // image.setImageurl(response.getHeaders() + "/image.json?=" + image.getTeacherimageno());
        }
        else {
            InputStream stream = resourceLoader.getResource("classpath:/static/image/default.jpg").getInputStream();
            headers.setContentType(MediaType.IMAGE_PNG);
            response = new ResponseEntity<>(stream.readAllBytes(),
                headers, HttpStatus.OK);
        }
        return response;
    }

    // ---------------------------------------강사조회---------------------------
    // 127.0.0.1:8081/COCO/api/teacher/selectoneno.json
    @GetMapping(value = "/selectoneno.json")
    public Map<String, Object> selectonenoGET(@RequestParam(name = "teacherno") Long teacherno){
        Map<String, Object> map = new HashMap<>();
        try{
            Teacher teacher = teacherRepository.findById(teacherno).orElse(null);
            List<Education> educations = educationRepository.findByTeacher(teacher);
            List<Link> links = linkRepository.findByTeacher(teacher);
            List<TeacherImage> images = teacherImageRepository.findByTeacher(teacher);
            map.put("status", 200);
            map.put("teacher", teacher);
            map.put("education", educations);
            map.put("link", links);
            map.put("image", images);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 127.0.0.1:8081/COCO/api/teacher/selectcours.json
    @GetMapping(value = "selectcours.json")
    public Map<String, Object> selectCoursGET(@RequestParam(name = "teacherno") Long teacherno) {
        Map<String, Object> map = new HashMap<>();
        try {
            Teacher teacher = teacherRepository.findById(teacherno).orElse(null);
            List<Cours> cours = coursRepository.findByTeachercode(teacher);
            map.put("status", 200);
            map.put("cours", cours);
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 멤버 및 이미지 조회
    // 127.0.0.1:8081/COCO/api/teacher/selectmember.json
    @GetMapping(value = "selectmember.json")
    public Map<String, Object> selectMember(@RequestParam(name = "teacherno") Long teacherno){
        Map<String, Object> map = new HashMap<>();
        try{
            Teacher teacher = teacherRepository.findById(teacherno).orElse(null);
            MemberImage image = memberImageRepository.findByUserid(teacher.getMember().getId());
            map.put("status", 200);
            map.put("teacher", teacher.getMember());
            map.put("image", image);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
}