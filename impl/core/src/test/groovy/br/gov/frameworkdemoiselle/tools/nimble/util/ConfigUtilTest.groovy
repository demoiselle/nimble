package br.gov.frameworkdemoiselle.tools.nimble.util

class ConfigUtilTest extends GroovyTestCase {
	
	void testConfig() {
		File tmpFile = new File("./src/test/file.conf")
		tmpFile.deleteOnExit()
		tmpFile << "stringVar='value1'\nbooleanVar=true\nlistVar=['a','b']\nmapVar=['k1':'a','k2':'b']"
		def conf = ConfigUtil.getConfig(tmpFile.canonicalPath)
		assertEquals "value1", conf?.stringVar
		assertTrue conf?.booleanVar
		assert ['a', 'b']== conf?.listVar
		assertEquals 'a', conf?.mapVar.k1
		assertEquals 'b', conf?.mapVar.k2
	}

	void testEvaluate() {
		def vars = [generateTemplate:true, skip:false, stringVar:'true']
		assertFalse ConfigUtil.evaluate(vars, "generateTemplate == false")
		assertTrue ConfigUtil.evaluate(vars, "skip == false")
		assertTrue ConfigUtil.evaluate(vars, "generateTemplate")
		assertFalse ConfigUtil.evaluate(vars, "skip")
		assertTrue ConfigUtil.evaluate(vars, "stringVar == 'true'")
	}
	
	void testGetTemplateConfig() {
		File tmpTemplatePath = new File("./src/test/")
		tmpTemplatePath.deleteOnExit()
		
		File tmpConfFile = new File("./src/test/template.conf")
		tmpConfFile.deleteOnExit()
		tmpConfFile << "name='MyTemplate'\ndescription='MyDescription'\nversion='2.0'\nvars {\nvar1 {\nlabel='var1'\n}\n}"

		def expected = [name:'MyTemplate', folderName:tmpTemplatePath.path, description:'MyDescription', version:'2.0', path:tmpTemplatePath.path]
		assert expected == ConfigUtil.getTemplateConfig(tmpTemplatePath)
	}
	
	void testLoadCustomVars() {
		def vars = [packageName:"br.gov.frameworkdemoiselle.sample", pojo:"Bean"]
		def customVars = [bean:"pojo.toLowerCase()", pojoFileName:"packageName.replace('.', '/') + '/domain/' + pojo + '.java'"]
		vars = ConfigUtil.loadCustomVars(vars, customVars)
		def expected = [packageName:"br.gov.frameworkdemoiselle.sample", pojo:"Bean", bean:"bean", pojoFileName:"br/gov/frameworkdemoiselle/sample/domain/Bean.java"]
		assert expected == vars
	}
	
	void testLoadVars() {
		File tmpFile = new File("./src/test/_template.conf")
		tmpFile.deleteOnExit()
		tmpFile << "vars {\npackageName {\nlabel='Package'\n}\npojo {\nlabel='Entity'\nrequired=false\n}\n}"
		def varList = []
		varList = ConfigUtil.loadVars(varList, tmpFile.canonicalPath)
		assertEquals "packageName", varList[0].name
		assertEquals "Package", varList[0].label
		assertTrue varList[0].required
		assertEquals "pojo", varList[1].name
		assertEquals "Entity", varList[1].label
		assertFalse varList[1].required
	}
}