/*
 Demoiselle Framework
 Copyright (C) 2012 SERPRO
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

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.AnnotationDeclaration
import japa.parser.ast.body.BodyDeclaration
import japa.parser.ast.body.ClassOrInterfaceDeclaration
import japa.parser.ast.body.FieldDeclaration
import japa.parser.ast.body.VariableDeclarator
import japa.parser.ast.expr.AnnotationExpr
import japa.parser.ast.visitor.VoidVisitorAdapter

/**
 * 
 * Utility class for Java Code reflection
 *  
 *  
 * @author Emerson Sachio Saito
 *
 */
class ReflectionUtil {

	private static def fieldsList = [:]

	/**
	 * 
	 * @param paramInstanceClass
	 * @param paramAnnotation
	 * @return the field that contains given Annotation type (ex: javax.persistence.Id)
	 */
	static def getFieldWithAnnotation (Class paramInstanceClass, String paramAnnotation) {

		def List<?> fieldsList = new ArrayList<?>()
		fieldsList = getFieldsFromClass(paramInstanceClass)
		def retField

		for (eachField in fieldsList){
			if (eachField.getDeclaredAnnotations().size() > 0) {
				for (eachAnnotation in eachField.getDeclaredAnnotations()){
					if (eachAnnotation.toString().contains(paramAnnotation)){
						retField =eachField
					}
				}
			}
		}

		return retField
	}

	/**
	 *  to find declared fields for a gived Instanced Class, using reflection
	 * 
	 * @param paramInstanceClass
	 * @return a List with all declared fields for a class and it's super classes
	 */
	static def getFieldsFromClass (Class paramInstanceClass) {
		def List<?> fieldsList = new ArrayList<?>()
		def varSuperClasses = getSuperClasses(paramInstanceClass)
		
		if (paramInstanceClass.getDeclaredFields().size() > 0) {
			for (eachField in paramInstanceClass.getDeclaredFields()){
				fieldsList.add eachField
			}
		}
		
		for (scls in varSuperClasses){
			if (scls.getDeclaredFields().size() > 0) {
				for (eachField in scls.getDeclaredFields()){
					fieldsList.add eachField
				}
			}
		}
		return fieldsList
	}

	/**
	 * to get Super Classes for a gived Instanced Class, using reflection
	 * traverses the hierarchy until the class java.lang.Object
	 * 
	 * @param paramInstanceClass
	 * @return a list of all Super Classes for gived Class
	 */
	static List<?> getSuperClasses (Class paramInstanceClass){

		def List<?> varSuperClasses = new ArrayList<?>()
		Class varSuperClass = getSuperClass(paramInstanceClass)
		
		while (varSuperClass.getName() != "java.lang.Object") {
			varSuperClasses.add varSuperClass
			varSuperClass = getSuperClass(varSuperClass)
		}
		return varSuperClasses
	}

	/**
	 * to get Super Classes for a gived Instanced Class, using reflection
	 * 
	 * @param paramInstanceClass
	 * @return a Super Class 
	 */
	static Class getSuperClass (Class paramInstanceClass) {
		return paramInstanceClass.getSuperclass()
	}
}
