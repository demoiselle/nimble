package br.gov.frameworkdemoiselle.tools.nimble.util

import java.io.File

class FileUtilTest extends GroovyTestCase {
	
	void tearDown() {
		File tempDir = new File("./src/test/temp/")
		if (tempDir.exists())
			FileUtil.delTree(tempDir)
	}

	void testAddSepToStringWithNoSeparator() {
		assertEquals "/dest/dir/", FileUtil.addSep("/dest/dir")
	}

	void testAddSepToStringAlreadyWithSeparator() {
		assertEquals "/dest/dir/", FileUtil.addSep("/dest/dir/")
	}

	void testRemoveSepFromStringWithNoSeparator() {
		assertEquals "/dest/dir", FileUtil.removeSep("/dest/dir/")
	}

	void testRemoveSepFromStringAlreadyWithSeparator() {
		assertEquals "/dest/dir", FileUtil.removeSep("/dest/dir")
	}

	void testRemoveExtFromFile() {
		assertEquals "/dest/dir/file", FileUtil.removeExt("/dest/dir/file.ext")
	}

	void testRemoveExtFromFileWithNoExt() {
		assertEquals "/dest/dir/file", FileUtil.removeExt("/dest/dir/file")
	}

	void testGetExtFromFile() {
		assertEquals "ext", FileUtil.getExt("/dest/dir/file.ext")
	}

	void testGetExtFromFileWithNoExt() {
		assertEquals "", FileUtil.getExt("/dest/dir/file")
	}
	
	void testDelTree() {
		def tempDir = new File("./src/test/temp/del/tree")
		tempDir.mkdirs()
		assertTrue tempDir.exists()
		FileUtil.delTree(tempDir)
		assertFalse tempDir.exists()
	}

	void testCopyFileToDir() {
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		def from = new File("./src/test/temp/from.txt")
		from << "from"
		def toDir = new File("./src/test/temp/destDir/")
		if (toDir.exists()) toDir.deleteDir()
		toDir.mkdir()
		def returned = new File(FileUtil.copy(from.path, toDir.path))
		assertEquals new File("./src/test/temp/destDir/from.txt").canonicalPath, returned.canonicalPath
	}

	void testCopyFileToFile() {
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		def fromTo = new File("./src/test/temp/fromTo.txt")
		fromTo << "fromTo"
		def toFileString = "./src/test/temp/destDir/to.txt"
		def toFile = new File(toFileString)
		def returned = new File(FileUtil.copy(fromTo.path, toFileString))
		assertEquals "to.txt", returned.name
	}

	void testConvertPropertiesFileToMap() {
		def tmpFile = new File("./src/test/vars.txt")
		tmpFile.deleteOnExit()
		tmpFile << '''var1=value1
		var2=value2
		var3=value3
		'''
		def expected = [var1:"value1", var2:"value2", var3:"value3"]
		assertTrue expected == FileUtil.convertPropsToMap("./src/test/vars.txt")
	}

	String code1 = '''
	package br.org.frameworkdemoiselle.TesteNimble.domain;

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
	void testHasString() {
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		
		def tmpFile1 = new File("./src/test/temp/Classe1.java")
		tmpFile1 << code1
		
		def tmpFile2 = new File("./src/test/temp/Classe2.java")
		tmpFile2 << code2
		
		def expected = true
		
		assert expected == FileUtil.hasString ("./src/test/temp/Classe2.java", "@Entity")
						
		assert expected == FileUtil.hasString ("./src/test/temp/Classe1.java", "@Id")
		
		expected = false
		
		assert expected == FileUtil.hasString ("./src/test/temp/Classe2.java", "@Id")
		
		assert expected == FileUtil.hasString ("./src/test/temp/Classe1.java", "@Entity")
	}
	
		
}
