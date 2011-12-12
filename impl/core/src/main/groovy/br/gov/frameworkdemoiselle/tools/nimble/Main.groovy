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

import br.gov.frameworkdemoiselle.tools.nimble.gui.wizard.tools.DemoiselleToolsWizard
import br.gov.frameworkdemoiselle.tools.nimble.logger.Log
import br.gov.frameworkdemoiselle.tools.nimble.util.FileUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil

class Main {
	
    public static void main(String[] args) {
		
		def log = Log.getInstance(Main.class)
		
        def cli = new CliBuilder(usage:"java -jar demoiselle-tools.jar")
        cli.i(longOpt: 'input', args:1, argName: 'inputPath', required: false, 'Path for templates source. Optional if using -t or --template.')
        cli.t(longOpt: 'template', args: 1, argName: 'templateName', required: false, 'Template name inside the current directory (or the subdirectory name). Optional if using -input')
        cli.o(longOpt: 'output', args: 1, argName: 'outputPath', required: false, 'Destination path. Optional. The default is <currentDir>')
        cli.v(longOpt: 'vars', args:1, argName: 'varsMapOfFileName', required: false, 'Vars list in a file (var=value in each line) or in the form var1=value1&var2=value2. Optional.')
        cli.g(longOpt: 'gui', 'Shows a window for user inputs')
        cli.h(longOpt: 'help', 'This usage help')
		
        def options = cli.parse(args)
        
//		println "args: ${args}"
//		println "options: " + options.arguments()
		
		def vars = (options.vars ? (options.vars.contains('=') ? StringUtil.convertKeyValueStringToMap(options.vars) : options.vars) : options.arguments())
		//def vars = options.vars.contains('=') ? StringUtil.convertKeyValueStringToMap(options.vars) : options.vars
//		println "vars: ${vars}"
		
        if (options.gui) {
			
			DemoiselleToolsWizard wizard = new DemoiselleToolsWizard()
			wizard.inputPath = options.input ?: null
			wizard.outputPath = options.output ?: null
			wizard.vars = vars ?: null
			wizard.start()
			
        } else {
		
            if (args.size() == 0 || options.help ||
					(!options.input && !options.template) ||
					(options.input && options.template)) {
                cli.usage()
                return
            }

            run options.input, options.template, options.output, vars
        }
    }

    private static run(input, template, output, vars) {
        def currentDir = FileUtil.getCurrentDir()
        def source = input ?: currentDir + template
        def destination = output ?: currentDir
        DemoiselleNimble nimble = new DemoiselleNimble()
        nimble.applyTemplates source, destination, vars
    }
	
}
