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
package br.gov.frameworkdemoiselle.tools.nimble.gui

import java.awt.BorderLayout 
import java.awt.Container 
import java.awt.Dimension 
import java.awt.Toolkit 
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame 
import javax.swing.JLabel 
import javax.swing.JPanel 
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.UIManager
 
import groovy.swing.SwingBuilder
import net.miginfocom.swing.MigLayout 

/**
 * 
 * @author Serge Normando Rehem
 *
 */
abstract class GenericFrame extends JFrame {
	
	boolean debug = false 
	
	// Variables
	//String title
	
	//def size = [600, 300]
	//Dimension size = new Dimension(600,300)
		
	String title
	String description
	
	//protected JFrame mainFrame
	protected JPanel mainJPanel
	protected JLabel descriptionLabel = new JLabel()
	
	GenericFrame() {
		
		try {
			// set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName())
		}
		catch (	Exception e) {
			// handle exception
		}
		
		/*
		 mainFrame = swing.frame(
		 id: 'mainFrame',
		 defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
		 lookAndFeel("system")
		 mainPanel()
		 }
		 */
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		//add mainPanel()
		
		Container contentPane = getContentPane()
		
		JPanel head = new JPanel()
		head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS))
		Box box1 = new Box(BoxLayout.X_AXIS)
		box1.add header()
		head.add box1
		Box box2 = new Box(BoxLayout.X_AXIS)
		box2.add(new JSeparator())
		head.add(box2);
		
		contentPane.add head, BorderLayout.NORTH
		
		contentPane.add mainContent(), BorderLayout.CENTER
		
		JPanel foot = new JPanel()
		foot.setLayout(new BoxLayout(foot, BoxLayout.Y_AXIS))
		Box box3 = new Box(BoxLayout.X_AXIS)
		box3.add new JSeparator()
		foot.add box3
		Box box4 = new Box(BoxLayout.X_AXIS)
		box4.add footer()
		foot.add box4
		
		contentPane.add foot, BorderLayout.SOUTH
		/*
		 mainFrame.addWindowListener(new WindowAdapter() {
		 public void windowClosing(WindowEvent e) {
		 close()
		 }
		 })
		 */
		
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel")
		
		getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close()
			}
		})
		
		// Center screen
		Toolkit tk = Toolkit.getDefaultToolkit()
		Dimension screenSize = tk.getScreenSize()
		int screenHeight = screenSize.height
		int screenWidth = screenSize.width
		setSize(new Dimension((screenWidth / 2) as int, (screenHeight / 2) as int))
		setLocation((screenWidth / 4) as int, (screenHeight / 4) as int)
		
		loadPreferences()
		
		//setVisible(true)
	}
	
	def swing = new SwingBuilder()
	
	JPanel header() {
		JPanel panel = swing.panel(layout: new MigLayout("filly"), constraints: 'north')
		panel.add(descriptionLabel)
		//swing.separator(constraints: 'north')
		return panel
	}
	
	private JLabel setDescriptionLabel(description) {
		descriptionLabel.setText("<html><strong>${description}</strong></html>")
	}
	
	void setDescription(description) {
		this.description = description
		setDescriptionLabel(description)
	}
	
	void setTitle(title) {
		this.title = title
		mainFrame.setTitle(title)
	}
	
	void hide() {
		//WindowSaver.saveSettings( )
		setVisible(false)
	}
	
	void close() {
		//WindowSaver.saveSettings( )
		System.exit(0)
	}
	
	def leftContent() { 
		null
	}
	
	abstract JPanel mainContent()
	
	def rightContent() { 
		null
	}
	
	def loadPreferences() {
	}
	
	def savePreferences() {
	}
	
	void buttonBar(JPanel panel) {
	}
	
}
