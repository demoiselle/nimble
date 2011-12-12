package br.gov.frameworkdemoiselle.tools.nimble.util

import br.gov.frameworkdemoiselle.tools.nimble.util.RegexUtil;


public class RegexUtilTest extends GroovyTestCase {
	
	void testGetClassAttributes() {
		def code = '''
		class A {
		
			public String getA() {
				return 'A'
			}
		
			private Long getBLong() { return 1L	}
		
			static int getCint()
			{
				return 1L
			}
		
			private static boolean getDBoolean()
			{
				return false
			}
		
			private static boolean isEBoolean()
			{
				return true
			}
				
			void toString() {
			}
		}
		'''
		def expected = ['A':'String','BLong':'Long','Cint':'int','DBoolean':'boolean','EBoolean':'boolean'] 
		
		assert expected == RegexUtil.getClassAttributes(code)
	}
	
	void testGetClassAttributesFromFile() {
		def code = '''
		class A {
			String a
			Long b
		
			public String getA() {
				return 'A'
			}
		
			private Long getBLong() { return 1L	}
		
			static int getCint()
			{
				return 1L
			}
		
			private static boolean getDBoolean()
			{
				return false
			}
		
			void toString() {
			}
		}
		'''
		
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		def tmpFile = new File("./src/test/temp/class.txt")
		tmpFile << code
		
		def expected = ['A':'String','BLong':'Long','Cint':'int','DBoolean':'boolean']
		
		assert expected == RegexUtil.getClassAttributesFromFile(tmpFile.path)
	}
}