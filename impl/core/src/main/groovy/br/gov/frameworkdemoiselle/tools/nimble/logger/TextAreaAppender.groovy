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
package br.gov.frameworkdemoiselle.tools.nimble.logger

import javax.swing.JTextArea 
import javax.swing.SwingUtilities 
import org.apache.log4j.WriterAppender 
import org.apache.log4j.spi.LoggingEvent 


/**
* Simple example of creating a Log4j appender that will
* write to a JTextArea.
* Adapted from: http://textareaappender.zcage.com/
* 
* @author Serge Normando Rehem
*/
class TextAreaAppender extends WriterAppender {
   
   static private JTextArea jTextArea = null
   
   /** Set the target JTextArea for the logging information to appear. */
   static public void setTextArea(JTextArea jTextArea) {
	   TextAreaAppender.jTextArea = jTextArea
   }
   /**
	* Format and then append the loggingEvent to the stored
	* JTextArea.
	*/
   public void append(LoggingEvent loggingEvent) {
	   final String message = this.layout.format(loggingEvent)

	   // Append formatted message to textarea using the Swing Thread.
	   SwingUtilities.invokeLater(new Runnable() {
		   public void run() {
			   jTextArea.append(message)
		   }
	   })
   }
}