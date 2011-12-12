package br.gov.frameworkdemoiselle.tools.nimble.template

import java.util.Map

/**
 * Created by IntelliJ IDEA.
 * User: 50765795515
 * Date: 06/10/2010
 * Time: 17:03:59
 * To change this template use File | Settings | File Templates.
 */
class FreeMarkerTemplateTest extends GroovyTestCase {
	void testApplyTemplate() {
		FreeMarkerTemplate template = new FreeMarkerTemplate()
		def tmpFile = new File("./src/test/templates/template.freemarker")
		tmpFile.deleteOnExit()
		tmpFile << '${var1} freemarker template'
		String texto = tmpFile.getPath()
		String texto2 = tmpFile.getCanonicalPath()
		// Test relative path
		//assertEquals "1 freemarker template", template.applyTemplate(tmpFile.getPath(), [var1:"1"])
		// Test absolute path
		assertEquals "1 freemarker template", template.applyTemplate(tmpFile.getCanonicalPath(), [var1:"1"])
	}
	
	void testApplyStringTemplate() {
		FreeMarkerTemplate template = new FreeMarkerTemplate()
		assertEquals "1 freemarker template", template.applyStringTemplate('${var1} freemarker template', [var1:'1'])
	}

	void testSupportedExt() {
		assert ["ftl","fm","freemarker"] == new FreeMarkerTemplate().getSupportedExt()
	}
}