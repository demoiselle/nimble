package br.gov.frameworkdemoiselle.tools.nimble.util

import java.io.Serializable;
import java.util.Date;
import br.gov.frameworkdemoiselle.tools.nimble.util.ReflectionUtil;


public class ReflectionUtilTest extends GroovyTestCase {
	def code1 = '''
package br.org.frameworkdemoiselle.Teste.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class Classe1 {

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
		'''
			
			
			def code2 = '''
package br.org.frameworkdemoiselle.Teste.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Classe2 extends Classe1 implements Serializable {

	/**
		 *
		 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Temporal(value = TemporalType.DATE)
	private Date dataMatricula;

	@Column
	private int numeroMatricula;

	public Date getDataMatricula() {
		return dataMatricula;
	}

	public void setDataMatricula(Date dataMatricula) {
		this.dataMatricula = dataMatricula;
	}

	public int getNumeroMatricula() {
		return numeroMatricula;
	}

	public void setNumeroMatricula(int numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}

}
		'''
			
		def code3 = '''
package br.org.frameworkdemoiselle.tools.nimble.test.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Entidade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*
				 *  If you are using Glassfish then remove the strategy attribute
				 */
	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Integer identificador;
	
	@Column
	private String texto;
	
	@Column
	@Temporal(value = TemporalType.DATE)
	private Date data;
	
	
	public Entidade() {
		super();
	}
	
	public Entidade(String texto, Date data) {
		super();
		this.texto = texto;
		this.data = data;
	}

	
	public Integer getIdentificador() {
		return identificador;
	}

	
	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}

	
	public String getTexto() {
		return texto;
	}

	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	
	public Date getData() {
		return data;
	}

	
	public void setData(Date data) {
		this.data = data;
	}

}
'''
	
		void tearDown() {
			File tempDir = new File("./src/test/temp/")
			if (tempDir.exists())
				FileUtil.delTree(tempDir)
		}
		
	void testGetExtendedClasses() {
		
			def tempDir = new File("./src/test/temp/")
			if (tempDir.exists()) tempDir.deleteDir()
			tempDir.mkdir()
			def tmpFile1 = new File("./src/test/temp/Classe1.java")
			tmpFile1 << code1
			
			def tmpFile2 = new File("./src/test/temp/Classe2.java")
			tmpFile2 << code2
			
			def expected = ['Classe1']
			
			assert expected == ReflectionUtil.getExtendedClassesFiles(tmpFile2)
		
	}
	
	void testGetAttributesFromClass() {
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		
		def tmpFile2 = new File("./src/test/temp/Classe2.java")
		tmpFile2 << code3
		
		def expected = ['identificador':'Integer', 'texto':'String', 'data':'Date']
		                
		
		assert expected == ReflectionUtil.getAttributesFromClassFile(tmpFile2)
		
		
		
		
	}
	
	void testGetPackageNameFromClass() {
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		def tmpFile1 = new File("./src/test/temp/Classe1.java")
		tmpFile1 << code1
		
		def expected = "br.org.frameworkdemoiselle.Teste.domain"
		
		assert expected == ReflectionUtil.getPackageNameFromClassFile(tmpFile1)
	}
	
	void testGetSuperClass() {
		List<String> listOfStrings = new ArrayList<String>()
		def expected = "java.util.AbstractList"
		assert expected == ReflectionUtil.getSuperClass(listOfStrings.getClass()).getName()
	}
	
	void testGetSuperClasses() {
	
		List<String> listOfStrings = new ArrayList<String>()
		def expected = "java.util.AbstractList"
		assert expected == ReflectionUtil.getSuperClasses(listOfStrings.getClass())[0].getName()
	
		
	}
	
	void testGetFieldsFromClass() {
		
			List<String> listOfStrings = new ArrayList<String>()
			def expected = "serialVersionUID"
			assert expected == ReflectionUtil.getFieldsFromClass(listOfStrings.getClass())[0].getName()
		
			
		}	
	
}


