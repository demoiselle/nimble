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

import org.apache.commons.io.FileUtils

import java.awt.Component
import java.util.Properties
import javax.swing.JFileChooser

/**
 * Utility class for file operations.
 */
class FileUtil {
	
	/**
	 * Adds file separator to the end of a path, if it does not exist.
	 */
	static String addSep(String path) {
		def fSeparator = File.separator
		// for windows so
		if (fSeparator.contains('\\')) {
			fSeparator = '/'
		}
		
		return path + (!path.endsWith(fSeparator) ? fSeparator : "")
	}

	/**
	 * Removes the file separator from the end of a path, if it exists.
	 */
	static String removeSep(String path) {
		def fSeparator = File.separator
		// for windows so
		if (fSeparator.contains('\\')) {
			fSeparator = '/'
		}
		return path.endsWith(fSeparator) ? path[0..-2]: path
	}

	/**
	 * Gets the current directory.
	 */
	static String getCurrentDir() {
		new File('').canonicalPath + File.separator
	}

	/**
	 * Gets the extension of a file name (ex: file.ext => ext)
	 */
	static String getExt(String fileName) {
		int p = fileName.lastIndexOf(".")
		return p>0 ? fileName.substring(p + 1) : ""
	}

	/**
	 * Gets a file name without its extension (ex: fileName.ext => fileName).
	 */
	static String removeExt(String fileName) {
		int p = fileName.lastIndexOf(".")
		return p>0 ? fileName.substring(0,p) : fileName
	}

	/**
	 * Copies a file from a source to a destination path.
	 */
	static String copy(String fromFile, String toFile) {
		def from = new File(fromFile)
		def to = new File(toFile)
		try {
			//if (toFile.endsWith(File.separator)) {
			if (to.directory) {
				FileUtils.copyFileToDirectory from, to
				return addSep(to.path) + from.name
			} else {
				FileUtils.copyFile from, to
				return to.path
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null
		}
	}
	
	/**
	 * Converts a .properties file into a Map.
	 */
	static Map convertPropsToMap(String fileName) {
		def props = new Properties()
		new File(fileName).withInputStream {
			stream -> props.load(stream)
		}
		return new HashMap<String, String>((Map) props)
	}

	/**
	 * Recursively removes a directory hierarchy.
	 * 
	 * @param file	a file or directory path
	 */
	static void delTree(File file) throws IOException {
		if (file.isDirectory()) {
			for (File c : file.listFiles())
				delTree(c)
		}
		if (!file.delete())
			throw new FileNotFoundException("Failed to delete file or directory: " + file)
	}
	
	static String chooseDir(Component parent, String title, String currentDirectoryPath) {
		final JFileChooser fc = new JFileChooser(currentDirectoryPath)
		fc.setDialogTitle(title)
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
		if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile().getPath()
		else
			return null
	}

	static String chooseFile(Component parent, String title, String currentDirectoryPath) {
		final JFileChooser fc = new JFileChooser(currentDirectoryPath)
		fc.setDialogTitle(title)
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY)
		if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile().getPath()
		else
			return null
	}

	/**
	 * Used to transform paths like \\dir1\\dir2 (i.e ms-windows so) to Java pattern package.
	 * 
	 * @param str
	 * @return
	 */
	static String toJavaPath(str) {
		String ret = str
		if (str.contains('\\')) {
			ret = str.replace('\\', '/')
		}
		return ret
	}
}