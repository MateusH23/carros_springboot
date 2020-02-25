package com.example.carros.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CarroService {
	
	@Autowired
	private CarroRepository carroRepository;
	
	public Iterable<Carro> getCarros(){
		return carroRepository.findAll();
	}
	
	public List<Carro> getCarrosFake(){
		List<Carro> carros = new ArrayList<>();
		carros.add(new Carro(1L, "Fusca"));
		carros.add(new Carro(2L, "Brasilia"));
		carros.add(new Carro(3L, "Chevette"));
		
		return carros;
	}

	public Optional<Carro> getCarroById(Long id) {
		return carroRepository.findById(id);
		
		/*Optional<Carro> op = carroRepository.findById(id);
		Carro carro = null;
		if(op.isPresent()) {
			carro = op.get();
		}
		
		return carro;*/
	}

	public Iterable<Carro> getCarrosByTipo(String tipo) {
		return carroRepository.findByTipo(tipo);
	}

	public Carro insert(Carro carro) {
		Assert.isNull(carro.getId(), "Não foi possível inserir o registro");
		return carroRepository.save(carro);
	}
	

	public Carro update(Long id, Carro carro) {
		Assert.notNull(id, "Não foi possível atualizar o registro");
		
		Optional<Carro> carroOp = carroRepository.findById(id);
		if(carroOp.isPresent()) {
			Carro carDb = carroOp.get();
			carDb.setNome(carro.getNome());
			carDb.setTipo(carro.getTipo());
			carroRepository.save(carDb);
			
			return carDb;
		} else {
			throw new RuntimeException("Não foi possível atualizar o registro!");
		}
	}

	public void delete(Long id) {
		Assert.notNull(id, "Não foi possível deletar o registro");
		
		Optional<Carro> carro = carroRepository.findById(id);
		if(carro.isPresent()) {
			carroRepository.delete(carro.get());			
		}
	}

}
