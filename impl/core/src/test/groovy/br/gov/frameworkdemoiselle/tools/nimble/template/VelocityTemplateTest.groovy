package br.gov.frameworkdemoiselle.tools.nimble.template

class VelocityTemplateTest extends GroovyTestCase {
    void testApplyTemplate() {
        VelocityTemplate template = new VelocityTemplate()
        def tmpFile = new File("./src/test/templates/template.vm")
        tmpFile.deleteOnExit()
        tmpFile << '${var1} velocity template'
        // Test relative path
        assertEquals "1 velocity template", template.applyTemplate(tmpFile.getPath(), [var1:"1"])
        // Test absolute path
        assertEquals "1 velocity template", template.applyTemplate(tmpFile.getCanonicalPath(), [var1:"1"])
    }

    void testApplyStringTemplate() {
        VelocityTemplate template = new VelocityTemplate()
        assertEquals "1 velocity template", template.applyStringTemplate('${var1} velocity template', [var1:'1'])
    }

    void testSupportedExt() {
        assert ["vsl","vm","velocity"] == new VelocityTemplate().getSupportedExt() 
    }
}
