package com.example.backend;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.service.MemberImageService;

@SpringBootTest
class BackendApplicationTests {

	@Autowired MemberImageService memberImageService;

	@Test
	void countimageGET() {
	
	}

}
