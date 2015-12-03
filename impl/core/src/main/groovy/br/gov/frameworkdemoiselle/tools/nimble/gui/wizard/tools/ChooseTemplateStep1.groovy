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

import java.awt.BorderLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.prefs.Preferences
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.JTextField
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionListener
import javax.swing.table.TableModel
import net.miginfocom.swing.MigLayout
import br.gov.frameworkdemoiselle.tools.nimble.gui.wizard.WizardContext
import br.gov.frameworkdemoiselle.tools.nimble.gui.wizard.WizardPanel
import br.gov.frameworkdemoiselle.tools.nimble.util.ConfigUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.FileUtil

/**
 * Class to select a Template
 *  
 *  
 * @author Serge Normando Rehem
 * @author Emerson Sachio Saito
 *
 */
class ChooseTemplateStep1 extends WizardPanel {
	
	final String PREF_SRC_TEMPLATE = "src";
	
	def tableData = []
	int selectedRow = -1
	
	JPanel panel
	JTextField templateSourcePathText
	JTable templatesTable
	TableModel tableModel
	
	String lastSrcPath = ""
	
	ChooseTemplateStep1() {
		prefs = Preferences.userNodeForPackage(ChooseTemplateStep1.class)
		
		title = "Demoiselle Nimble 1.2.2"
		description = "Choose Template"
		
		JPanel panel = swing.panel(layout: new MigLayout('insets 10, fill', "[pref!][grow,fill][]"), constraints:'grow') {
			def lab = label("Templates Source Folder*:", displayedMnemonic:'T');
			//templateSourcePathCombo = comboBox()
			templateSourcePathText = textField()
			templateSourcePathText.addFocusListener(new FocusAdapter() {
				void focusLost(FocusEvent e) {
					//FIXME - problemas
					//updateTemplatesTable(templateSourcePathText.text)
				}
			})
			templateSourcePathText.addKeyListener(new KeyAdapter() {
				void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						updateTable(templateSourcePathText.text)
					}
				}
			})
			lab.setLabelFor(templateSourcePathText)
			button(text: 'Browse...', constraints: 'wrap', actionPerformed: { selectTemplateSourceFolder() }, mnemonic: 'R')
			scrollPane(constraints:"span 3, grow, wrap") {
				templatesTable = table(id:"templatesTable",autoCreateColumnsFromModel:false, rowSelectionAllowed:true, selectionMode:ListSelectionModel.SINGLE_SELECTION) {
					current.selectionModel.addListSelectionListener({e ->
						selectedRow = templatesTable.getSelectedRow()
						templateDescriptionLabel.setText(tableData.template[selectedRow].description)
					} as ListSelectionListener)
					tableModel = tableModel(list : tableData) {
						swing.templatesTable.addColumn( propertyColumn( header:'Name', propertyName:'name', modelIndex: 0, editable: false ) )
						swing.templatesTable.addColumn( propertyColumn( header:'Version', propertyName:'version', modelIndex: 1, minWidth:70, maxWidth:70, editable: false ) )
					}
				}
			}
			templatesTable.addKeyListener(new KeyAdapter() {
				void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						parentFrame?.proceed()
					}
				}
			})
			panel(constraints:"span 4, wrap") { 
				label(id:"templateDescriptionLabel", text=" ")
			}
		}
		add(panel, BorderLayout.CENTER)
	}
	
	void onLoad() {
		firstField = templateSourcePathText
		loadPreferences()
		updateTemplatesTable(templateSourcePathText.text)
	}
	
	void selectTemplateSourceFolder() {
		String dir = (new File(templateSourcePathText.text).isDirectory()) ? templateSourcePathText.text : FileUtil.getCurrentDir()
		String srcFolder = FileUtil.chooseDir(this, "Select templates source folder", dir);
		if (srcFolder != null) {
			//templateSourcePathCombo.addItem(srcFolder);
			templateSourcePathText.setText(srcFolder)
			updateTemplatesTable(srcFolder)
		}
	}
	
	void updateTemplatesTable(String srcFolder) {
		
		if (srcFolder == lastSrcPath) return
		
		tableData.clear()
		selectedRow = -1
		
		if (new File(srcFolder).isDirectory()) {
			new File(srcFolder).eachFile { file ->
				if (file.isDirectory() && !file.isHidden() && 
				!file.getPath().contains('.svn-base') && !file.getParent().contains('.svn')) {
					
					def template = ConfigUtil.getTemplateConfig(file)
					tableData << [name:template?.name, version:template?.version, template:template]
				}
			}
			
			tableData.sort { it.name }
			templatesTable.getModel().fireTableDataChanged()
			lastSrcPath = srcFolder
		}
	}
	
	Boolean beforeNext() {
		WizardContext context = WizardContext.getDefault()
		
		if (selectedRow != -1) {
			String templateVersion = templatesTable.getValueAt(selectedRow, 1);
			templateVersion = templateVersion.trim() 
			switch ( templateVersion ) {
				// without version, means that is a template folder
				case [""]:					
					context.clear()
					JOptionPane.showMessageDialog(this, "Please use Browse... Button to select a template folder",
					title, JOptionPane.WARNING_MESSAGE)
				return false
					break
				default:
					context.template = tableData.template[selectedRow]
					savePreferences()
					return true
			}
			
		} else {
			context.clear()
			JOptionPane.showMessageDialog(this, "Please select a template from the list.",
				title, JOptionPane.WARNING_MESSAGE)
			return false
		}
	}
	
	public void loadPreferences() {
		templateSourcePathText.setText(context.inputPath ?: prefs?.get(PREF_SRC_TEMPLATE, ""))
	}
	
	public void savePreferences() {
		prefs?.put(PREF_SRC_TEMPLATE, templateSourcePathText.getText())
	}
}