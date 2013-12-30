/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.frameworkdemoiselle.tools.nimble.util

import java.util.Map;

import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil;

class StringUtilTest extends GroovyTestCase{
    
    void testInsertAfter() {
        assertEquals "this is a cool! groovy string", StringUtil.insertAfter("this is a groovy string", " cool!", "is a")
    }

	void testInsertAfterFirstOccurence() {
		assertEquals "insert here groovy and not here or there", StringUtil.insertAfter("insert here and not here or there", " groovy", "here", "first")
	}
	
	void testInsertAfterLastOccurence() {
		assertEquals "not insert here and not here, but there groovy", StringUtil.insertAfter("not insert here and not here, but there", " groovy", "here", "last")
	}
	
	void testInsertBefore() {
		assertEquals "this is a cool! groovy string", StringUtil.insertBefore("this is a groovy string", " cool! ", "groovy")
	}
	
	void testInsertBeforeFirst() {
		assertEquals "this is a cool! groovy string groovy", StringUtil.insertBefore("this is a groovy string groovy", " cool! ", "groovy", "first")
	}

	void testInsertTop() {
		assertEquals "this is a cool! groovy string", StringUtil.insertTop("a cool! groovy string", "this is "	)	
	}

	void testInsertBottom() {
		assertEquals "this is a cool! groovy string", StringUtil.insertBottom("this is a cool!", " groovy string")
	}
	
	void testInsertWithTopParam() {
		assertEquals "this is a cool! groovy string", StringUtil.insert("a cool! groovy string", "this is ", "top")	
	}

	void testInsertWithBottomParam() {
		assertEquals "this is a cool! groovy string", StringUtil.insert("this is a cool!", " groovy string", "bottom")
	}

	void testInsertWithBeforeParam() {
		assertEquals "this is a cool! groovy string", StringUtil.insert("this is a groovy string", " cool! ", "before", "groovy")
	}

	void testInsertWithAfterParam() {
		assertEquals "this is a cool! groovy string", StringUtil.insert("this is a groovy string", " cool!", "after", "is a")
	}

	void testUpcaseFirstLetter() {
		assertEquals "Groovy", StringUtil.upperCaseFirstLetter("groovy")
	}	

	void testLowerCaseFirstLetter() {
		assertEquals "firstLower", StringUtil.lowerCaseFirstLetter("FirstLower")
		assertEquals "firstLowerCase", StringUtil.lowerCaseFirstLetter("FirstLowerCase")
		assertEquals "lower", StringUtil.lowerCaseFirstLetter("Lower")
	}

		
    void testConvertKeyValueStringToMap() {
        assert [p1:'v1'] == StringUtil.convertKeyValueStringToMap("p1=v1")
        assert [p1:'v1',p2:'v2'] == StringUtil.convertKeyValueStringToMap("p1=v1&p2=v2")
    }

	void testConvertMapToKeyValueString() {
		assert "p1=v1" == StringUtil.convertMapToKeyValueString([p1:'v1'])
		assert "p1=v1&p2=v2" == StringUtil.convertMapToKeyValueString([p1:'v1',p2:'v2'])
	}
	
	void testGetClassNameOfListOf(){
		def parm1 = "List<EntidadeRelacionamento>"
		assertEquals "EntidadeRelacionamento", StringUtil.getClassNameOfListOf(parm1)		
		
	}
	
	void testHasOneInList(){
		def list1 = ['Column', 'Temporal', 'OneToOne']
		def list2 = ['ManyToMany','ManyToOne','OneToMany','OneToOne']
		def list3 = ['Column', 'Temporal']
		
		assertEquals "OneToOne", StringUtil.hasOneInList(list1, list2)
		assertNull StringUtil.hasOneInList(list3, list2) 
	}
}

