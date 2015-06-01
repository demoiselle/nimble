<%
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
def tmpFile = new File(beanPath+beanJavaName)
def varDataForFields = PU.generateDataForFields(tmpFile)
%>

package ${packageName}.business;

import static org.junit.Assert.*;
import java.util.*;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.gov.frameworkdemoiselle.junit.DemoiselleRunner;
import ${packageName}.domain.${pojo};
import ${packageName}.business.${pojo}BC;

@RunWith(DemoiselleRunner.class)
public class ${pojo}BCTest {

    @Inject
	private ${pojo}BC ${beanLower}BC;
	
	@Before
	public void before() {
		for (${pojo} ${beanLower} : ${beanLower}BC.findAll()) {
			${beanLower}BC.delete(${beanLower}.get${idNameUpper}());
		}
	}	
	
	
	@Test
	public void testInsert() {
				
		// modifique para inserir dados conforme o construtor
		${pojo} ${beanLower} = new ${pojo}(${varDataForFields});
		${beanLower}BC.insert(${beanLower});
		List<${pojo}> listOf${pojo} = ${beanLower}BC.findAll();
		assertNotNull(listOf${pojo});
		assertEquals(1, listOf${pojo}.size());
	}	
	
	@Test
	public void testDelete() {
		
		// modifique para inserir dados conforme o construtor
		${pojo} ${beanLower} = new ${pojo}(${varDataForFields});
		${beanLower}BC.insert(${beanLower});
		
		List<${pojo}> listOf${pojo} = ${beanLower}BC.findAll();
		assertNotNull(listOf${pojo});
		assertEquals(1, listOf${pojo}.size());
		
		${beanLower}BC.delete(${beanLower}.get${idNameUpper}());
		listOf${pojo} = ${beanLower}BC.findAll();
		assertEquals(0, listOf${pojo}.size());
	}
	
	@Test
	public void testUpdate() {
		// modifique para inserir dados conforme o construtor
		${pojo} ${beanLower} = new ${pojo}(${varDataForFields});
		${beanLower}BC.insert(${beanLower});
		
		List<${pojo}> listOf${pojo} = ${beanLower}BC.findAll();
		${pojo} ${beanLower}2 = (${pojo})listOf${pojo}.get(0);
		assertNotNull(listOf${pojo});

		// alterar para tratar uma propriedade existente na Entidade ${pojo}
		// ${beanLower}2.setUmaPropriedade("novo valor");
		${beanLower}BC.update(${beanLower}2);
		
		listOf${pojo} = ${beanLower}BC.findAll();
		${pojo} ${beanLower}3 = (${pojo})listOf${pojo}.get(0);
		
		// alterar para tratar uma propriedade existente na Entidade ${pojo}
		// assertEquals("novo valor", ${beanLower}3.getUmaPropriedade());
	}

}