package br.gov.frameworkdemoiselle.tools.nimble

import static org.junit.Assert.*

import org.junit.AfterClass;
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import br.gov.frameworkdemoiselle.tools.nimble.util.FileUtil;

class DemoiselleNimbleTest {
	
	static final String TEST_TEMPLATE_DEST_PATH = "./src/test/templates/"
	static final String TEST_TEMPLATES_SRC_PATH = "./src/test/templates/src/test1/"
	static final String TEST_TEMPLATES_DEST_PATH = "./src/test/templates/dest/test1/"
	static final String TEST_CONSOLE_SRC_PATH = "./src/test/templates/src/console/"
	static final String TEST_CONSOLE_DEST_PATH = "./src/test/templates/dest/console/"

	private DemoiselleNimble nimble
	
	@AfterClass
	@BeforeClass
	static void removeDestinationFolderIfExists() {
		def destFolder = new File(TEST_TEMPLATES_DEST_PATH)
		if (destFolder.exists())
			FileUtil.delTree(destFolder)
	}
	
	@Before
	void setUp() {
		nimble = new DemoiselleNimble()
		nimble.silent = true
	}
		
	@Test
	void testApplyTemplates() {
		new File(TEST_TEMPLATES_DEST_PATH).mkdirs()
		assertTrue nimble.applyTemplates(TEST_TEMPLATES_SRC_PATH, TEST_TEMPLATES_DEST_PATH,
				[var1:'1',var11:'1.1',var2:'2',mbeanName:'MyNewMBean',pojo:'MyBean',packageName:'com.mycompany.mypackage'])
		assertEquals "root", new File(TEST_TEMPLATES_DEST_PATH + "/rootTemplate").text
		assertEquals "vm template 1", new File(TEST_TEMPLATES_DEST_PATH + "/templateFile").text
		assertEquals "a freemarker template 1", new File(TEST_TEMPLATES_DEST_PATH + "/aFreeMarkerTemplate").text
		assertEquals "2 another freemarker template", new File(TEST_TEMPLATES_DEST_PATH + "/anotherFreeMarkerTemplate").text
		assertEquals "vm template 1", new File(TEST_TEMPLATES_DEST_PATH + "/templateFile").text
		assertEquals "vm template 1", new File(TEST_TEMPLATES_DEST_PATH + "/templateFile").text
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.size() > 0
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("MyNewMBean")
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/com/mycompany/mypackage/MyNewMBean.java").text.contains("com.mycompany.mypackage")
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/com/mycompany/mypackage/MyNewMBean.java").text.contains("MyBeanMB")
		assertEquals "folder1 1", new File(TEST_TEMPLATES_DEST_PATH + "/folder1/folder1template").text
		assertEquals "folder1.1 1.1", new File(TEST_TEMPLATES_DEST_PATH + "/folder1/folder1.1/folder1.1template").text
//		assertEquals "folder2 2", new File(TEST_TEMPLATES_DEST_PATH + "/folder2/folder2template").text
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/META-INF/persistence.xml").text.contains("com.mycompany.mypackage.domain.MyBean")
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/justCopyThisFile.ext").exists()
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/com/mycompany/mypackage/SimpleFile.txt").exists()
	}
	
	@Test
	void testApplyTemplatesWithVarsFromFile() {
		def varsFile = new File(TEST_TEMPLATES_SRC_PATH  + "templateVars.txt")
		varsFile.deleteOnExit()
		varsFile << '''var1=1
		var11=1.1
		var2=2
		mbeanName=MyNewMBean
		pojo=MyBean
		packageName=com.mycompany.mypackage
		'''
		assertTrue nimble.applyTemplates(TEST_TEMPLATES_SRC_PATH, TEST_TEMPLATES_DEST_PATH, TEST_TEMPLATES_SRC_PATH  + "templateVars.txt")
		assertEquals "root", new File(TEST_TEMPLATES_DEST_PATH + "/rootTemplate").text
		assertEquals "vm template 1", new File(TEST_TEMPLATES_DEST_PATH + "/templateFile").text
		assertEquals "a freemarker template 1", new File(TEST_TEMPLATES_DEST_PATH + "/aFreeMarkerTemplate").text
		assertEquals "2 another freemarker template", new File(TEST_TEMPLATES_DEST_PATH + "/anotherFreeMarkerTemplate").text
		assertEquals "vm template 1", new File(TEST_TEMPLATES_DEST_PATH + "/templateFile").text
		assertEquals "vm template 1", new File(TEST_TEMPLATES_DEST_PATH + "/templateFile").text
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.size() > 0
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("MyNewMBean")
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/com/mycompany/mypackage/MyNewMBean.java").text.contains("com.mycompany.mypackage")
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/com/mycompany/mypackage/MyNewMBean.java").text.contains("MyBeanMB")
		assertEquals "folder1 1", new File(TEST_TEMPLATES_DEST_PATH + "/folder1/folder1template").text
		assertEquals "folder1.1 1.1", new File(TEST_TEMPLATES_DEST_PATH + "/folder1/folder1.1/folder1.1template").text
//		assertEquals "folder2 2", new File(TEST_TEMPLATES_DEST_PATH + "/folder2/folder2template").text
		assertEquals "mybean.inserted.ok=MyBean inserted ok!\np1=v1\ng1.p2=v2\nmenu.mybean=MyBeans",
				new File(TEST_TEMPLATES_DEST_PATH + "/messages.properties").text
		assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/com/mycompany/mypackage/SimpleFile.txt").exists()
	}
	
	/*@Test
	 void testApplyTemplatesAgain() {
	 assertTrue nimble.applyTemplates(TEST_TEMPLATES_SRC_PATH, TEST_TEMPLATES_DEST_PATH,
	 [var1:'1',var11:'1.1',var2:'2',mbeanName:'MyNewMBean2',mbeanOutcome:"mbean_outcome2",mbeanToViewId:"mbean_to_view_id"])
	 assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("MyNewMBean")
	 assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("MyNewMBean2")
	 assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("mbean_outcome2")
	 assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("mbean_to_view_id2")
	 }
	 @Test
	 void testApplyTemplatesAndAgain() {
	 assertTrue nimble.applyTemplates(TEST_TEMPLATES_SRC_PATH, TEST_TEMPLATES_DEST_PATH,
	 [var1:'1',var11:'1.1',var2:'2',mbeanName:'MyNewMBean2',mbeanOutcome:"mbean_outcome2",mbeanToViewId:"mbean_to_view_id"])
	 assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("MyNewMBean")
	 assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("MyNewMBean2")
	 assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("mbean_outcome2")
	 assertTrue new File(TEST_TEMPLATES_DEST_PATH + "/faces-config.xml").text.contains("mbean_to_view_id2")
	 }*/
	
	@Test
	void testApplyTemplatesWithTemplateConfFile() {
		def args = ["com.mycompany.mypackage","MyPojo"]
		assertTrue nimble.applyTemplates(TEST_CONSOLE_SRC_PATH, TEST_CONSOLE_DEST_PATH, args)
		assertEquals "packageName:com.mycompany.mypackage&pojo=MyPojo", new File(TEST_CONSOLE_DEST_PATH + "/consoleVars").text
		FileUtil.delTree(new File(TEST_CONSOLE_DEST_PATH))
	}
	
}