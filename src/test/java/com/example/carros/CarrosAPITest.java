package com.example.carros;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.carros.domain.Carro;
import com.example.carros.domain.dto.CarroDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarrosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarrosAPITest {
	
	@Autowired
	protected TestRestTemplate rest;

	private ResponseEntity<CarroDTO> getCarro(String url) {
		return rest.withBasicAuth("user", "123").getForEntity(url, CarroDTO.class);
	}

	private ResponseEntity<List<CarroDTO>> getCarros(String url) {
		return rest.withBasicAuth("user", "123").exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CarroDTO>>() {
		});
	}
	
	@Test
	public void testSave() {
		Carro carro = new Carro();
		carro.setNome("Porsche");
		carro.setTipo("esportivos");
		
		// Insert
		ResponseEntity response = rest.withBasicAuth("user", "123").postForEntity("/api/v1/carros", carro, null);
		System.out.println(response);
		
		// Verifica se criou
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		//Busca o objeto
		String location = response.getHeaders().getLocation().toString();
		CarroDTO c = getCarro(location).getBody();
		
		assertNotNull(c);
		assertEquals("Porsche", c.getNome());
		assertEquals("esportivos", c.getTipo());
		
		// Deletar o objeto
		rest.withBasicAuth("user", "123").delete(location);
		
		// Verifica se deletou
		assertEquals(HttpStatus.NOT_FOUND, getCarro(location).getStatusCode());
	}
	
	@Test
	public void testLista() {
		List<CarroDTO> carros = getCarros("/api/v1/carros").getBody();
		assertNotNull(carros);
		assertEquals(30, carros.size());
	}
	
	@Test
	public void testListaPorTipo() {
		assertEquals(10, getCarros("/api/v1/carros/tipo/classicos").getBody().size());
		assertEquals(10, getCarros("/api/v1/carros/tipo/luxo").getBody().size());
		assertEquals(10, getCarros("/api/v1/carros/tipo/esportivos").getBody().size());
		assertEquals(HttpStatus.NO_CONTENT, getCarros("/api/v1/carros/tipo/xxx").getStatusCode());
	}
	
	@Test
	public void testGetOk() {
		ResponseEntity<CarroDTO> response = getCarro("/api/v1/carros/11");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
		CarroDTO carro = response.getBody();
		assertEquals("Ferrari FF", carro.getNome());
	}
	
	@Test
	public void testGetNotFound() {
		ResponseEntity<CarroDTO> response = getCarro("/api/v1/carros/31");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testUpdate() {
		Carro carro = new Carro();
		carro.setNome("Fusca");
		carro.setTipo("classicos");
		
		ResponseEntity response = rest.withBasicAuth("user", "123").postForEntity("/api/v1/carros", carro, CarroDTO.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		String location = response.getHeaders().getLocation().toString();

		CarroDTO c = getCarro(location).getBody();

		assertNotNull(c);
		assertEquals("Fusca", c.getNome());
		assertEquals("classicos", c.getTipo());
				
		c.setNome("Fuscao Preto");
		c.setTipo("classicos");
		
		rest.withBasicAuth("user", "123").put(location, c);
		
		c = getCarro(location).getBody();
		assertEquals("Fuscao Preto", c.getNome());
		assertEquals("classicos", c.getTipo());
		
	}	
}
