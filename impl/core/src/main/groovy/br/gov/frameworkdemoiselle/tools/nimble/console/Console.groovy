/*
  Demoiselle Framework
 Copyright (C) 2011 SERPRO
 ============================================================================
 This file is part of Demoiselle Framework.
 
 Demoiselle Framework is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License version 3
 as published by the Free Software Foundation.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public License version 3
 along with this program; if not,  see <http://www.gnu.org/licenses/>
 or write to the Free Software Foundation, Inc., 51 Franklin Street,
 Fifth Floor, Boston, MA  02110-1301, USA.
 ============================================================================
 Este arquivo é parte do Framework Demoiselle.
 
 O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
 modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
 do Software Livre (FSF).
 
 Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
 GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
 APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
 para maiores detalhes.
 
 Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
 "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
 ou escreva para a Fundação do Software Livre (FSF) Inc.,
 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */
package br.gov.frameworkdemoiselle.tools.nimble.console

import br.gov.frameworkdemoiselle.tools.nimble.logger.Log
import br.gov.frameworkdemoiselle.tools.nimble.util.BooleanUtil

/**
 * 
 * @author Serge Normando Rehem
 *
 */
class Console {
	
	static def read = System.in.newReader().&readLine 
	
	static def REGEXP_VALID_NAME = /^[A-Za-z0-9][A-Za-z0-9._-]+$/
	
	static String readLine(String prompt = null) {
		return readLine(prompt, [], null, true)
	}
	
	static String readLine(String prompt = null, ArrayList options) {
		return readLine(prompt, options, null, true)
	}
	
	static String readLine(String prompt = null, ArrayList options,
			String defaultValue, Boolean required = true, String regularExpression = null) {
		
		while (true) {
			if (prompt != null) {
				def log = Log.getInstance(Console.class)
				print prompt + (defaultValue ? " [$defaultValue]" : "") + ": "
			}
			
			String line = read()
			String value = validateInput(line, required, options, defaultValue, regularExpression)
			
			if (value)
				return value
		}
	}
	
	static String readLineBoolean(String prompt = null, String defaultValue = null) {
		while (true) {
			if (prompt != null) {
				def log = Log.getInstance(Console.class)
				print prompt + (defaultValue ? " [$defaultValue]" : "") + ": "
			}
			
			String line = read()
			if (line && BooleanUtil.isValid(line))
				return BooleanUtil.normalizeString(line)
			else if (!line && defaultValue)
				return BooleanUtil.normalizeString(defaultValue)
		}
		return null
	}
	
	static String validateInput(String value, Boolean required,
		ArrayList options = null, String defaultValue = null, String regularExpression = null) {

		if (options && !options.isEmpty()) {
			if (value) {
				String valueUp = value.toUpperCase()
				return (valueUp in options) ? valueUp : null
			} else {
				return defaultValue ?: (required ? null : '')
			}
		} else {
			if (value && regularExpression) {
				return (value ==~ regularExpression) ? value : null
			}
			if (!defaultValue) {
				return required ? value : (value ?: '')
			} else {
				return value ?: defaultValue
			}
		}
	}

	static main(args) {

		def agree = Console.readLineBoolean('Do you agree? (y/n)')
		println "Line 1: $agree"

		def agree2 = Console.readLineBoolean('Do you really agree? (y/N)', 'N')
		println "Line 1: $agree2"

		return
		
		print "Type something: "
		def line1 = Console.readLine() 
		println "Line 1: $line1"
		
		def line2 = Console.readLine("Type your name")
		println "Line 2: $line2"
		
		def line3 = Console.readLine("Confirma (s/N)?", ["S", "N"], "N")
		println "Line 3: $line3"
		
		def line4 = Console.readLine("Type a valid project name", null, null, true, REGEXP_VALID_NAME)
		println "Line 4: $line4"
		
		def line5 = Console.readLine("Version", null, "1.0.0-SNAPSHOT", true, REGEXP_VALID_NAME)
		println "Line 5: $line5"
	}
	
}