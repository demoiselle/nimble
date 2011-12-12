package com.mycompany.mypackage.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.gov.framework.demoiselle.core.bean.IPojo;

@Entity
public class MyBean implements IPojo {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	//TODO: Add attributes here
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}