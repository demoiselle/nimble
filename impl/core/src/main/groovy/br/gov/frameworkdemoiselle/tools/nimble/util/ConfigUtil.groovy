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

import br.gov.frameworkdemoiselle.tools.nimble.gui.Variable
import br.gov.frameworkdemoiselle.tools.nimble.gui.VisualComponent
import br.gov.frameworkdemoiselle.tools.nimble.logger.Log 

/**
 * Utility class for config file operations.
 */
class ConfigUtil {
	static def log = Log.getInstance(ConfigUtil.class)
	
	/**
	 * Add customVars into vars Map	
	 * @param vars
	 * @param customVars
	 * @return
	 */
	static Map loadCustomVars(Map vars, Map customVars) {
		Binding binding = new Binding()
		vars?.each { binding.setVariable(it.key, it.value) }
		GroovyShell shell = new GroovyShell(binding)
		for (cvar in customVars) {
			if (!vars.containsKey(cvar.key)) {
				try {
					cvar.value = shell.evaluate(cvar.value)
				} catch (MissingPropertyException e) {
					// let it be, let it be...
				}
				binding.setVariable(cvar.key, cvar.value)
				vars << cvar
			} else {
				log.warn "Variable named '${cvar.key}' was already defined!"
			}
		}
		return vars
	}
	
	/**
	 * Evaluates a String expression returning result
	 * @param varList
	 * @param expression
	 * @return
	 */
	static def evaluate(Map vars, String expression) {
		if (expression == null) {
			return null
		}
		Binding binding = new Binding()
		vars?.each { binding.setVariable(it.key, it.value) }
		GroovyShell shell = new GroovyShell(binding)
		def result //= false //= null
		try {
			result = shell.evaluate(expression)
		} catch (MissingPropertyException e) {
			// let it be, let it be...
			e.printStackTrace()
		}
		return result
	}
	
	/**
	 * Return all variables in a list, declared inside a Config File
	 * @param varList
	 * @param confFileName
	 * @return
	 */
	static ArrayList loadVars(ArrayList varList, String confFileName) {
		def config = getConfig(confFileName)
		varList.clear()
		config.vars.entrySet().each { var ->
			Variable v = new Variable(name:var.key)
			VisualComponent c = new VisualComponent()
			var.value.entrySet().each { property ->
				if (property.key != "component")
					v.setProperty(property.key, property.value)
				else {
					property.value.entrySet().each { compProperty ->
						c.setProperty(compProperty.key, compProperty.value)
					}
				}
			}
			v.component = c
			varList << v
		}
		return varList
	}
	
	/**
	 * Parses the given configuration file.
	 * 
	 * @param confFileName
	 * @return the corresponding ConfigObject
	 */
	static def getConfig(confFileName) {
		try {
			return new ConfigSlurper().parse(new File(confFileName).toURI().toURL())
		} catch (Exception e) {
			return null
		}
	}
	
	/**
	 * Returns a hashmap containing data read from the given "template.conf" file.
	 * 
	 * @param templatePath
	 * @return hash, if existing
	 */
	static Map getTemplateConfig(File templatePath) {
		
		String confFileName = FileUtil.addSep(templatePath.path) + "template.conf"
		String name = "", folderName = "", description = "", version = ""
		
		if (new File(confFileName).exists()) {
			def conf = getConfig(confFileName)
			name = conf.name ?: templatePath.name
			folderName = templatePath.path
			version = conf.version ?: ''
			description = conf.description ?: ''
		} else {
			name = templatePath.name
			folderName = name
		}
		
		return [name:name, folderName:folderName, description:description, version:version, path:templatePath.path]
	}
}
