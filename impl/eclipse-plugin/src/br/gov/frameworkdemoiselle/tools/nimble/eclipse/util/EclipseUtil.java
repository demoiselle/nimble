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
package br.gov.frameworkdemoiselle.tools.nimble.eclipse.util;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

import br.gov.frameworkdemoiselle.tools.nimble.eclipse.Activator;


@SuppressWarnings("restriction")
public class EclipseUtil {

	public static NimbleConfiguration createNimbleConfiguration(IProject selectedProject) {
		NimbleConfiguration config = new NimbleConfiguration();
		config.setInput(getPluginPath("/templates"));
		config.setOutput(EclipseUtil.getSelectionPath(selectedProject));	
		return config;
	}
	
	public static String getPluginPath(String path) {
		String result = null;
		try {
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			URL url = FileLocator.toFileURL(FileLocator.find(bundle,new Path(path),null));
			result = new java.io.File(url.getFile()).getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void refreshWorkspace() throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProgressMonitor monitor = null;
		IProject[] projects = workspace.getRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			refreshProject(projects[i], monitor);
		}
	}
	
	/**
	 * Look for an selected project, if there is no one it uses de workspace loc.
	 * @param selectedProject 
	 * @return
	 */
	public static String getSelectionPath(IProject selectedProject) {
		String path = null;
		if(selectedProject==null) {
			path=getActivedWorkspacePath();
		}else {		
			path=selectedProject.getLocation().toOSString();
		}
		return path;
	}
	
	public static String getActivedWorkspacePath() {
		String path = null;		
		try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			path = workspace.getRoot().getLocation().toOSString();
		}catch (Exception e) {}
	
		return path;
	}
	
	public static void refreshProject(IProject project) throws CoreException {
		refreshProject(project, null);//getMonitor());
	}
	
	public static void refreshProject(IProject project, IProgressMonitor monitor) throws CoreException {
		System.out.println("Refreshing " + project.getName());
		project.refreshLocal(File.DEPTH_INFINITE, monitor);
	}

	public static IProgressMonitor getMonitor(Shell shell) {
		ProgressMonitorPart pmp = new ProgressMonitorPart(shell, null);
		return pmp;
	}
	
	
	
	public static IProject getSelected(ISelection selection) {
		IProject project =null;
		if (selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it
					.hasNext();) {
				Object element = it.next();
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element)
							.getAdapter(IProject.class);
				}
			}
		}
		return project;
	}
	

}
