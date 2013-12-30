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
package br.gov.frameworkdemoiselle.tools.nimble.util

import java.util.List;
import java.util.ArrayList;
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil


/**
 * Utility class for Regex (Regular Expressions) operations.
 * 
 * @author Serge Normando Rehem
 * @author Rodrigo Hjort
 * @author Emerson Sachio Saito
 *
 */

class RegexUtil {
	
	private static def map = [:]
	
	/**
	* Returns a map containing attributes name and type, obtained by
	* regex searching get methods in a given fileName
	*/
	static def getClassAttributesFromFile(String fileName) {
		try {
			map = [:]
			def file = new File(fileName)
			getClassAttributes(file.text)
			return map
		} catch(Exception e) {
			println "Error on getClassAttributes: "
			e.printStackTrace()
			return map
		}
	}	
	
	/**
	 * Returns a map containing attributes name and type, obtained by
	 * regex searching get methods in a given String
	 */
	static def getClassAttributes(String text) {
		
		def regex = [/(\w+) get(\w+\s*)\(\)\s*\{(\s*)/,/(\w+) is(\w+\s*)\(\)\s*\{(\s*)/]
		regex.each {
			def matcher = text =~ it
			matcher.each { all, type, name, ignoreIt ->
				map.put name, type
			}
		}
		return map
	}
	
			
	/**
	 *
	 * @param fileName
	 * @param path
	 *
	 * @return a map containing attributes name and type, obtained by
	 * regex searching get methods in a given fileName and path
	 * supporting extended Classes
	 */
	static def getClassAttributesFromFile(String fileName, String path) {
		
		try {
			map = [:]
			// get Attributes from original Class
			def file = new File(path+fileName)
			getClassAttributes(file.text)
			
			// get Attributes from Extended Class
			def extendedClasses = ParserUtil.getExtendedClassesFiles(file)
			for (cls in extendedClasses){
				file = new File (path+cls+".java")
				getClassAttributes(file.text)
			}
			
			return map
		} catch(Exception e) {
			println "Error on getClassAttributes: "
			e.printStackTrace()
			return map
		}
	}
	
}