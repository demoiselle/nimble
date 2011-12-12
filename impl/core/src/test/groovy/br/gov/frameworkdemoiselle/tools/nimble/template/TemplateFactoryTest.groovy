package br.gov.frameworkdemoiselle.tools.nimble.template

class TemplateFactoryTest extends GroovyTestCase {
    void testNullFactory() {
        def templ = TemplateFactory.getTemplate("unkown")
        assertNull templ
    }

    void testVelocityFactory() {
        ["vsl","vm","velocity"].each {
            def templ = TemplateFactory.getTemplate(it)
            assertTrue templ instanceof VelocityTemplate
        }
    }

    void testFreeMarkerFactory() {
        ["ftl","fm","freemarker"].each { ext ->
            def templ = TemplateFactory.getTemplate(ext)
            assertTrue templ instanceof FreeMarkerTemplate
        }
    }

    void testGroovyFactory() {
        def templ = TemplateFactory.getTemplate("groovy")
        assertTrue templ instanceof GroovyTemplate
    }
}
