package com.example.carros.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.carros.domain.Carro;
import com.example.carros.domain.CarroService;
import com.example.carros.domain.dto.CarroDTO;

@RestController
@RequestMapping("/api/v1/carros")
public class CarroController {

	@Autowired
	private CarroService carroService;

	@GetMapping
	public ResponseEntity get() {
		return ResponseEntity.ok(carroService.getCarros());
		// return new ResponseEntity<>(carroService.getCarros(), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/{id}")
	public ResponseEntity getCarroById(@PathVariable("id") Long id) {
		CarroDTO carro = carroService.getCarroById(id);

		return ResponseEntity.ok(carro);

		/*
		 * return carro.isPresent() ? ResponseEntity.ok(carro.get()) :
		 * ResponseEntity.notFound().build();
		 */

		/*
		 * if(carro.isPresent()) { return ResponseEntity.ok(carro.get()); } else {
		 * return ResponseEntity.notFound().build(); }
		 */
	}

	@GetMapping("/tipo/{tipo}")
	public ResponseEntity getCarrosByTipo(@PathVariable("tipo") String tipo) {
		List<CarroDTO> carros = carroService.getCarrosByTipo(tipo);

		return carros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carros);
	}

	@PostMapping
	public ResponseEntity save(@RequestBody Carro carro) {
		CarroDTO c = carroService.insert(carro);
		URI location = getUri(c.getId());
		return ResponseEntity.created(location).build();
	}

	private URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(id).toUri();
	}

	@PutMapping("/{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Carro carro) {
		CarroDTO c = carroService.update(id, carro);
		return c != null ?
				ResponseEntity.ok(c) :
					ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") Long id) {
		carroService.delete(id);
		
		return ResponseEntity.ok().build();
	}

}
