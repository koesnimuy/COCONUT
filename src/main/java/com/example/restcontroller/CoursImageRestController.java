package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.CoursImage;
import com.example.repository.CoursImageREpository;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping(value = "/api/coursimage")
@RequiredArgsConstructor
public class CoursImageRestController {
    
    final CoursImageREpository imageREpository;

    @GetMapping(value="/search.json")
    public Map<String,Object> searchcours(@RequestParam(name = "coursno")Long coursno){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            CoursImage image = imageREpository.findBycours_coursno(coursno);
            if (image != null) {
                map.put("status", 200);
                map.put("imageno", image.getCoursimageno());
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    

   
}
