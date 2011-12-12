package br.gov.frameworkdemoiselle.tools.nimble.util;

import static org.junit.Assert.*;

import org.junit.Test;

import static BooleanUtil.*;

class BooleanUtilTest {

	@Test
	public void testToStringBoolean() {
		assertEquals TRUE_VALUE, toString(true)
		assertEquals FALSE_VALUE, toString(false)
		assertNull toString(null)
	}

	@Test
	public void testParseInteger() {
		assertTrue parseInteger(1)
		assertFalse parseInteger(0)
		assertNull parseInteger(-1)
		assertNull parseInteger(2)
	}

	@Test
	public void testParseString() {
		assertTrue parseString('y')
		assertTrue parseString('s')
		assertTrue parseString('true')
		assertTrue parseString('1')
		assertTrue parseString('on')
		
		assertFalse parseString('n')
		assertFalse parseString('false')
		assertFalse parseString('0')
		assertFalse parseString('off')
		
		assertNull parseString(null)
		assertNull parseString('dummy')
	}
	
	@Test
	public void testNormalizeString() {
		assertEquals TRUE_VALUE, normalizeString('y')
		assertEquals TRUE_VALUE, normalizeString('yes')
		assertEquals TRUE_VALUE, normalizeString('sim')
		assertEquals TRUE_VALUE, normalizeString('true')
		assertEquals TRUE_VALUE, normalizeString('1')
		assertEquals TRUE_VALUE, normalizeString('on')
		
		assertEquals FALSE_VALUE, normalizeString('n')
		assertEquals FALSE_VALUE, normalizeString('no')
		assertEquals FALSE_VALUE, normalizeString('não')
		assertEquals FALSE_VALUE, normalizeString('false')
		assertEquals FALSE_VALUE, normalizeString('0')
		assertEquals FALSE_VALUE, normalizeString('off')
	}

	@Test
	public void testIsValid() {
		assertTrue isValid('y')
		assertTrue isValid('yes')
		assertTrue isValid('s')
		assertTrue isValid('sim')
		assertTrue isValid('true')
		assertTrue isValid('1')
		assertTrue isValid('on')
		
		assertTrue isValid('n')
		assertTrue isValid('no')
		assertTrue isValid('não')
		assertTrue isValid('false')
		assertTrue isValid('0')
		assertTrue isValid('off')
		
		assertFalse isValid(null)
	}
	
}
