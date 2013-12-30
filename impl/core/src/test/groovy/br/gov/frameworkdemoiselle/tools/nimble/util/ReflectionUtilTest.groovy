package br.gov.frameworkdemoiselle.tools.nimble.util

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReflectionUtilTest extends GroovyTestCase {	
	
	void testGetSuperClass() {
		List<String> listOfStrings = new ArrayList<String>()
		def expected = "java.util.AbstractList"
		assert expected == ReflectionUtil.getSuperClass(listOfStrings.getClass()).getName()
	}
	
	void testGetSuperClasses() {
	
		List<String> listOfStrings = new ArrayList<String>()
		def expected = "java.util.AbstractList"
		assert expected == ReflectionUtil.getSuperClasses(listOfStrings.getClass())[0].getName()		
	}
	
	void testGetFieldsFromClass() {
		
			List<String> listOfStrings = new ArrayList<String>()
			def expected = "serialVersionUID"
			assert expected == ReflectionUtil.getFieldsFromClass(listOfStrings.getClass())[0].getName()		
	}
}


