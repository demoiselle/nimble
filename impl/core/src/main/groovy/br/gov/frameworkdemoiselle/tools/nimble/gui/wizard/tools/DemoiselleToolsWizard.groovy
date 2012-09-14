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
package br.gov.frameworkdemoiselle.tools.nimble.gui.wizard.tools

import java.util.List
import br.gov.frameworkdemoiselle.tools.nimble.gui.wizard.GenericWizardFrame
import br.gov.frameworkdemoiselle.tools.nimble.gui.wizard.WizardContext
import br.gov.frameworkdemoiselle.tools.nimble.util.ConfigUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.FileUtil

/**
 * 
 *  Main class for Graphical User Interface
 * 
 * @author Serge Normando Rehem
 * @author Rodrigo Hjort
 * @author Emerson Sachio Saito
 *
 */
class DemoiselleToolsWizard extends GenericWizardFrame {
	
	private ChooseTemplateStep1 step1
	private TemplateVariablesStep2 step2

	private WizardContext context = WizardContext.getDefault()
	
	String inputPath, outputPath
	List<String> vars

	DemoiselleToolsWizard() {
		step1 = new ChooseTemplateStep1()
		step2 = new TemplateVariablesStep2()
	}
	
	void start() {
		
		// check whether an input path was informed
		def skip1 = false
		if (inputPath) {
			def template = ConfigUtil.getTemplateConfig(new File(inputPath))
			if (template?.version?.length > 0) {
				context.template = template
				skip1 = true
			}
			context.inputPath = inputPath
		}
		if (!skip1)
			panels.add step1
		
		// consider using the given output path
		if (outputPath)
			context.outputPath = outputPath
		panels.add step2
		
		// forward received argument values
		if (vars)
			context.variables = vars
		
		super.start()
	}
	
	public static void main(String[] args) {
		
		DemoiselleToolsWizard wizard = new DemoiselleToolsWizard()
		wizard.outputPath = FileUtil.getCurrentDir()
		
		args.each{ s ->
			if(s.indexOf("inputPath=")>-1) {
				wizard.inputPath = s.substring(10)
			}
			if(s.indexOf("outputPath=")>-1) {
				wizard.outputPath = s.substring(11)
			}
		}		
		
		println "Wizard will start with current configuration:"
		println "- inputPath: " + wizard.inputPath
		println "- outputPath: " + wizard.outputPath
		
		wizard.start()
	}
	
}
