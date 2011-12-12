package br.gov.frameworkdemoiselle.tools.nimble.util

class TemplateUtilTest extends GroovyTestCase {
	
	static final String TEST_TEMPLATE = "./src/test/templates/managedBean.vsl"
	
	void tearDown() {
		File tempDir = new File("./output/")
		if (tempDir.exists())
			FileUtil.delTree(tempDir)
	}
	
	void testApplyTemplateWithNoVars() {
		assertEquals "managedBean \$var1  \$var3", TemplateUtil.applyTemplate(TEST_TEMPLATE)
	}
	
	void testApplyTemplateWith1Var() {
		assertEquals "managedBean 1  \$var3", TemplateUtil.applyTemplate(TEST_TEMPLATE, [var1:1])
	}
	
	void testApplyTemplateWith2Vars() {
		assertEquals "managedBean 1 2 \$var3", TemplateUtil.applyTemplate(TEST_TEMPLATE, [var1:1,var2:2])
	}
	
	void testApplyTemplateWith3Vars() {
		assertEquals "managedBean 1 2 3", TemplateUtil.applyTemplate(TEST_TEMPLATE, [var1:1,var2:2,var3:3])
	}

	void testApplyStringTemplate() {
		assertEquals "1 2 String Template",  TemplateUtil.applyStringTemplate('$var1 ${var2} String Template', [var1:'1', var2:'2'])
	}
	
	void testMountDestFileNameWithPackageName() {
		String path = './output/src/main/templates/generate-crud/src/main/java/${packageName}/domain'
		String template = '${pojo}.java.vm'
		Map vars = [packageName:"com.mycompany", pojo:"MyPojo"]
		def expected = "./output/src/main/templates/generate-crud/src/main/java/com/mycompany/domain/MyPojo.java.vm"
		assertEquals expected, TemplateUtil.mountDestFileName(path, template, vars)
	}
	
	void atestMountDestFileNameWithoutName() {
		String path = './output/src/test/template/generate-crud/src/main/java'
		String template = '${project}-README.txt.vm'
		Map vars = [project:"myproject"]
		def expected = "./output/src/test/template/generate-crud/src/main/java/myproject-README.txt.vm"
		assertEquals expected, TemplateUtil.mountDestFileName(path, template, vars)
	}
}