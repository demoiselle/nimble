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
 * Utilitary class reserved for handling boolean values.
 */
class BooleanUtil {
	
	static final String[] TRUE_OPTIONS = ['true', 'y', 's', 'on', '1', 'yes', 'sim']
	static final String[] FALSE_OPTIONS = ['false', 'n', 'off', '0', 'no', 'não']

	static final String TRUE_VALUE = TRUE_OPTIONS[0]
	static final String FALSE_VALUE = FALSE_OPTIONS[0]

	static final String CONSOLE_YES = 'Y'
	static final String CONSOLE_NO = 'N'
	static final String[] CONSOLE_OPTIONS = [CONSOLE_YES, CONSOLE_NO]
	
	static String toString(Boolean value) {
		return (value != null) ? (value ? TRUE_VALUE : FALSE_VALUE) : null
	}

	static Boolean parseInteger(Integer value) {
		return parseString(value.toString())
	}
	
	static Boolean parseString(String value) {
		if (!value)
			return null
		def val = value.toLowerCase()
		if (val in TRUE_OPTIONS)
			return true
		else if (val in FALSE_OPTIONS)
			return false
		else
			return null
	}

	static String normalizeString(String value) {
		return value ? toString(parseString(value)) : null
	}
	
	static Boolean isValid(String value) {
		return value && parseString(value) != null
	}
	
}
