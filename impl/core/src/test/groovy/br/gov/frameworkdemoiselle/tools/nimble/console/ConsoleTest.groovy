package br.gov.frameworkdemoiselle.tools.nimble.console;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import br.gov.frameworkdemoiselle.tools.nimble.console.Console;

class ConsoleTest {

	@Test
	void testEmptyNotRequired() {
		assertEquals '', Console.validateInput(null, false)
	}

	@Test
	void testNotEmptyNotRequired() {
		assertEquals 'abc', Console.validateInput('abc', false)
	}

	@Test
	void testNotEmptyRequired() {
		assertEquals 'abc', Console.validateInput('abc', true)
	}

	@Test
	void testEmptyRequired() {
		assertNull Console.validateInput(null, true)
	}

	// with options
	
	@Test
	void testEmptyNotRequiredOptions() {
		assertEquals '', Console.validateInput(null, false, ['Y', 'N'])
	}

	@Test
	void testNotEmptyNotRequiredOptionsInvalid() {
		assertNull Console.validateInput('abc', false, ['Y', 'N'])
	}

	@Test
	void testNotEmptyNotRequiredOptionsValid() {
		assertEquals 'Y', Console.validateInput('Y', false, ['Y', 'N'])
	}

	@Test
	void testNotEmptyRequiredOptionsInvalid() {
		assertNull Console.validateInput('abc', true, ['Y', 'N'])
	}

	@Test
	void testNotEmptyRequiredOptionsValid() {
		assertEquals 'Y', Console.validateInput('Y', true, ['Y', 'N'])
	}

	@Test
	void testEmptyRequiredOptions() {
		assertNull Console.validateInput(null, true, ['Y', 'N'])
	}

	@Test
	void testNotEmptyNotRequiredOptionsValidLower() {
		assertEquals 'Y', Console.validateInput('y', false, ['Y', 'N'])
	}

	@Test
	void testEmptyRequiredOptionsDefaulted() {
		assertEquals 'N', Console.validateInput(null, true, ['Y', 'N'], 'N')
	}

	@Test
	void testEmptyNotRequiredOptionsDefaulted() {
		assertEquals 'N', Console.validateInput(null, false, ['Y', 'N'], 'N')
	}

	// with default
	
	@Test
	void testEmptyNotRequiredDefaulted() {
		assertEquals 'foo', Console.validateInput(null, false, null, 'foo')
	}

	@Test
	void testNotEmptyNotRequiredDefaulted() {
		assertEquals 'abc', Console.validateInput('abc', false, null, 'foo')
	}

	@Test
	void testNotEmptyRequiredDefaulted() {
		assertEquals 'abc', Console.validateInput('abc', true, null, 'foo')
	}

	@Test
	void testEmptyRequiredDefaulted() {
		assertEquals 'foo', Console.validateInput(null, true, null, 'foo')
	}

	// with regular expression

	@Test
	void testEmptyNotRequiredRegExp() {
		assertEquals '', Console.validateInput(null, false, null, null, '^a.c$')
	}

	@Test
	void testNotEmptyNotRequiredRegExpInvalid() {
		assertNull Console.validateInput('xyz', false, null, null, '^a.c$')
	}

	@Test
	void testNotEmptyNotRequiredRegExpValid() {
		assertEquals 'abc', Console.validateInput('abc', false, null, null, '^a.c$')
	}

	@Test
	void testNotEmptyRequiredRegExpInvalid() {
		assertNull Console.validateInput('xyz', true, null, null, '^a.c$')
	}

	@Test
	void testNotEmptyRequiredRegExpValid() {
		assertEquals 'abc', Console.validateInput('abc', true, null, null, '^a.c$')
	}

	@Test
	void testEmptyRequiredRegExp() {
		assertNull Console.validateInput(null, true, null, null, '^a.c$')
	}
	
}
