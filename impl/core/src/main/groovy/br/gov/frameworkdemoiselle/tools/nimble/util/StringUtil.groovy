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

/**
 * 
 * Utility class reserved for String operations.
 * 
 * @author Serge Normando Rehem
 * @author Emerson Sachio Saito
 */
class StringUtil {
	
	/**
	 * Generic inserts method
	 */
	static String insert(String str, String fragment, String insertPosition = "after", String insertPoint = null, String insertOcurrence = "last") {
		String position = upperCaseFirstLetter(insertPosition)
		if (insertPosition in ["top","bottom"])
			return StringUtil."insert${position}"(str, fragment)
		else
			return StringUtil."insert${position}"(str, fragment, insertPoint, insertOcurrence)
	}
	
	/**
	 * Inserts a fragment after first or last (default) occurrence of a String (insertPoint) inside another (str)
	 */
	static String insertAfter(String str, String fragment, String insertPoint, String insertOcurrence = "last") {
		def pos
		if (insertOcurrence.equalsIgnoreCase("first")) 
		  pos =  str.indexOf(insertPoint) 
		else 
		  pos = str.lastIndexOf(insertPoint)
		
		  
		  return str.substring(0, pos + insertPoint.size()) + fragment + str.substring(pos + insertPoint.size())
	}
	
	/**
	 * Inserts a fragment before the occurrence (insertOcurrence), default=last, of a String (insertPoint) inside another (str)
	 */
	static String insertBefore(String str, String fragment, String insertPoint, String insertOcurrence= "last") {
		def pos
		if (insertOcurrence.equalsIgnoreCase("first"))
			pos = str.indexOf(insertPoint)
		else			
			pos = str.lastIndexOf(insertPoint)
		
		return str.substring(0, pos - 1) + fragment + str.substring(pos)
	}
	
		
	/**
	 * Inserts a fragment at the beggining of a given String
	 */
	static String insertTop(String str, String fragment) {
		return fragment + str;
	}
	
	/**
	 * Inserts a fragment at the end of a given String
	 */
	static String insertBottom(String str, String fragment) {
		return str + fragment;
	}
	
	/**
	 * Converts a string in the form key1=value1&key2=value2 into a Map [key1:value1,key2:value2]
	 */
	static Map convertKeyValueStringToMap(String keyValueString) {
		def map = [:]
		(keyValueString =~ /([^&=]+)=([^&]+)/).each { full, key, value ->
			map[key] = value
		}
		return map
	}
	
	/**
	* Converts a Map [key1:value1,key2:value2] into a String in the form key1=value1&key2=value2 
	*/
   static String convertMapToKeyValueString(Map map) {
	   String str = ""
	   map.each { 
		   str += ((str!="")?"&":"") + it.key + "=" + it.value 
	   }
	   return str
   }
   
	/**
	 * Uppercases the first letter of a given string
	 */
	static String upperCaseFirstLetter(String str) {
		str[0].toUpperCase() + str.substring(1)
	}
	
	
	/**
	 * Lowercases the first letter of a given string
	 */
	static String lowerCaseFirstLetter(String str) {
		str[0].toLowerCase() + str.substring(1)
	}
	
	/**
	 *  Returns the Name of Class Parameter (N) for List<N> 
	 */
	static String getClassNameOfListOf(def str) {
		
		def i = str.indexOf('<')
		def j = str.indexOf('>')
		str.substring(i+1,j)
	}
	
	/**
	 * 
	 * @param L1 a List of strings
	 * @param L2 a List of strings
	 * @return one of elements on L1 is on L2
	 */
	static String hasOneInList(List<String> L1, List<String> L2){
		//TODO melhorar
		//	def words = ['ant', 'buffalo', 'cat', 'dinosaur']
		// assert words.findAll{ w -> w.size() > 4 } == ['buffalo', 'dinosaur']
		for (String element : L1){
			if (L2.contains(element)){
				return element;
			}			
		}		
		return null
	}
}
