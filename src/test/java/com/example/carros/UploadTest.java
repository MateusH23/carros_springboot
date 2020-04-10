package com.example.carros;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.carros.api.upload.UploadInput;
import com.example.carros.api.upload.UploadOutput;
import com.example.carros.domain.upload.FirebaseStorageService;

@SpringBootTest(classes = CarrosApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class UploadTest {
	
	@Autowired
	protected TestRestTemplate rest;
	
	@Autowired
	private FirebaseStorageService service;
	
	private TestRestTemplate basicAuth() {
		return rest.withBasicAuth("admin", "123");
	}
	
	private UploadInput getUploadInput() {
		UploadInput upload = new UploadInput();
		upload.setFilename("nome.txt");
		//Base64 de Mateus Hernani
		upload.setBase64("TWF0ZXVzIEhlcm5hbmk=");
		upload.setMimeType("text/plain");
		return upload;
	}
	
	@Test
	public void testUploadFirebase() {
		String url = service.upload(getUploadInput());
		
		//Faz o get na URL
		ResponseEntity<String> urlResponse = basicAuth().getForEntity(url, String.class);
		System.out.println(urlResponse);
		assertEquals(HttpStatus.OK, urlResponse.getStatusCode());
	}
	
	@Test
	public void testUploadAPI() {
		UploadInput upload = getUploadInput();
		
		//Insert
		ResponseEntity<UploadOutput> response = basicAuth().postForEntity("/api/v1/upload", upload, UploadOutput.class);
		System.out.println(response);
		
		//verifica se criou
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		UploadOutput out = response.getBody();
		assertNotNull(out);
		System.out.println(out);
		
		String url = out.getUrl();
		
		//Faz o GET na url
		ResponseEntity<String> urlResponse = basicAuth().getForEntity(url, String.class);
		System.out.println(urlResponse);
		assertEquals(HttpStatus.OK, urlResponse.getStatusCode());
	}

}
