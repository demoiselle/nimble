package br.gov.frameworkdemoiselle.tools.nimble.util



public class CompilerUtilTest extends GroovyTestCase {
	
	
	void testgetClassFromFile() {
		
		String fileLocation = "./src/test/resources/br/org/frameworkdemoiselle/tools/nimble/test/domain/";
		String fileName = "Estudante"
		String packageFile = "br.org.frameworkdemoiselle.tools.nimble.test.domain"

		def expected = "br.org.frameworkdemoiselle.tools.nimble.test.domain.Estudante"
		
		assert expected == CompilerUtil.getClassFromFile(fileLocation, fileName, packageFile).getName()
		
	}
}


