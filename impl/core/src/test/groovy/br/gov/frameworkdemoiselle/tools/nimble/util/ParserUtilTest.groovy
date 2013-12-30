/*
 Demoiselle Framework
 Copyright (C) 2013 SERPRO
 ============================================================================
 This file is part of Demoiselle Framework.
 Demoiselle Framework is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License version 3
 as published by the Free Software Foundation.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License version 3
 along with this program; if not,  see <http://www.gnu.org/licenses/>
 or write to the Free Software Foundation, Inc., 51 Franklin Street,
 Fifth Floor, Boston, MA  02110-1301, USA.
 ============================================================================
 Este arquivo é parte do Framework Demoiselle.
 O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
 modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
 do Software Livre (FSF).
 Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
 GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
 APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
 para maiores detalhes.
 Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
 "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
 ou escreva para a Fundação do Software Livre (FSF) Inc.,
 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */
package br.gov.frameworkdemoiselle.tools.nimble.util

import java.util.List;
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil.AnnotationDeclarationVisitor



public class ParserUtilTest extends GroovyTestCase {
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
	@GeneratedValue(strategy = SEQUENCE)
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
	private String textoQualquer;
	
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

def code4 = '''
package br.org.frameworkdemoiselle.tools.nimble.test.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
public class EntidadeAsterisco implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Integer id;
	
	@Column
	private String text;
	
	@Column
	@Temporal(value = TemporalType.DATE)
	private Date date;
	
	
	public EntidadeAsterisco() {
		super();
	}
	
	public EntidadeAsterisco(String text, Date date) {
		super();
		this.text = text;
		this.date = date;
	}

	
	public Integer getId() {
		return id;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getText() {
		return text;
	}

	
	public void setText(String text) {
		this.text = text;
	}

	
	public Date getDate() {
		return date;
	}

	
	public void setDate(Date date) {
		this.date = date;
	}

}
'''


def code5 = '''
package br.org.frameworkdemoiselle.tools.nimble.test.domain;

import static javax.persistence.GenerationType.SEQUENCE;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class EntidadeComRelacionamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;
	
	@Column
	private String texto;
	
	@Column
	@Temporal(value = TemporalType.DATE)
	private Date umaData;

	
    @OneToMany(cascade = ALL, fetch = LAZY)
	@JoinColumn(name = "EntidadeRelacao_fk")
	private List<EntidadeRelacao> entidadeRelacao = new ArrayList<EntidadeRelacao>();

	@ManyToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "tb_estudante_endereco", joinColumns = @JoinColumn(name = "id_estudante"), inverseJoinColumns = @JoinColumn(name = "cod_endereco"))
	private List<Endereco> enderecos;	

	public EntidadeComRelacionamento() {
		super();
	}
	
	public EntidadeComRelacionamento(String texto, Date data) {
		super();
		this.texto = texto;
		this.umaData = data;
	}
	
	public EntidadeComRelacionamento(final String texto, final Date data, final List<EntidadeRelacao> entidadeRelacao) {
		super();
		this.texto = texto;
		this.umaData = data;
		this.entidadeRelacao = entidadeRelacao;
	}


	
	public Integer getId() {
		return id;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getTexto() {
		return this.texto;
	}

	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	
	public Date getUmaData() {
		return this.umaData;
	}

	
	public void setUmaData(Date data) {
		this.umaData = data;
	}

}
'''

def code6 = '''
package br.org.frameworkdemoiselle.tools.nimble.test.domain;

import static javax.persistence.GenerationType.SEQUENCE;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class OutraEntidadeComRelacionamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long identificador;
	
	@Column
	private String texto;
	
	@Column
	private Date umaData;

	
    @OneToMany(cascade = ALL, fetch = LAZY)
	@JoinColumn(name = "EntidadeRelacao_fk")
	private List<EntidadeRelacao> entidadeRelacao = new ArrayList<EntidadeRelacao>();	

	public OutraEntidadeComRelacionamento() {
		super();
	}
	
	public OutraEntidadeComRelacionamento(String texto, Date data) {
		super();
		this.texto = texto;
		this.umaData = data;
	}
	
	public OutraEntidadeComRelacionamento(final String texto, final Date data, final List<EntidadeRelacao> entidadeRelacao) {
		super();
		this.texto = texto;
		this.umaData = data;
		this.entidadeRelacao = entidadeRelacao;
	}


	
	public Long getIdentificador() {
		return this.identificador;
	}

	
	public void setId(Long identificador) {
		this.identificador = identificador;
	}

	
	public String getTexto() {
		return this.texto;
	}

	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	
	public Date getUmaData() {
		return this.umaData;
	}

	
	public void setUmaData(Date data) {
		this.umaData = data;
	}

}
'''
		void tearDown() {
			File tempDir = new File("./src/test/temp/")
			if (tempDir.exists())
				FileUtil.delTree(tempDir)
		}
		
	void testGetExtendedClassesFiles() {
		
			def tempDir = new File("./src/test/temp/")
			if (tempDir.exists()) tempDir.deleteDir()
			tempDir.mkdir()
			def tmpFile1 = new File("./src/test/temp/Classe1.java")
			tmpFile1 << code1
			
			def tmpFile2 = new File("./src/test/temp/Classe2.java")
			tmpFile2 << code2
			
			def expected = ['Classe1']
			
			assert expected == ParserUtil.getExtendedClassesFiles(tmpFile2)
		
	}
	
	void testGetAttributesFromClassFile() {
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		
		def tmpFile1 = new File("./src/test/temp/Classe1.java")
		tmpFile1 << code1
		
		def tmpFile2 = new File("./src/test/temp/Classe2.java")
		tmpFile2 << code2
		
		def tmpFile3 = new File("./src/test/temp/Classe3.java")
		tmpFile3 << code3
		
		def expected1 = ['dataMatricula':'Date', 'numeroMatricula':'int', 'id':'Long', 'cpf':'Long', 'nome':'String', 'dataNascimento':'Date']
		
		def expected2 = ['identificador':'Integer', 'textoQualquer':'String', 'data':'Date']
		
		assert expected2 == ParserUtil.getAttributesFromClassFile(tmpFile3)
		              
		assert expected1 == ParserUtil.getAttributesFromClassFile(tmpFile2)
		
		

	}
	
	void testGetPackageNameFromClassFile() {
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		def tmpFile1 = new File("./src/test/temp/Classe1.java")
		tmpFile1 << code1
		
		def expected = "br.org.frameworkdemoiselle.Teste.domain"
		
		assert expected == ParserUtil.getPackageNameFromClassFile(tmpFile1)
	}	
	
	
	void testGetCompilationUnit() {
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		
		def tmpFile4 = new File("./src/test/temp/Classe4.java")
		tmpFile4 << code2
		
		def expected = 'package br.org.frameworkdemoiselle.Teste.domain;'
		
		def ret = ParserUtil.getCompilationUnit(tmpFile4).getPackage()
		assert expected.toString().trim() == ret.toString().trim()
		
	}
	

		
	void testAnnotationDeclarationVisitor() {
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		
		def tmpFile5 = new File("./src/test/temp/Classe5.java")
		tmpFile5 << code6
		def compUtil = ParserUtil.getCompilationUnit(tmpFile5)
		
		new AnnotationDeclarationVisitor().visit(compUtil, null);
		assertFalse new AnnotationDeclarationVisitor().getFieldAndAnnotationsUtilList().empty
		
	}

	
	void testHasAnnotationForField (){
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		
		def tmpFile1 = new File("./src/test/temp/Classe1.java")
		tmpFile1 << code1
		
		def tmpFile2 = new File("./src/test/temp/Classe2.java")
		tmpFile2 << code2

		
		def tmpFile6 = new File("./src/test/temp/Classe6.java")
		tmpFile6 << code5
		
		assert ParserUtil.hasAnnotationForField(tmpFile2, 'id', 'Id' )
		
		assert ParserUtil.hasAnnotationForField(tmpFile6, 'entidadeRelacao', 'OneToMany' )
		assert ParserUtil.hasAnnotationForField(tmpFile6, 'umaData', 'Temporal' )
		assert ParserUtil.hasAnnotationForField(tmpFile6, 'enderecos', 'JoinTable' )
		
		
		assertFalse  ParserUtil.hasAnnotationForField(tmpFile6, 'texto', 'OneToMany' )

	}
	
	
	void testGetAnnotationsForField (){
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		
		
		def tmpFile1 = new File("./src/test/temp/Classe1.java")
		tmpFile1 << code1
		
		def tmpFile2 = new File("./src/test/temp/Classe2.java")
		tmpFile2 << code2
			
		def tmpFile7 = new File("./src/test/temp/Classe7.java")
		tmpFile7 << code5
		
		def expected1 = ['Id', 'GeneratedValue']
		
		def expected2 = ['Column', 'Temporal']
		
		assert expected1 == ParserUtil.getAnnotationsForField(tmpFile2, 'id')
		
		assert expected2 == ParserUtil.getAnnotationsForField(tmpFile7, 'umaData')		
	}
	
	void testGetFieldAnnotatedWith(){
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
						
		def tmpFile8 = new File("./src/test/temp/Classe8.java")
		tmpFile8 << code5
		
		def tmpFile9 = new File("./src/test/temp/Classe9.java")
		tmpFile9 << code6
		
		def expected1 = "id"
		
		def expected2 = "identificador"
		
		assert expected1 == ParserUtil.getFieldAnnotatedWith(tmpFile8, 'Id')
		
		assert expected2 == ParserUtil.getFieldAnnotatedWith(tmpFile9, 'Id')
		
	}
	
	void testGetFieldValue(){
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
						
		def tmpFile10 = new File("./src/test/temp/Classe10.java")
		tmpFile10 << code5
		
		def tmpFile11 = new File("./src/test/temp/Classe11.java")
		tmpFile11 << code6
		
		def expected1 = "List<Endereco>"
		
		def expected2 = "List<EntidadeRelacao>"
		
		assert expected1 == ParserUtil.getFieldValue (tmpFile10, 'enderecos')
		assert expected2 == ParserUtil.getFieldValue (tmpFile11, 'entidadeRelacao')
	}
	
	void testGenerateDataForFields(){
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
						
		def tmpFile12 = new File("./src/test/temp/Classe12.java")
		tmpFile12 << code5
		
		def tmpFile13 = new File("./src/test/temp/Classe13.java")
		tmpFile13 << code6
		
		def expected1 = '"texto",new Date(),null,null'
		def expected2 = 'Long.valueOf(1),"texto",new Date(),null'

		assert expected1 == ParserUtil.generateDataForFields (tmpFile12)
		assert expected2 == ParserUtil.generateDataForFields (tmpFile13)
		
	}
}


