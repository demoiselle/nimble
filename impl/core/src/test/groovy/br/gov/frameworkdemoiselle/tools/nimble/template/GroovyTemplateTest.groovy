package br.gov.frameworkdemoiselle.tools.nimble.template

class GroovyTemplateTest extends GroovyTestCase {
    void testApplyTemplate() {
        GroovyTemplate template = new GroovyTemplate()
        def tmpFile = new File("./src/test/templates/template.groovy")
		if (tmpFile.exists()) tmpFile.delete()
        tmpFile.deleteOnExit()
        tmpFile << '${var1} groovy template'
        // Test relative path
        assertEquals "1 groovy template", template.applyTemplate(tmpFile.getPath(), [var1:'1'])
        // Test absolute path
        assertEquals "1 groovy template", template.applyTemplate(tmpFile.getCanonicalPath(), [var1:'1'])
    }

    void testApplyStringTemplate() {
		GroovyTemplate template = new GroovyTemplate()
		assertEquals "1 groovy template", template.applyStringTemplate('${var1} groovy template', [var1:'1'])
	}

	void testApplyStringTemplatePackage() {
		GroovyTemplate template = new GroovyTemplate()
		assertEquals "./src/test/templates/dest/com.mycompany.mypackage/", template.applyStringTemplate('./src/test/templates/dest/${packageName}/', [packageName:'com.mycompany.mypackage'])
	}
		
    void testSupportedExt() {
        assert ["gvy","gt","groovy"] == new GroovyTemplate().getSupportedExt() 
    }
}
