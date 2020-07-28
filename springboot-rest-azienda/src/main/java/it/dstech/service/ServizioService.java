package it.dstech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dstech.model.Servizio;
import it.dstech.model.User;
import it.dstech.repository.ServizioRepository;

@Service
public class ServizioService {

	@Autowired
	private ServizioRepository servizioRep;
	
	public void save(Servizio servizio) {
		servizioRep.save(servizio);
	}
	
	public Servizio get(Long id) {
		return servizioRep.findById(id).get();
	}

	public void delete(Long id) {
		servizioRep.deleteById(id);
	}
	
	public List<Servizio> listaServizi() {
		return (List<Servizio>) servizioRep.findAll();
	}
	public void reset(){
		for (Servizio servizio : listaServizi()) {
			servizio.setQuantitàDisponibile(servizio.getQuantita());
			servizio.setUser(null);
			save(servizio);
		}
	}
	
}
