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
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.prefs.Preferences
import javax.swing.BorderFactory
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import net.miginfocom.swing.MigLayout
import br.gov.frameworkdemoiselle.tools.nimble.DemoiselleNimble
import br.gov.frameworkdemoiselle.tools.nimble.gui.Variable 
import br.gov.frameworkdemoiselle.tools.nimble.gui.wizard.WizardPanel 
import br.gov.frameworkdemoiselle.tools.nimble.util.BooleanUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.ConfigUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.FileUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil


/**
 *  Second step on template's variables processing by GUI.
 *   
 * @author Serge Normando Rehem
 * @author Rodrigo Hjort
 * @author Emerson Sachio Saito
 *
 */
class TemplateVariablesStep2 extends WizardPanel {
	
	final String PREF_DEST_FOLDER = "dest";
	
	def varList = []
		
	//JScrollPane panel
	JPanel panel
	JTextField outputPathText
	KeyAdapter keyHandler
	
//	public final static Cursor busyCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
//	public final static Cursor defaultCursor = Cursor.getDefaultCursor()
	
	TemplateVariablesStep2() {
		prefs = Preferences.userNodeForPackage(TemplateVariablesStep2.class)
		keyHandler = new KeyAdapter() {
			void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					parentFrame?.proceed()
				}
			}
		}
	}
	
	void onLoad() {
		if (panel != null) {
			removeAll()
			updateUI()
		}
		title = context.template?.name ?: ""
		description = context.template?.description ?: ""
		if (context.template) {
			String confFileName = FileUtil.addSep(context.template.path) + "template.conf"
			varList = ConfigUtil.loadVars(varList, confFileName)
		}
		mountPanel()
		firstField = outputPathText
	}
	
	Boolean validateForm() {
		for (Variable v: varList) {
			if (v.required) {
				if (v.component.textValue.trim().equals("")) {
					JOptionPane.showMessageDialog(this, 
						"Please fill all required fields!", title, JOptionPane.WARNING_MESSAGE)
					v.component.visualComponent.requestFocus()
					return false
				}
			}
		}
		return true
	}
	
	void finish() {
		if (validateForm()) {
			
//			getRootPane().setCursor(busyCursor)
//			this.setCursor(busyCursor);
//			Thread.sleep(5000)
//			this.setCursor(defaultCursor);
//			getRootPane().setCursor(defaultCursor)
			
			if (new File(outputPathText.text).exists()) {
				if (!new File(outputPathText.text).isDirectory()) {
					JOptionPane.showMessageDialog(this, 
						"Please inform a valid Output Folder", title, JOptionPane.WARNING_MESSAGE)
					return
				}
			} else {
				if (outputPathText.text.size() > 0) {
					def response = JOptionPane.showConfirmDialog(this,
						"Output Folder does not exist. Create it?", title, JOptionPane.OK_CANCEL_OPTION, 1)
					if (response == JOptionPane.CANCEL_OPTION)
						return
					else if (response == JOptionPane.OK_OPTION)
						new File(outputPathText.text).mkdirs()
				}else {
					JOptionPane.showMessageDialog(this,	"Please inform a valid Output Folder", title, JOptionPane.WARNING_MESSAGE)
				    return
				}
			}
			
			def vars = [:]
			varList.each { v ->
				if (v.component.textValue.equalsIgnoreCase("Click to select an entity class file...")) {
					v.component.textValue = "MyEntity"
				}
				vars.put v.name, v.component.textValue
			}
			
			DemoiselleNimble nimble = new DemoiselleNimble(true)
			boolean result = nimble.applyTemplates(context.template?.folderName, outputPathText.text, vars)
			
			savePreferences()
			
			if (result)
				JOptionPane.showMessageDialog(this,
					"Template processing was concluded successfully!", title, JOptionPane.INFORMATION_MESSAGE)
			else
				JOptionPane.showMessageDialog(this,
					"An error occurred while processing the template.", title, JOptionPane.ERROR_MESSAGE)

			System.exit(0)
		}
	}
	
	void mountPanel() {
		panel = swing.panel(border: null) {
			panel(layout: new MigLayout('insets 10, fillx', "[pref!][grow,fill][]",""), constraints:'grow') {
				
				def lof = label("1. Output Folder*:", displayedMnemonic:'1');
				lof.setLabelFor(outputPathText)
				outputPathText = textField()
				outputPathText.setText(context.outputPath)
				button(text: 'Browse...', constraints: 'wrap', mnemonic:'R', actionPerformed: { selectOutputFolder() })
				outputPathText.addKeyListener(keyHandler)
				
				varList.eachWithIndex { v, i ->
					
					def defaultValue = ""
					if (v.defaultValueClass != null) {
						def dvc = Class.forName(v.defaultValueClass).newInstance()
						defaultValue = dvc.getDefault()
					} else if (v.defaultValue != null) {
						defaultValue = v.defaultValue
					}
					def lab
					if (v.label != null){
						lab = label(text:"${i + 2}. ${v.label}${v.required ? '*' : ''}:")
						if (i < 10)
							lab.setDisplayedMnemonic(48 + i + 2)
					}
								
					
					def component
					
					// TODO: dar uma melhorada nesse trecho
					if ("comboBox".equalsIgnoreCase(v.component?.type)) {
					/*switch (v.component.type) {
						case "comboBox":*/
						def comboItems = ""
						if (v.listClass != null) {
							def lc = Class.forName(v.listClass).newInstance()
							comboItems = lc.getList()
						} else if (v.component.attributes.get("items"))
							comboItems = v.component.attributes.get("items")
						def comboEditable = v.component.attributes.get("editable") ?: false
						component = comboBox(items:comboItems, editable:comboEditable, constraints: 'span 2, wrap')
					} else if ("checkBox".equalsIgnoreCase(v.component?.type)) {
					/*		break
						case "checkBox":*/
						def checkSelected = v.component.attributes.get("selected") ?: false
						component = checkBox(text:'', selected:checkSelected, constraints: 'wrap');
					} else if ("boolean".equalsIgnoreCase(v.dataType)) {
					/*		break
					 case "boolean":*/
						Boolean checkSelected = BooleanUtil.parseString(defaultValue) ?: false
						component = checkBox(text:null, selected:checkSelected, constraints: 'wrap');
					} else if ("browserButton".equalsIgnoreCase(v.component?.type)) {
					/*		break
					 case "browserButton":*/
						String actionToPerform = v.component.attributes.get("actionToPerform")
						if (actionToPerform =="selectEntityAndAttributes") {
							component = button(text: 'Click to select an entity class file...', constraints: 'wrap', mnemonic:'W', actionPerformed: { selectEntityAndAttributes(v.component) })
						}else {
							component = button(text: 'Browse...', constraints: 'wrap', mnemonic:'W', actionPerformed: { selectFileName(v.component) })
						}
					}else {
					/*		break
						case "text":
						default:*/
						def passed = (context.variables?.size() > i ? context.variables[i] : null)
						component = textField(text:(passed ?: defaultValue), constraints: 'span 2, wrap', columns: 30)
					/*		break;
					}*/
					}
					component.visible = v.visible
					
					if (v.visible && v.label != null) {
						lab.setLabelFor(component)
					}
					
					v.component.attributes.each { key, value ->
						if (key != "editable" && key != "items" && key != "selected" && key != "actionToPerform")
							component.setProperty(key, value)
					}
					v.component.visualComponent = component
					component.addKeyListener(keyHandler)
				}
			}
		}
		panel.setBorder(BorderFactory.createEmptyBorder())
		add(panel, BorderLayout.CENTER)
	}
	
	void selectOutputFolder() {
		String dir = (new File(outputPathText.text).isDirectory()) ? outputPathText.text : FileUtil.getCurrentDir()
		String destFolder = FileUtil.chooseDir(this, "Select output folder", dir)
		if (destFolder != null) {
			outputPathText.setText(destFolder)
		}
	}
	
	/**
	 *  Select an Entity Class Name and attributes to template variables
	 * @param comp variable to receive a value
	 */
	void selectEntityAndAttributes(def comp) {
		String dir = (new File(outputPathText.text).isDirectory()) ? outputPathText.text : FileUtil.getCurrentDir()
		String entityFile = FileUtil.chooseFile(this, "Select Entity Class", dir)
		
		if (entityFile != null) {

			String entityFileName = FileUtil.removePath(entityFile)
			String entityFilePath = FileUtil.returnPath(entityFile)
			
			// verificar se é uma classe de entidade
			// verify if it is an entity class
			if (!FileUtil.hasString (entityFilePath+entityFileName,"@Entity"))
			{
				JOptionPane.showMessageDialog(this,	"The selected class isn't annotated whit @Entity!", title, JOptionPane.ERROR_MESSAGE)
				return
			}
			// verificar se existe ID para a entidade
			// verify if the entity class has a ID
			def fileSelectedEntity = new File(entityFilePath+entityFileName)
			def extendedClasses = ParserUtil.getExtendedClassesFiles(fileSelectedEntity)
			def hasId = FileUtil.hasString (entityFilePath+entityFileName,"@Id")
			if (!hasId) {
				for (cls in extendedClasses){
					hasId = FileUtil.hasString (entityFilePath+cls+".java","@Id")
				}
			}
			if (!hasId)
			{
				JOptionPane.showMessageDialog(this,	"The selected class or that extends isn't annotated whit @Id!", title, JOptionPane.ERROR_MESSAGE)
				return
			}
			def mapNameTypeID = [:]
			mapNameTypeID = FileUtil.getNameAndTypeId(entityFilePath+entityFileName)
			if (mapNameTypeID.isEmpty()) {
				for (cls in extendedClasses){
					 mapNameTypeID = FileUtil.getNameAndTypeId (entityFilePath+cls+".java")
				}
			}
			def varNameId 
			def varTypeId
			
			Set sMap = mapNameTypeID.entrySet();
			Iterator itMap = sMap.iterator();
	
			while(itMap.hasNext())
			{
				Map.Entry mEntry = (Map.Entry)itMap.next();
				if (mEntry.getKey()=='Name') {
					varNameId = mEntry.getValue()
				}
				if (mEntry.getKey()=='Type') {
					varTypeId = mEntry.getValue()
				}				
			}			

			varList.each { var ->
				if (var.name == "idName") {
					if (varNameId != null) {
						var.component.textValue = varNameId
					}				
				}
				if (var.name == "idType") {
					if (varTypeId != null) {
						var.component.textValue = varTypeId
					}
				}
				if (var.name == "packageName") {
					var.component.textValue = FileUtil.removeExt(ParserUtil.getPackageNameFromClassFile(fileSelectedEntity))
				}
			}
			
			entityFileName = FileUtil.removeExt entityFileName
			comp.textValue = entityFileName

		}
	}
	
	/**
	 * Select a file name with extension
	 * @param comp variable to receive a value
	 */
	void selectFileName(def comp) {
		String dir = FileUtil.getCurrentDir()
		String entityClass = FileUtil.chooseFileName(this, "Select a File", dir)
		if (entityClass != null) {
			comp.textValue = entityClass
		}
	}
	
	public void loadPreferences() {
		outputPathText.setText(context.outputPath ?: prefs?.get(PREF_DEST_FOLDER, ""))
		String stringVars = prefs?.get(context.template?.name, "[:]")
		def mapVars = StringUtil.convertKeyValueStringToMap(stringVars)
		for (Variable v: varList) {
			if (mapVars[v.name]!=null) {
				v.component.textValue = mapVars[v.name]
			}
			
		}
	}
	
	public void savePreferences() {
		prefs?.put(PREF_DEST_FOLDER, outputPathText.getText())
		def vars = [:]
		for (Variable v: varList) {
			vars.put v.name, v.component.textValue
		}
		prefs.put context.template?.name, StringUtil.convertMapToKeyValueString(vars)
	}
	
	public static void main(String[] args) {
		new TemplateVariablesStep2().setVisible true
	}
	
}
