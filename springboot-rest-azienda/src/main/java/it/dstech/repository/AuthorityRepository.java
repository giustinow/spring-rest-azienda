package it.dstech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.dstech.model.Authority;
import it.dstech.model.AuthorityName;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Authority findByName(AuthorityName name);
}
