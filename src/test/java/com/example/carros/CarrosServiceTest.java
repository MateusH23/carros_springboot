package com.example.carros;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.carros.domain.Carro;
import com.example.carros.domain.CarroService;
import com.example.carros.domain.dto.CarroDTO;

@SpringBootTest
class CarrosServiceTest {
	
	@Autowired
	private CarroService service;

	@Test
	void testSave() {
		
		Carro c = new Carro();
		c.setNome("Impala 67");
		c.setTipo("classicos");
		
		CarroDTO cDto = service.insert(c);
		
		assertNotNull(cDto);
		
		Long id = cDto.getId();
		assertNotNull(id);
		
		// Buscando o registro do banco
		Optional<CarroDTO> op = service.getCarroById(id);
		assertTrue(op.isPresent());
		
		//Comparando o objeto
		cDto = op.get();
		assertEquals("Impala 67", cDto.getNome());
		assertEquals("classicos", cDto.getTipo());
		
		// Deletando o registro
		service.delete(id);
		
		assertFalse(service.getCarroById(id).isPresent());
		
	}
	
	@Test
	void testLista() {
		List<CarroDTO> lista = service.getCarros();
		
		assertEquals(30, lista.size());
	}
	
	@Test
	void testListaTipo() {
		assertEquals(10, service.getCarrosByTipo("classicos").size());
		assertEquals(10, service.getCarrosByTipo("esportivos").size());
		assertEquals(10, service.getCarrosByTipo("luxo").size());
		assertEquals(0, service.getCarrosByTipo("a").size());
	}
	
	@Test
	void testGet() {
		Optional<CarroDTO> c = service.getCarroById(1L);
		assertTrue(c.isPresent());
		
		CarroDTO carro = c.get();
		assertEquals("Tucker 1948", carro.getNome());
	}

}
