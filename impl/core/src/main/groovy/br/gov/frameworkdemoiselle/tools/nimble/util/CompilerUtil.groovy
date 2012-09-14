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

import javax.tools.JavaCompiler
import javax.tools.ToolProvider
import br.gov.frameworkdemoiselle.tools.nimble.util.ReflectionUtil

/**
 *  A utility class for Java Compilation 
 * 
 * @author Emerson Sachio Saito
 *
 */
class CompilerUtil {
	
	/**
	 * 
	 * @param fileLocation A directory path where is located a file
	 * @param fileName Name of java file without extension
	 * @param PackageName The package name of java file
	 * 
	 * @return a Java class object from gived file
	 */
	
	static def getClassFromFile (String fileLocation, String fileName, String PackageName){
		
		def retClass
		def file = new File(fileLocation+fileName+".java")
		def extendedClasses = ReflectionUtil.getExtendedClassesFiles(file)
		  		
		for (cls in extendedClasses){
			doCompileJava(fileLocation, cls+".java")
			URL varUrl2= new File(fileLocation+cls+".class").toURI().toURL()
			ToolProvider.getSystemToolClassLoader().getSystemClassLoader().addURL(varUrl2)
			URL[] allLocations = new URL[1];
			allLocations[0] = varUrl2;
			ClassLoader loader = URLClassLoader.newInstance(allLocations,ToolProvider.getSystemToolClassLoader());
			Class varClass = loader.loadClass(PackageName +'.'+ cls, true);
			ToolProvider.getSystemToolClassLoader().loadClass(PackageName +'.'+ cls)
		}		
		int compileResult = doCompileJava(fileLocation, fileName+".java") 
		if (compileResult == 0) {
			URL varUrl2= new File(fileLocation+fileName+".class").toURI().toURL()
			ToolProvider.getSystemToolClassLoader().getSystemClassLoader().addURL(varUrl2)
			URL[] allLocations = new URL[1];
			allLocations[0] = varUrl2;
			ClassLoader loader = URLClassLoader.newInstance(allLocations,ToolProvider.getSystemToolClassLoader());
			Class varClass = loader.loadClass(PackageName +'.'+ fileName, true);
			retClass = varClass
		
		}
		return retClass
	}
	

	/**
	 * 
	 * @param fileLocation A directory path where is located a file
	 * @param fileName Name of java file without extension
	 * 
	 * @return 0 if ok 
	 */
	static int doCompileJava (String fileLocation, String fileName){
				
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler()
		int result = 0
		return result = compiler.run(null, null, null, fileLocation + fileName)		
	} 
}