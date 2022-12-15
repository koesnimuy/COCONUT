package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.CustomCours;
import com.example.dto.LectureList;
import com.example.entity.Chapter;
import com.example.entity.Cours;
import com.example.entity.CoursImage;
import com.example.entity.InterestCategori;
import com.example.entity.Lecture;
import com.example.entity.Member;
import com.example.entity.Teacher;
import com.example.jwt.JwtUtil;
import com.example.mapper.CoursMapper;
import com.example.repository.CategoriRepository;
import com.example.repository.ChapterRepository;
import com.example.repository.CoursImageREpository;
import com.example.repository.CoursRepository;
import com.example.repository.LectureRepository;
import com.example.repository.MemberRepository;
import com.example.repository.ReviewRepository;
import com.example.repository.teacher.TeacherRepository;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping(value = "/api/cours")
@RequiredArgsConstructor
public class CoursRestController {

     final ResourceLoader resourceLoader;
    final CoursRepository coursRepository;
    final JwtUtil jwtUtil;
    final MemberRepository memberRepository;
    final TeacherRepository teacherRepository;
    final CoursMapper cMapper;
    final ReviewRepository reviewRepository;
    final CategoriRepository categoriRepository;
    final CoursImageREpository coursimageREpository;
    final ChapterRepository chapterRepository;
    final LectureRepository lectureRepository;

    //강좌만들기
    @PostMapping(value="/make.json")
    public Map<String,Object> CoursMakingPOST(@ModelAttribute Cours cours,
    @RequestParam(name = "name") String[] name,    //
    @RequestHeader(name = "TOKEN") String token,
    @RequestParam(name = "file") MultipartFile file) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
        String username = jwtUtil.extractUsername(token);
        Member member = memberRepository.findById(username).orElse(null);
        if (member != null) {
            Teacher teacher = teacherRepository.findByMember(member);
            cours.setTeachercode(teacher);
            Cours cours1 = coursRepository.save(cours);
                    
            //카테고리여러개받기
                    List<InterestCategori> list1 = new ArrayList<>();
                    for (int j = 0; j < name.length; j++) {
                        InterestCategori category = new InterestCategori();
                        category.setName(name[j]);
                        category.setCourscode(cours1);
                        list1.add(category);
                    }
                    categoriRepository.saveAll(list1);
                    //이미지
                    CoursImage image = new CoursImage();
                    image.setImagetype(file.getContentType());
                    image.setImagename(file.getOriginalFilename());
                    image.setImagesize(file.getSize());
                    image.setImagedata(file.getBytes());
                    image.setCours(cours1);
                    image.setRepno(1);

                    coursimageREpository.save(image);
                    
                    map.put("status", 200);
            }
            
        
        } catch (Exception e) {
        e.printStackTrace();
        map.put("status", -1);
        }
        return map;
    }


    //받을거 페이지, 검색어, 정렬순서, 카테고리배열로 , 카테고리에잇는 번호
    @GetMapping(value="/findcours.json")
    public Map<String,Object> CoursFindGET(
        @RequestParam(name = "page", defaultValue = "1", required = false)int page, //페이지
        @RequestParam(name = "text", required = false)String text, //검색
        @RequestParam(name = "row", defaultValue = "1", required = false)int row,// 정렬
        @RequestParam(name = "opt", defaultValue = "1", required = false)int opt, //인기순이나 가격순그런거
        @RequestParam(name = "name" , required = false, defaultValue = "null")String[] name,
        @RequestParam(name = "diff", required = false)String diff,
        @RequestParam(name = "inst", required = false)String inst ) { 
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> map1 = new HashMap<>();
        map.put("status", 0);
        String top = "DESC";
        String option = "coursno";

        if (row == 2) {
            top = "ASC";
        }

        int start = 1;
        if (page > 1) {
           start = 12*(page-1)+1; 
        }
        

        if (opt == 2) {
            option = "ratingavg";
        }
        else if (opt == 3) {
            option = "price";
        }
        else if (opt == 4) {
            option = "reviewtotal";
        }
        else if (opt == 5) {
            option = "ordertotal";
        }


        int size = name.length;
        System.out.println(size + " size @@@@@@@");
        List<Long> category = new ArrayList<>();

            System.out.println("123");
            if (size == 1 && !name[0].equals("null")) {
                System.out.println(name[0].toString());
                List<InterestCategori> categori = categoriRepository.findByName(name[0]);
                for (int i = 0; i < categori.size(); i++) {
                    Long no = categori.get(i).getCourscode().getCoursno();
                    System.out.println(no.toString());
                    category.add(no);
                }
                if (category.isEmpty()) {
                    return map;
                } 
                map1.put("category", category);
            }
            else if (size > 1) { 
                System.out.println("1111");   
                List<Long> fin = new ArrayList<>();

                for (int i = 0; i < name.length; i++) {
                    List<InterestCategori> categori = categoriRepository.findByName(name[i]);
                    // System.out.println(categori.toString());
                    for (int j = 0; j < categori.size(); j++) {
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                        System.out.println(categori.get(j).getCourscode().getCoursno());
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                        Long no = categori.get(j).getCourscode().getCoursno();
                        // System.out.println(no.toString());
                        fin.add(no);
                    }        
                }
                // for (int i = 0; i < fin.size(); i++) {
                //     int compare = Collections.frequency(fin, fin.get(i));
                //     if (compare == size) {
                //         System.out.println(fin.get(i));
                //         category.add(fin.get(i)); 이게 중복 갯수 를 새서 교집합느낌으로다가 해주기?
                //     }
                // }
                HashSet<Long> hashSet = new HashSet<>();
                for(Long no : fin){
                    hashSet.add(no);
                }
                System.out.println(hashSet.toString());
                map1.put("category", hashSet);
            
         
            
        }

       
        map1.put("status", "개강"); //상태가 무엇일떄 보여줄지
        map1.put("top", top);
        map1.put("start", start);
        map1.put("text", text);
        map1.put("option", option);
        if (diff != null) {
            map1.put("diff", diff); //난이도선택
        }
        if (inst != null) {
            map1.put("instrument", inst);
        }
       
        
        System.out.println(top);
        System.out.println(map1.toString());

        
        List<CustomCours> list = cMapper.selectall(map1);
        if (!list.isEmpty()) {
            for (CustomCours customCours : list) { 
                Double avg = (double) Math.round(customCours.getRatingavg()*10);
                Double avg1 = avg/10;
                customCours.setRatingavg(avg1);
                
                CoursImage image = coursimageREpository.findBycours_coursno(customCours.getCoursno());
                Long no = image.getCoursimageno();
                customCours.setImageno(no);

                Cours cours = coursRepository.findByCoursno(customCours.getCoursno());
                Teacher teacher = teacherRepository.findById(cours.getTeachercode().getTeacherno()).orElse(null);
                String name1 = teacher.getMember().getName();
                customCours.setTeachername(name1);

                List<String> names = new ArrayList<>();
                List<InterestCategori> list1 = categoriRepository.findByCourscode_coursno(customCours.getCoursno());
                for (InterestCategori cate : list1) {
                    names.add(cate.getName()); 
                } 
                customCours.setName(names);

            }
        // System.out.println(list.toString());
            map.put("courslist", list);
            map.put("status", 200);
        }
        int total = cMapper.countselect(map1);
        map.put("total", total);
        System.out.println(total);
        return map;
    }
    

    @GetMapping(value="/selectone.json")
    public Map<String,Object> coursselectoneGET(@RequestParam(name = "coursno")Long coursno) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Cours cours = coursRepository.findById(coursno).orElse(null);
            map.put("cours", cours);
            if (cours != null) {
                map.put("teachername", cours.getTeachercode().getMember().getName());
                map.put("teachercode", cours.getTeachercode().getTeacherno());
                // 강좌이미지
                CoursImage image = coursimageREpository.findBycours_coursno(coursno);
                if (image != null) {
                    Long imageno = image.getCoursimageno();
                    map.put("coursimage", imageno);
                }

                //카테고리검색
                List<InterestCategori> list1 = categoriRepository.findByCourscode_coursno(cours.getCoursno());
                // System.out.println(list.toString());
                if (!list1.isEmpty()) {
                    map.put("categorylist", list1);
                }

                //챕터조회
                List<Chapter> list2 = chapterRepository.findByCours_coursno(coursno);
                if (!list2.isEmpty()) {
                    map.put("chapterlist", list2);
                    List<LectureList> list4 = new ArrayList<>();
                    for (int i = 0; i < list2.size(); i++) {
                        Long chapterno = list2.get(i).getNo();
                        List<Lecture> list3 = lectureRepository.findByChapter_noOrderByFreeAsc(chapterno);
                        LectureList lectureList = new LectureList();
                        lectureList.setChapterno(chapterno);
                        lectureList.setLecturelist(list3);
                        list4.add(lectureList);
                        map.put( chapterno + "" , list3);
                    }
                    // map.put("lecturelist", list4);
                    
                }
                
                map.put("status", 200);
            }
     
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    //<img src="/image?no=1" >
    // 127.0.0.1:8081/COCO/api/cours/image?no=1
    @GetMapping(value="/image")
    public ResponseEntity<byte[]> imageGET(
        @RequestParam(name="no") Long no) throws IOException{
        
            if(no > 0L) {           
                CoursImage coursImage = coursimageREpository.findById(no).orElse(null);
                if(coursImage.getImagesize() > 0L) {
                    // 타입설정 png인지 jpg인지 gif인지
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(
                        MediaType.parseMediaType( coursImage.getImagetype() ) );
                    // 실제이미지데이터, 타입이포함된 header, status 200    
                    ResponseEntity<byte[]> response 
                        = new ResponseEntity<>(
                            coursImage.getImagedata(), headers, HttpStatus.OK);
                    return response;        
                }
                else {
                    InputStream is = resourceLoader.getResource("classpath:/static/image/default.jpg")
                        .getInputStream();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    // 실제이미지데이터, 타입이포함된 header, status 200    
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
                // 실제이미지데이터, 타입이포함된 header, status 200    
                ResponseEntity<byte[]> response 
                    = new ResponseEntity<>(
                        is.readAllBytes(), headers, HttpStatus.OK);
                return response;
            }
    }


    // status변경용도
    @PutMapping(value="/updatestatus.json")
    public Map<String,Object> coursupdatestatusPUT(@RequestBody Cours cours) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Cours dbcours = coursRepository.findById(cours.getCoursno()).orElse(null);
            if (dbcours != null) {
                dbcours.setStatus(cours.getStatus());
                coursRepository.save(dbcours);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @PutMapping(value = "/updateall.json")
    public Map<String,Object> coursupdatePUT(@RequestBody Cours cours) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Cours dbcours = coursRepository.findById(cours.getCoursno()).orElse(null);
            if (dbcours != null) {
                dbcours.setStatus(cours.getStatus());
                dbcours.setDifficult(cours.getDifficult());
                dbcours.setIntro(cours.getIntro());
                dbcours.setPeriod(cours.getPeriod());
                dbcours.setPrice(cours.getPrice());
                dbcours.setTitle(cours.getTitle());
                coursRepository.save(dbcours);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    //카테고리관리
    @PutMapping(value="/categori.json")
    public Map<String,Object> catePUT(@RequestBody InterestCategori category) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Cours cours = coursRepository.findByCoursno(category.getCoursno());
            if (cours != null) {
                categoriRepository.save(category);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        
        return map;
    }
    
    @PutMapping(value="updateimage.json") 
    public Map<String,Object> updateCoursImagePUT(@RequestParam(name = "coursno")Long coursno,
    @RequestParam(name = "file")MultipartFile file) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            CoursImage image = coursimageREpository.findBycours_coursno(coursno);
            if (image != null) {
                image.setImagedata(file.getBytes());
                image.setImagename(file.getOriginalFilename());
                image.setImagesize(file.getSize());
                image.setImagetype(file.getContentType());
                coursimageREpository.save(image);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }


    
}
