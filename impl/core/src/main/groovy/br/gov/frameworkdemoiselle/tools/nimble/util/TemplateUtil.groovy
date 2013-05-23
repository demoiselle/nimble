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

import br.gov.frameworkdemoiselle.tools.nimble.template.Template
import br.gov.frameworkdemoiselle.tools.nimble.template.TemplateFactory
import br.gov.frameworkdemoiselle.tools.nimble.template.VelocityTemplate

/**
 * Utility class for template operations.
 * 
 * @author Serge Normando Rehem
 * 
 */
class TemplateUtil {

	public static Boolean isTemplate(fileExt) {
		TemplateFactory.getTemplate(fileExt) != null
	}
	
	public static String applyTemplate(String templateName, vars = [:]) {
		Template template = TemplateFactory.getTemplate(FileUtil.getExt(templateName))
		return template.applyTemplate(templateName, vars)
	}
	
	public static String applyStringTemplate(String templateString, vars = [:]) {
		return new VelocityTemplate().applyStringTemplate(templateString, vars)
	}
	
	public static String mountDestFileName(String path, String templateName, Map vars) {
		
		
		String templateFileName = new File(templateName).name
		
		int p = path.indexOf('$')
		if (p > 0) {
			def destPath = applyStringTemplate(path, vars) ?: path
			def parentFolderName = destPath.substring(0, p)
			def folderName = destPath.substring(p)
			if (folderName.contains('.')) {
				path = parentFolderName + folderName.replace('.', '/')
			} else {
				path = parentFolderName + folderName
			}
		}
		String destFileName = FileUtil.addSep(path)
		
		new File(destFileName).mkdirs()
		
		if (templateFileName.contains('$')) {
			templateFileName = applyStringTemplate(templateFileName, vars)
		}
		
		destFileName += templateFileName
//		destFileName += FileUtil.removeExt(templateFileName)
		
		return destFileName
	}
}