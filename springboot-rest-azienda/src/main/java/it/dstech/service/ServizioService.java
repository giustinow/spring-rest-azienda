package it.dstech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dstech.model.Servizio;
import it.dstech.repository.ServizioRepository;

@Service
public class ServizioService {

	@Autowired
	private ServizioRepository servizioRep;
	
	public void save(Servizio servizio) {
		servizioRep.save(servizio);
	}
}
