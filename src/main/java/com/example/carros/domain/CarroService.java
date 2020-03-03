package com.example.carros.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.carros.domain.dto.CarroDTO;

@Service
public class CarroService {
	
	@Autowired
	private CarroRepository carroRepository;
	
	public List<CarroDTO> getCarros(){
		return carroRepository.findAll().stream().map(CarroDTO::create).collect(Collectors.toList());
	}
	
	/*
	 * public List<Carro> getCarrosFake(){ List<Carro> carros = new ArrayList<>();
	 * carros.add(new Carro(1L, "Fusca")); carros.add(new Carro(2L, "Brasilia"));
	 * carros.add(new Carro(3L, "Chevette"));
	 * 
	 * return carros; }
	 */

	public Optional<CarroDTO> getCarroById(Long id) {
		return carroRepository.findById(id).map(CarroDTO::create);
		
		/*Optional<Carro> c = carroRepository.findById(id);
		if(c.isPresent()) {
			return Optional.of(new CarroDTO(c.get()));
		} else {
			return null;
		}*/
		
		/*Optional<Carro> op = carroRepository.findById(id);
		Carro carro = null;
		if(op.isPresent()) {
			carro = op.get();
		}
		
		return carro;*/
	}

	public List<CarroDTO> getCarrosByTipo(String tipo) {
		return carroRepository.findByTipo(tipo).stream().map(CarroDTO::create).collect(Collectors.toList());
	}

	public CarroDTO insert(Carro carro) {
		Assert.isNull(carro.getId(), "Não foi possível inserir o registro");
		
		return CarroDTO.create(carroRepository.save(carro));
	}
	

	public CarroDTO update(Long id, Carro carro) {
		Assert.notNull(id, "Não foi possível atualizar o registro");
		
		Optional<Carro> carroOp = carroRepository.findById(id);
		if(carroOp.isPresent()) {
			Carro carDb = carroOp.get();
			carDb.setNome(carro.getNome());
			carDb.setTipo(carro.getTipo());
			carroRepository.save(carDb);
			
			return CarroDTO.create(carDb);
		} else {
			return null;
		}
	}

	public boolean delete(Long id) {
		Assert.notNull(id, "Não foi possível deletar o registro");
		
		Optional<Carro> carro = carroRepository.findById(id);
		if(carro.isPresent()) {
			carroRepository.delete(carro.get());
			return true;
		}
		return false;
	}

}
