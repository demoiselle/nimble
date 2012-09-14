package br.org.frameworkdemoiselle.tools.nimble.test.domain;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class Pessoa {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private Long cpf;

	@Column(nullable = false, length = 255)
	private String nome;

	@Column(nullable = false)
	@Temporal(value = TemporalType.DATE)
	private Date dataNascimento;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}
}
