package it.dstech.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

	@Entity
	@Table(name = "AUTHORITIES")
	public class Authority {

	    @Id
	    @Column(name = "ID")
	    @GeneratedValue
	    private Long id;

	    @Column(name = "NAME", length = 50)
	    @Enumerated(EnumType.STRING)
	    private AuthorityName name;

	    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
	    @JsonBackReference
	    private List<User> users;

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public AuthorityName getName() {
	        return name;
	    }

	    public void setName(AuthorityName name) {
	        this.name = name;
	    }

	    public List<User> getUsers() {
	        return users;
	    }

	    public void setUsers(List<User> users) {
	        this.users = users;
	    }
}
