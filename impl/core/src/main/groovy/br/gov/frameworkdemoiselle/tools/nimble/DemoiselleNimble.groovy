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
package br.gov.frameworkdemoiselle.tools.nimble

import java.io.File
import java.io.IOException
import java.util.Map

import javax.swing.JOptionPane

import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.runtime.ProcessGroovyMethods

import br.gov.frameworkdemoiselle.tools.nimble.console.Console
import br.gov.frameworkdemoiselle.tools.nimble.console.Message
import br.gov.frameworkdemoiselle.tools.nimble.logger.Log
import br.gov.frameworkdemoiselle.tools.nimble.template.TemplateFactory
import br.gov.frameworkdemoiselle.tools.nimble.util.ConfigUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.FileUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.SystemUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.TemplateUtil

/**
 * This class is the core of Demoiselle Template Engine.
 *
 * @author Serge Normando Rehem
 */
class DemoiselleNimble {
	static final String CONF_EXT = '.conf'
	static final String FRAG_EXT = '.fragment'

	Boolean silent = false

	class Overwrite {
		static int UNDEFINED = 0
		static ALL = 1
		static NONE = 2
	}
	int overwrite = Overwrite.UNDEFINED

	def msg = new Message()

	def log = Log.getInstance(DemoiselleNimble.class)
	def gui = false

	// Statistics
	def executions = [:]
	def templates = [:]
	def fragments = [:]
	def confFiles = [:]
	def singleFiles = [:]

	DemoiselleNimble(gui = false) {
		this.gui = gui
	}

	Boolean applyTemplates(String templateSrcPath, String templateDestPath, String varsFile) {
		try {
			return applyTemplates(templateSrcPath, templateDestPath, FileUtil.convertPropsToMap(varsFile))
		} catch (Exception ex) {
			return false;
		}

	}

	Boolean applyTemplates(String templateSrcPath, String templateDestPath, ArrayList args) {
		def map = [:]

		try {
			// read settings on "template.conf"
			def conf = ConfigUtil.getConfig(templateSrcPath + "/template" + CONF_EXT)
			def vars = conf.vars
			//println "vars: $vars"

			vars?.eachWithIndex() { var, i ->
				//println "var: $var, i: $i"

				if (args[i])
					map[var.key] = args[i]
				else if (var.value) {

					// boolean variables deserve a special handling! :D
					if (var.value.dataType && "boolean".equalsIgnoreCase(var.value.dataType)) {
						map[var.key] = Console.readLineBoolean(
								"${var.value?.label}? (y/n)", var.value.defaultValue)

						// remainder types are treated like strings
					} else {
						def required = var.value.required ?: true
						map[var.key] = Console.readLine(
								var.value.label, [], var.value.defaultValue ?: null,
								required, Console.REGEXP_VALID_NAME)
					}
				}

				//println "value: ${map[var.key]}"
			}
		} catch (Exception ex) {

			// ex: java.lang.NullPointerException: Cannot get property 'vars' on null object
			//     at org.codehaus.groovy.runtime.NullObject.getProperty(NullObject.java:56)

			// TODO: implementar tratamento de exceÃ§Ã£o adequado
			ex.printStackTrace()

			// nÃ£o dar continuidade no processamento!
			return false
		}

		//		println "map: $map"

		//return true
		return applyTemplates(templateSrcPath, templateDestPath, map)
	}

	private void initStatistics() {
		executions["total"] = 0
		templates["total"] = 0
		templates["generated"] = 0
		templates["overwritten"] = 0
		templates["generated"] = 0
		templates["skipped"] = 0
		fragments["total"] = 0
		fragments["skipped"] = 0
		fragments["incremented"] = 0
		fragments["copied"] = 0
		singleFiles["total"] = 0
		singleFiles["skipped"] = 0
		singleFiles["overwritten"] = 0
		singleFiles["copied"] = 0
		confFiles["total"] = 0
	}

	private void printDashes() {
		log.info "-" * 80
	}

	/**
	 * This method applies the template specified in templateSrcPath and rediects its results
	 * into templateDestPath. Optionally you can pass a map of variables to be used along with
	 * the transformation.
	 */
	Boolean applyTemplates(String templateSrcPath, String templateDestPath, Map vars) {

		templateSrcPath = FileUtil.addSep(templateSrcPath)
		templateDestPath = FileUtil.addSep(templateDestPath)

		initStatistics()
		printDashes()
		log.info msg.title
		printDashes()
		log.info msg.source(templateSrcPath)
		log.info msg.destination(templateDestPath)

		try {
			def config = ConfigUtil.getConfig(templateSrcPath + "/template" + CONF_EXT)

			// Add source and destination to variables list
			vars.put "templateSourcePath", templateSrcPath
			vars.put "templateDestPath", templateDestPath

			// force variables 'typenization' according to its data types
			config?.vars?.each { var ->
				if ("boolean".equalsIgnoreCase(var.value?.dataType?.toString())) {
					def varValue = vars.get(var.key)
					if (varValue) {
						Boolean boolValue = Boolean.valueOf(varValue)
						vars.put(var.key, boolValue)
					}
				}
			}


			// Process customized template variables
			if (config?.customVars) {
				vars = ConfigUtil.loadCustomVars(vars, config?.customVars)
			}
			printDashes()
			log.info msg.assigned_vars
			vars?.each { var ->
				log.info "- ${var.key} = ${var.value}"
			}
			printDashes()


			// Processes all executions
			printDashes()
			log.info msg.processing_exec
			printDashes()
			def execution = config?.exec
			def command = execution?.command

			if (command) {
				if (command instanceof List) {
					for (cmd in command) {
						if (executeCommand(cmd, vars, templateDestPath, config) != 0) return
					}
				} else {
					if (executeCommand(command as String, vars, templateDestPath, config) != 0) return
				}
			}

			// Processes all files except fragment
			printDashes()
			log.info msg.processing
			printDashes()
			new File(templateSrcPath).eachFileRecurse { file ->
				if (file.isFile() && !file.isHidden() &&
				!file.getPath().contains('.svn-base') && !file.getParent().contains('.svn') &&
				!file.name.toLowerCase().endsWith(CONF_EXT) &&
				!file.name.toLowerCase().contains(FRAG_EXT)) {
					log.info "FROM: " + file.path

					def conf = ConfigUtil.getConfig(file.path + CONF_EXT)
					Boolean skip = conf?.skipIfExists
					Boolean processTemplate = (conf?.condition ? ConfigUtil.evaluate(vars, conf?.condition) : true)

					if (!processTemplate) {
						log.info msg.skipped_condition
						templates["skipped"]++
					} else if (TemplateFactory.getTemplate(FileUtil.getExt(file.name.toLowerCase()))) {
						def destFolder = getDestFolder(templateSrcPath, templateDestPath, file)
						applyFileTemplate file.path, destFolder, vars, skip
						templates["total"]++
					} else if (!isConfFile(file)) {
						// Copies file only if it isn't a .conf file corresponding to another
						String destFolder = getDestFolder(templateSrcPath, templateDestPath, file)
						String destFileName = TemplateUtil.mountDestFileName(destFolder, file.name, vars)
						//						String destFileName = getDestFolder(templateSrcPath, templateDestPath, file) + file.name
						log.info "TO  : " + destFileName

						if (new File(destFileName).exists()) {
							if (skip || overwrite ==  Overwrite.NONE) {
								log.info msg.skipped
								singleFiles["skipped"]++
							} else if (overwriteDestinationFile(destFileName)) {
								//println "$file.path -> templateDestPath"
								FileUtil.copy(file.path, destFileName)
								//								FileUtil.copy(file.path, templateDestPath)
								log.info msg.overwritten
								singleFiles["overwritten"]++
							} else {
								log.info msg.skipped
								singleFiles["skipped"]++
							}
						} else {
							FileUtil.copy(file.path, destFileName)
							//							FileUtil.copy(file.path, templateDestPath)
							log.info msg.copied
							singleFiles["copied"]++
						}
						singleFiles["total"]++
					} else {
						log.info msg.skipped_conf
						confFiles["total"]++
					}
				}
			}

			// Processes all fragments
			printDashes()
			log.info msg.processing_frag
			printDashes()
			new File(templateSrcPath).eachFileRecurse { file ->
				if (file.isFile() && !file.isHidden() &&
				!file.getPath().contains('.svn-base') && !file.getParent().contains('.svn') &&
				TemplateUtil.isTemplate(FileUtil.getExt(file.name.toLowerCase())) &&
				file.name.toLowerCase().contains(FRAG_EXT)) {
					log.info "FROM: " + file.path

					def conf = ConfigUtil.getConfig(file.path + CONF_EXT)
					if (conf != null) confFiles["total"]++

					String insertPoint = conf?.insertPoint
					String insertPosition = (conf?.insertPosition ?: "after")
					String insertOcurrence = (conf?.insertOcurrence ?: "last")
					Boolean processFragment = (conf?.condition ? ConfigUtil.evaluate(vars, conf?.condition) : true)

					if (!processFragment) {
						log.info msg.skipped_condition
						fragments["skipped"]++

					} else if (insertPoint != null || insertPosition in ["top", "bottom"]) {

						String fragment = TemplateUtil.applyTemplate(file.path, vars)

						String destFolder = getDestFolder(templateSrcPath, templateDestPath, file)
						String destFile = file.name - file.name.substring(file.name.indexOf(FRAG_EXT))
						String destFileName = TemplateUtil.mountDestFileName(destFolder, destFile, vars)

						File fileToReceiveFragment = new File(destFileName)
						log.info "TO  : " + destFileName

						// TODO: criar arquivo inicial se nao existente
						if (!fileToReceiveFragment.exists()) {
							fragments["copied"]++
							// ... copiar aqui
						}
						// ...
						else if (fileToReceiveFragment.text.contains(fragment) && !(conf?.duplicateIfExists == true)) {
							log.info msg.skipped_frag
							fragments["skipped"]++
						} else {
							if (!(insertPosition in ["top", "bottom"]) && !fileToReceiveFragment.text.contains(insertPoint)) {
								insertPoint = conf?.firstInsertPoint
								insertPosition = (conf.firstInsertPosition ?: "after")
							}
							fileToReceiveFragment.text = StringUtil.insert(fileToReceiveFragment.text, fragment, insertPosition, insertPoint, insertOcurrence)
							log.info msg.incremented(insertPosition)
							fragments["incremented"]++
						}
					} else {
						log.info msg.skipped_no_insert_point
					}
					fragments["total"]++
				}
			}

			showStatistics()
			return true
		} catch (Exception ex) {
			return false
		}
		return false
	}

	/**
	 * Executes a shell instruction.
	 *
	 * @param command	the command to be issued
	 * @param vars	optional variables, for replacing purposes
	 * @param path	a directory to take place the execution
	 * @param config	the configuration object
	 *
	 * @return	0 if successful, any other value otherwise
	 */
	private int executeCommand(String command, Map vars, String path, def config) {
		vars?.each { var ->
			command = command.replaceAll("@${var.key}", "${var.value}")
		}
		//FIX-ME windows ok. MAC ?? see SystemUtil
		def commandExec = null
		if (SystemUtil.isWindows())
			commandExec = ['cmd', '/c',]+ command
		else
			commandExec = command
		log.info "Executing instruction: ${commandExec}"

		def proc = null
		def out = new StringBuilder()
		def err = new StringBuilder()
		try {
			proc = commandExec.execute(null, new File(path))
			ProcessGroovyMethods.waitForProcessOutput(proc, out, err)
		} catch (IOException e) {
			//println error?.message
			println "ERROR:"
			println e.getMessage()
			return -1
		}

		def code = proc.exitValue()
		if (code == 0) {
			log.info "\n$out"
			executions["total"]++
		} else {
			def error = config?.exec?.errors["$code"]
			def message = error?.message ?: "General failure"
			println "ERROR: ${code} - ${message}"
			println out
			println err
		}

		return code
	}

	private void showStatistics() {
		printDashes()
		log.info "Executions.... ${executions['total']}"
		log.info "Templates..... ${templates['total']} [generated=${templates['generated']}, overwritten=${templates['overwritten']}, skipped=${templates['skipped']}]"
		log.info "Single Files.. ${singleFiles['total']} [overwritten=${singleFiles['overwritten']}, copied=${singleFiles['copied']}, skipped=${singleFiles['skipped']}]"
		log.info "Fragments..... ${fragments['total']} [incremented=${fragments['incremented']}, skipped=${fragments['skipped']}, copied=${fragments['copied']}]"
		log.info "Config Files.. ${confFiles['total']}"
		log.info "ALL DONE!"
		printDashes()
	}

	private Boolean applyFileTemplate(String templateName, String templateDestPath, vars = [:], Boolean skipIfExists = false) {
		try {
			String destFileName = FileUtil.removeExt(TemplateUtil.mountDestFileName(templateDestPath, templateName, vars))
			log.info "TO  : " + destFileName

			vars.put "templateName", templateName
			vars.put "templateDestPath", templateDestPath
			vars.put "templateDestFileName", destFileName
			vars.put "templateVars", vars

			boolean apply = true, skip = false
			def destFile = new File(destFileName)
			if (destFile.exists()) {
				if (skipIfExists || overwrite ==  Overwrite.NONE) {
					log.info msg.skipped_template
					templates["skipped"]++
				} else {
					if (!overwriteDestinationFile(destFileName)) {
						log.info msg.skipped_template
						templates["skipped"]++
					} else {
						destFile.setText(TemplateUtil.applyTemplate(templateName, vars))
						log.info msg.generated
						templates["overwritten"]++
					}
				}
			} else {
				destFile.setText(TemplateUtil.applyTemplate(templateName, vars))
				log.info msg.generated
				templates["generated"]++
			}

			vars.remove "templateName"
			vars.remove "templateDestPath"
			vars.remove "templateDestFileName"
			vars.remove "templateVars"

			return true
		} catch (Exception ex) {
			ex.printStackTrace()
			throw new Exception()
		}
		return false
	}

	// TODO: refatorar código para usar método abaixo
	/*
	 private Boolean applyFragmentTemplate(String templateName, String templateDestPath, vars = [:]) {
	 try {
	 String destFileName = TemplateUtil.mountDestFileName(templateDestPath, templateName, vars)
	 log.info "TO  : " + destFileName
	 vars.put "templateName", templateName
	 vars.put "templateDestPath", templateDestPath
	 vars.put "templateDestFileName", destFileName
	 vars.put "templateVars", vars
	 println "destFileName = ${destFileName}"
	 // ...
	 vars.remove "templateName"
	 vars.remove "templateDestPath"
	 vars.remove "templateDestFileName"
	 vars.remove "templateVars"
	 return true
	 } catch (Exception ex) {
	 ex.printStackTrace()
	 }
	 return false
	 }
	 */

	private String getDestFolder(templateSrcPath, templateDestPath, file) {
		def ret = FileUtil.addSep((FileUtil.removeSep(FileUtil.toJavaPath (templateDestPath)) - FileUtil.toJavaPath(file.parent)) +
				(FileUtil.toJavaPath(file.parent) - FileUtil.removeSep(FileUtil.toJavaPath(templateSrcPath))))
		return ret
	}

	private Boolean isConfFile(file) {
		return file.path.toLowerCase().endsWith(CONF_EXT) && new File(file.path[0..-1 * CONF_EXT.size() - 1]).exists()
	}

	private Boolean overwriteDestinationFile(String destFileName) {
		if (silent) return true
		if (overwrite == Overwrite.ALL) return true
		if (overwrite == Overwrite.NONE) return false
		boolean willOverwrite = true
		def responses = ["Y", "A", "N", "T"]
		String response = ""
		if (gui) {
			def buttons = [
				"Yes",
				"Yes to All",
				"No",
				"No to All"
			]
			def result = JOptionPane.showOptionDialog(null, destFileName + "\n\n" + msg.dest_file_exists_gui,
					"Confirmation", JOptionPane.WARNING_MESSAGE, 0, null, buttons as Object[], buttons[2])
			response = responses[result]
		} else {
			response = Console.readLine(msg.dest_file_exists, responses, 'N')
		}
		if (response == "A") overwrite = Overwrite.ALL
		if (response == "T") overwrite = Overwrite.NONE
		if (response in ["N", "T"]) willOverwrite = false
		return willOverwrite
	}
}