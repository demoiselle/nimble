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
package br.gov.frameworkdemoiselle.tools.nimble.template

import br.gov.frameworkdemoiselle.tools.nimble.util.FileUtil 
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.VelocityContext
import org.apache.velocity.Template

class VelocityTemplate extends GenericTemplate {
    static VelocityEngine engine = new VelocityEngine()

    VelocityTemplate() {
        Properties props = new Properties()
        props.put("resource.loader", "file");
        props.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader")
        props.put("file.resource.loader.path", "")
        props.put("file.resource.loader.cache", "false")
        props.put("file.resource.loader.modificationCheckInterval", "2")
        engine.init(props)
    }

    List getSupportedExt() {
        ["vsl","vm","velocity"]
    }

    String applyTemplate(String templateName, Map vars) {
        try {
            VelocityContext context = new VelocityContext()
            Template template = engine.getTemplate(new File(templateName).getCanonicalPath())
            StringWriter writer = new StringWriter()
            vars?.each { var ->
                context.put(var.key, var.value)
            }
            template.merge(context, writer)
            return writer.toString()
        } catch (Exception ex) {
            ex.printStackTrace()
        }
        return null
    }
	
    String applyStringTemplate(String templateString, Map vars) {
        try {
            VelocityContext context = new VelocityContext()
            StringWriter writer = new StringWriter()
            vars?.each { var ->
                context.put(var.key, var.value)
            }
            engine.evaluate(context, writer, "", templateString);
            //template.merge(context, writer)
            def ret = writer.toString()
			//println "RESULT: $ret"
			return ret
        } catch (Exception ex) {
            ex.printStackTrace()
        }
        return null
    }

    public static void main(String[] args) {
        VelocityTemplate vt = new VelocityTemplate()
        println FileUtil.getCurrentDir()
        println "template $args[0]"
        println vt.applyStringTemplate(args[0],[var1:'var1'])
        println "test:" + vt.applyStringTemplate('${var1}Var.java.vm',[var1:'var1'])
        //println vt.applyStringTemplate(args[1],[var1:'var1'])
        new File('.').eachFile { f ->
            //println "FNAME:" + f.name
            if (f.name.contains('$')) {
                println "FNAME:" + f.name
                println vt.applyStringTemplate(f.name,[var1:'var1'])
            }
        }
    }
}