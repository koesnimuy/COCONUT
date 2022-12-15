package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Chapter;
import com.example.entity.Lecture;
import com.example.entity.LectureFile;
import com.example.repository.ChapterRepository;
import com.example.repository.LectureFileRepository;
import com.example.repository.LectureRepository;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping(value = "/api/lecture")
@RequiredArgsConstructor
public class LectureRestController {
    
    final ResourceLoader resourceLoader;
    final LectureRepository lectureRepository;
    final LectureFileRepository FileRepository;
    final ChapterRepository chapterRepository;

    @PostMapping(value="/insert.json")
    public Map<String,Object> chapterPOST(@ModelAttribute Lecture lecture,
    @RequestParam(name = "file")MultipartFile file,
    @RequestParam(name = "chapterno")Long chapterno) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Chapter chapter = chapterRepository.findById(chapterno).orElse(null);
            if (chapter != null) {
                lecture.setChapter(chapter);
                Lecture lecture1 = lectureRepository.save(lecture);

                if (file.getSize() > 0) {
                        LectureFile file2 = new LectureFile();
                        file2.setFiledata(file.getBytes());
                        file2.setFilename(file.getOriginalFilename());
                        file2.setFilesize(file.getSize());
                        file2.setFiletype(file.getContentType());
                        file2.setLecture(lecture1);
                        String[] type = file2.getFiletype().split("/");
                        file2.setType(type[0]);
                        
                        FileRepository.save(file2);
                }
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }

        return map;
    }


    @GetMapping(value="/selectone.json")
    public Map<String,Object> selectonelecturePOST(@RequestParam(name = "lectureno")Long lectureno) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
           Lecture lecture = lectureRepository.findById(lectureno).orElse(null);
           if (lecture != null) {
            Long videono = 0L;
            LectureFile file = FileRepository.findByLecture_no(lectureno);
             if (file.getType().equals("video") ) {
                    videono = file.getNo();
                }
            map.put("status", 200);
            map.put("lecture", lecture);
            map.put("videono", videono); //0이면 없는거
           }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }

        return map;
    }
    
    @PutMapping(value="/update.json")
    public  Map<String,Object> updatelecturePUT(@RequestBody Lecture lecture) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try { 
        Lecture lecture1 = lectureRepository.findById(lecture.getNo()).orElse(null);
            if (lecture1 != null) {
                lecture1.setTitle(lecture.getTitle());
                lecture1.setContent(lecture.getContent());
                lecture1.setFree(lecture.getFree());
                lectureRepository.save(lecture1);
                map.put("status", 200);
            }
        } catch (Exception e) {
           e.printStackTrace();
           map.put("status", -1);
        }
        return map;
    }

    @PostMapping(value = "delete.json")
    public  Map<String,Object> deletelectureDELETE(@RequestParam(name = "no")Long no ) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try { 
            Lecture lecture1 = lectureRepository.findById(no).orElse(null);
                if (lecture1 != null) {
                    LectureFile file = FileRepository.findByLecture_no(lecture1.getNo());
                    if (file != null) {
                        FileRepository.delete(file);
                    }
                    lectureRepository.delete(lecture1);
                    map.put("status", 200);
                }
            } catch (Exception e) {
               e.printStackTrace();
               map.put("status", -1);
            }
            return map;
    }


    @PutMapping(value="/updatefile.json")
    public  Map<String,Object> updatefilePUT(@RequestParam(name = "file")MultipartFile file,
        @ModelAttribute Lecture lecture ) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Lecture lecture1 = lectureRepository.findById(lecture.getNo()).orElse(null);
            if (lecture1 != null) {
               LectureFile file1 = FileRepository.findByLecture_no(lecture1.getNo());
               if (file1 != null) {
                file1.setFiledata(file.getBytes());
                file1.setFilename(file.getOriginalFilename());
                file1.setFilesize(file.getSize());
                file1.setFiletype(file.getContentType());
                file1.setLecture(lecture1);
                FileRepository.save(file1);
                map.put("status", 200);
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 127.0.0.1:8080/ROOT/api/lectuer/video?no=1
    @GetMapping(value = "/video")
    public ResponseEntity<byte[]> selectOneImageGET(
            @RequestParam(name="no") Long no ) throws IOException{
        LectureFile video = FileRepository.findById(no).orElse(null);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> response = null;
        if (no > 0) {
            if(video.getFilesize() > 0L) { // DB에 파일존재O
                headers.setContentType(MediaType.parseMediaType(video.getFiletype()));
                response = new ResponseEntity<>(video.getFiledata(), headers, HttpStatus.OK);
            }
            else {
                InputStream stream = resourceLoader.getResource("classpath:/static/image/default.jpg").getInputStream();
                headers.setContentType(MediaType.IMAGE_PNG);
                response = new ResponseEntity<>(stream.readAllBytes(), 
                    headers, HttpStatus.OK);
            }
        }
        else{
            InputStream stream = resourceLoader.getResource("classpath:/static/image/default.jpg").getInputStream();
                headers.setContentType(MediaType.IMAGE_PNG);
                response = new ResponseEntity<>(stream.readAllBytes(), 
                    headers, HttpStatus.OK);
        }
        return response;
    }

}
