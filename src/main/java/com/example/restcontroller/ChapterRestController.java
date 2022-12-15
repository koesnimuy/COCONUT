package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Chapter;
import com.example.entity.Cours;
import com.example.entity.Lecture;
import com.example.entity.LectureFile;
import com.example.repository.ChapterRepository;
import com.example.repository.CoursRepository;
import com.example.repository.LectureFileRepository;
import com.example.repository.LectureRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/api/chapter")
@RequiredArgsConstructor
public class ChapterRestController {

    final ChapterRepository chapterRepository;
    final CoursRepository coursRepository;
    final LectureRepository lectureRepository;
    final LectureFileRepository fileRepository;

    
    @PostMapping(value="/insert.json")
    public Map<String,Object> chapterPOST(
        @RequestBody Chapter chapter) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            System.out.println(chapter.toString());
            Cours cours = coursRepository.findById(chapter.getCoursno()).orElse(null);
            if (cours != null) {
                chapter.setCours(cours);
                chapterRepository.save(chapter);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }

        return map;
    }

    @PutMapping(value="/update.json")
    public Map<String,Object> chapterupdatePUT(
        @RequestBody Chapter chapter) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            System.out.println(chapter.toString());
            Cours cours = coursRepository.findById(chapter.getCoursno()).orElse(null);
            if (cours != null) {
                chapter.setCours(cours);
                chapterRepository.save(chapter);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }

        return map;
    }

    @DeleteMapping(value = "/delete.json")
    public Map<String,Object> deletechapterDELETE(@RequestBody Chapter chapter){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            System.out.println(chapter.toString());
            Chapter chapter1 = chapterRepository.findById(chapter.getNo()).orElse(null);
            System.out.println(chapter1.toString());
            if (chapter1 != null) {
                List<Lecture> list = 
                lectureRepository.findByChapter_noOrderByFreeAsc(chapter1.getNo());
                if (!list.isEmpty()) {
                    for (Lecture lecture : list) {
                        LectureFile file = fileRepository.findByLecture_no(lecture.getNo());
                        if (file != null) {
                            fileRepository.delete(file);
                        }
                    }
                    lectureRepository.deleteAll(list);
                }
                chapterRepository.delete(chapter1);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }
    
}
