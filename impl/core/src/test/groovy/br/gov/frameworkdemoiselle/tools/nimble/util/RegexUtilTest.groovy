package br.gov.frameworkdemoiselle.tools.nimble.util

import java.io.Serializable;
import java.util.Date;


public class RegexUtilTest extends GroovyTestCase {
	
	def codeA = '''
		public class A {
	
	private String A;

	private Long B;

	private int C;

	private boolean D;
	
	private boolean E;

	public String getA() {
		return A;
	}

	public void setA(String a) {
		A = a;
	}

	public Long getB() {
		return B;
	}

	public void setB(Long b) {
		B = b;
	}

	public int getC() {
		return C;
	}

	public void setC(int c) {
		C = c;
	}

	public boolean isD() {
		return D;
	}

	public void setD(boolean d) {
		D = d;
	}

	public boolean isE() {
		return E;
	}

	public void setE(boolean e) {
		E = e;
	}
	}'''
	
	
	def codeB = '''
		public class B {
	
	private String A;

	private Long B;

	private int C;

	private boolean D;
	
	private boolean E;

	public String getA() {
		return A;
	}

	public void setA(String a) {
		A = a;
	}

	public Long getB() {
		return B;
	}

	public void setB(Long b) {
		B = b;
	}

	public int getC() {
		return C;
	}

	public void setC(int c) {
		C = c;
	}

	public boolean isD() {
		return D;
	}

	public void setD(boolean d) {
		D = d;
	}

	public boolean isE() {
		return E;
	}

	public void setE(boolean e) {
		E = e;
	}
	}'''
	
	def code1 = '''
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
	
	void setUp(){
		File tempDir = new File("./src/test/temp/")
		if (tempDir.exists())
			FileUtil.delTree(tempDir)
	}
	
	void tearDown() {
		File tempDir = new File("./src/test/temp/")
		if (tempDir.exists())
			FileUtil.delTree(tempDir)
	}
	
	void testGetClassAttributes() {
		
		def expected0 = ['A':'String','B':'Long','C':'int','D':'boolean','E':'boolean']
		RegexUtil.resetMap()
		assert expected0 == RegexUtil.getClassAttributes(codeA) 
	}

	void testGetClassAttributesFromFile() {		
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		def tmpFile = new File("./src/test/temp/B.java")
		tmpFile << codeB
		
		def expected = ['A':'String','B':'Long','C':'int','D':'boolean', 'E':'boolean']
		
		RegexUtil.resetMap()
		assert expected == RegexUtil.getClassAttributesFromFile(tmpFile.path)
	}
	
	
	void testGetClassAttributesFromFileAndPath() {
		
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		
		
		def tmpFile1 = new File("./src/test/temp/Classe1.java")
		tmpFile1 << code1
		
		def tmpFile2 = new File("./src/test/temp/Classe2.java")
		tmpFile2 << code2
		
		def expected = ['DataMatricula':'Date','NumeroMatricula':'int','Id':'Long','Nome':'String','Cpf':'Long','DataNascimento':'Date']
		
		RegexUtil.resetMap()
		assert expected == RegexUtil.getClassAttributesFromFile(tmpFile2.getName(),tmpFile2.getParent()+"/" )
	}
	
	
}