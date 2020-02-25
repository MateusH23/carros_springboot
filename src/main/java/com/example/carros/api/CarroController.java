package com.example.carros.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.carros.domain.Carro;
import com.example.carros.domain.CarroService;

@RestController
@RequestMapping("/api/v1/carros")
public class CarroController {
	
	@Autowired
	private CarroService carroService ;
	
	@GetMapping
	public Iterable<Carro> get(){
		return carroService.getCarros();
	}
	
	@GetMapping("/{id}")
	public Optional<Carro> getCarroById(@PathVariable("id") Long id){
		return carroService.getCarroById(id);
	}
	
	@GetMapping("/tipo/{tipo}")
	public Iterable<Carro> getCarrosByTipo(@PathVariable("tipo") String tipo){
		return carroService.getCarrosByTipo(tipo);
	}
	
	@PostMapping
	public String save(@RequestBody Carro carro) {
		Carro c = carroService.insert(carro);
		return "Carro salvo com sucesso: " + c.getId();
	}
	
	@PutMapping("/{id}")
	public String atualizar(@PathVariable("id") Long id, @RequestBody Carro carro) {
		Carro c = carroService.update(id, carro);
		return "Carro atualizado com sucesso: " + c.getId();
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") Long id) {
		carroService.delete(id);
		return "Carro deletado com sucesso: " + id;
	}

}
