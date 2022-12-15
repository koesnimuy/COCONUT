package com.example.restcontroller.board;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.TestBoard;
import com.example.entity.board.BoardImage;
import com.example.repository.board.BoardImageRepository;
import com.example.repository.board.TestBoardRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/testboard")
@RequiredArgsConstructor
public class TestBoardRestController {
    
    final TestBoardRepository boardRepository;
    final BoardImageRepository boardImageRepository;
    final ResourceLoader resourceLoader;

    // 127.0.0.1:8081/COCO/api/testboard/write.json
    @PostMapping(value="/write.json")
    public Map<String, Object> boardwrite(
        @RequestBody TestBoard board) {
        Map<String, Object> map = new HashMap<>();
        System.out.println(board.toString());
        try {
            board.setBoardno(1L);
            boardRepository.save(board);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 게시물 1개 조회 GET
    // 127.0.0.1:8081/COCO/api/testboard/selectOne.json?no=1
    @GetMapping(value="/selectOne.json")
    public Map<String, Object> selectOne(
        @RequestParam(name = "no")Long no ) {
        Map<String, Object> retMap = new HashMap<>();
        
        try {
            retMap.put("result", boardRepository.findById(no));
            retMap.put("status", 200);    
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("status", 0);    
        }

        return retMap;
    }

    // 127.0.0.1:8081/COCO/api/testboard/image?no=1
    @GetMapping(value="/image")
    public ResponseEntity<byte[]> imageGET(
        @RequestParam(name="no") Long no) throws IOException{
        
            if(no > 0L) {           
                BoardImage boardImage = boardImageRepository.findById(no).orElse(null);
                if(boardImage.getImagesize() > 0L) {
                    // 타입설정 png인지 jpg인지 gif인지
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(
                        MediaType.parseMediaType( boardImage.getImagetype() ) );
                    // 실제이미지데이터, 타입이포함된 header, status 200    
                    ResponseEntity<byte[]> response 
                        = new ResponseEntity<>(
                            boardImage.getImagedata(), headers, HttpStatus.OK);
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

    // 127.0.0.1:8081/COCO/api/testboard/insertimg.json
    @PostMapping(value = "/insertimg.json")
    public Map<String, Object> insertPOST(
        @ModelAttribute BoardImage image,
        @RequestParam(name = "file") MultipartFile file) throws IOException{
            System.out.println(image.toString());
            System.out.println(file.getOriginalFilename());

            Map<String, Object> map = new HashMap<>();
            try {
                image.setImagedata(file.getBytes());
                image.setImagename(file.getOriginalFilename());
                image.setImagetype(file.getContentType());
                image.setImagesize(file.getSize());

                boardImageRepository.save(image);

                String url = "http://3.35.91.60:8080/COCO/api/testboard/image?no="+image.getNo();

                map.put("status", 200);
                map.put("result", url);
            } catch (Exception e) {
                map.put("status", -1);
                map.put("result", e.getMessage());
            }
            return map;
        }
}
