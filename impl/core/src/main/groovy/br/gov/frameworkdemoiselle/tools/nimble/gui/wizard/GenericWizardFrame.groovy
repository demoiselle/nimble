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

package br.gov.frameworkdemoiselle.tools.nimble.gui.wizard

import java.awt.CardLayout
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.util.List
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.KeyStroke
import net.miginfocom.swing.MigLayout
import br.gov.frameworkdemoiselle.tools.nimble.gui.GenericFrame


/**
 * 
 * @author Serge Normando Rehem
 *
 */
class GenericWizardFrame extends GenericFrame {
	
	private def panels = []
	private JPanel cards
	
	protected int currentPanelIndex
	private WizardPanel currentPanel
	
	private JButton backButton
	private JButton nextButton
	private JButton cancelButton
	private JButton finishButton
	
	GenericWizardFrame() {
	}
	
	void start() {
		for (WizardPanel panel : getPanels())
			cards.add(panel, panel.getTitle())
		setCurrentPanel(0)
		setVisible(true)
	}
	
	String getTitle() {
		currentPanel?.title
	}
	
	String getDescription() {
		currentPanel?.description
	}
	
	WizardPanel getCurrentPanel() {
		return currentPanel
	}
	
	List<JPanel> getPanels() {
		return panels
	}
	
	JPanel mainContent() {
		cards = swing.panel(layout: new CardLayout()) 
		return cards
	}
	
	JPanel footer() {
		finishButton = swing.button(id: "finishButton", "Finish", actionPerformed: { getCurrentPanel().finish() }, mnemonic: 'F')
		cancelButton = swing.button(id: "cancelButton", "Cancel", actionPerformed: { cancel() }, mnemonic: 'C')
		nextButton = swing.button(id: "nextButton", "Next >", actionPerformed: { next() }, mnemonic: 'N')
		backButton = swing.button(id: "backButton", "< Back", actionPerformed: { back() }, enabled: false, mnemonic: 'B')
		JPanel p = new JPanel(new MigLayout("", "[right][grow]", ""))
		p.add(backButton, "sgx button, span 2, align right, split")
		p.add(nextButton, "sgx button")
		p.add(cancelButton, "sgx button")
		p.add(finishButton, "sgx button")
		return p
	}
	
	void setCurrentPanel(index) {
//		println "GenericWizardFrame.setCurrentPanel(${index})"
		
		currentPanelIndex = index
		currentPanel = panels[currentPanelIndex]

		currentPanel.parentFrame = this
		currentPanel.onLoad()
		setTitle(currentPanel.getTitle())
		setDescription(currentPanel.getDescription())
		
		def count = this.getPanels().size() - 1
		backButton?.enabled = (currentPanelIndex > 0)
		nextButton?.enabled = (currentPanelIndex < count)
		finishButton?.enabled = (currentPanelIndex == count)
	}
	
	void back() {
		if (beforeBack()) {
			if (currentPanelIndex > 0) {
				setCurrentPanel(--currentPanelIndex)
				((CardLayout) cards.getLayout()).previous(cards)
				focusFirstField()
			}
		}
	}
	
	void next() {
		if (beforeNext()) {
			if (currentPanelIndex < this.getPanels().size()) {
				setCurrentPanel(++currentPanelIndex)
				((CardLayout) cards.getLayout()).next(cards)
				focusFirstField()
			}
		}
	}

	void focusFirstField() {
		currentPanel?.firstField?.requestFocusInWindow()
	}
	
	void proceed() {
		if (nextButton?.enabled) {
			next()
		} else if (finishButton?.enabled) {
			getCurrentPanel().finish()
		}
	}
	
	Boolean beforeBack() {
		return true
	}
	
	Boolean beforeNext() {
		currentPanel.beforeNext()
	}
	
	void cancel() {
		System.exit(0)
	}
	
}
