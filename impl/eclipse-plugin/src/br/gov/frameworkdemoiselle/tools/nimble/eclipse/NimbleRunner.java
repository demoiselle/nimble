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
package br.gov.frameworkdemoiselle.tools.nimble.eclipse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.osgi.framework.Bundle;

import br.gov.frameworkdemoiselle.tools.nimble.eclipse.util.EclipseUtil;
import br.gov.frameworkdemoiselle.tools.nimble.eclipse.util.NimbleConfiguration;




public class NimbleRunner implements IDebugEventSetListener{

	public static final String NIMBLE_MAIN = "br.gov.frameworkdemoiselle.tools.nimble.gui.wizard.tools.DemoiselleToolsWizard";

	private IProcess[] processes;

	private String[] classPath;
	
	private List<String> workspaceInitialDirectories = new ArrayList<String>();

	public NimbleRunner() throws CoreException {
		try {
			classPath = getClasspath();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CoreException(new Status(Status.CANCEL, Activator.PLUGIN_ID,
					"Não foi possível obeter o classpath para o nimble."));
		}
	}

	public void run(NimbleConfiguration configuration) throws CoreException {
		System.out.println("Starting Demoiselle Nimble " + configuration.getArguments());
		this.workspaceInitialDirectories = getActualWorkspaceDiretories();
		performRun(configuration,null);
	}

	private void performRun(NimbleConfiguration configuration, IProgressMonitor monitor) throws CoreException {
		
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		//String mode = ILaunchManager.DEBUG_MODE;
		String mode = ILaunchManager.RUN_MODE;
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		if (vmInstall != null) {
			IVMRunner vmRunner = vmInstall.getVMRunner(mode);
			if (vmRunner != null) {				
				if (classPath != null) {
					VMRunnerConfiguration vmConfig = new VMRunnerConfiguration(NIMBLE_MAIN, classPath);
					vmConfig.setProgramArguments(configuration.getArguments());

					ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
					ILaunchConfigurationType type = manager
							.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);

					ILaunchConfigurationWorkingCopy launchWorkingCopy = type.newInstance(null, "Execute Nimble");
					launchWorkingCopy.setAttribute(IDebugUIConstants.ATTR_PRIVATE, true);
					DebugUITools.setLaunchPerspective(type,mode,IDebugUIConstants.PERSPECTIVE_DEFAULT);

					ILaunchConfiguration launchConfig = launchWorkingCopy.doSave();
					ILaunch launch = new Launch(launchConfig, mode, null);
					DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);

					vmRunner.run(vmConfig, launch, monitor);
					
					processes = launch.getProcesses();
					
					DebugPlugin.getDefault().addDebugEventListener(this);            	
					
				}
			}
		}
	}

	@Override
	/**
	 * This method is here to refresh the fresh project in the workspace as soon as maven has finished creating it.
	 *
	 * @param events An array of debug events. We are looking for a <code>DebugEvent#TERMINATE</code> event.
	 */
	public void handleDebugEvents(DebugEvent[] events) {
		for (int i = 0; i < events.length; i++) {
			DebugEvent event = events[i];
			if (ArrayUtils.contains(getProcesses(), event.getSource())) {
				if (event.getKind() == DebugEvent.TERMINATE) {
					System.out.println("Closing Demoiselle Nimble " + new Date());
					DebugPlugin.getDefault().removeDebugEventListener(this);
					try {
						verifyWorkspace();
						EclipseUtil.refreshWorkspace();
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private List<String> getActualWorkspaceDiretories(){
		List<String> directories = new ArrayList<String>();
		File root = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile();
		if(root.isDirectory()) {
			File[] fullList = root.listFiles();
			for (File file : fullList) {
				if(file.isDirectory()) {
					directories.add(file.getName());
				}
			}
		}
		return directories;
	}

	private void verifyWorkspace() {
		List<String> diretorios = getActualWorkspaceDiretories();
		for(String dir: diretorios) {
			if(!this.workspaceInitialDirectories.contains(dir)) {
				System.out.println("Adicionando o projeto: "+dir);				
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(dir);
				try {
					project.create(null);
					project.open(null);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * @return
	 */
	private IProcess[] getProcesses() {
		if (processes != null) {
			return processes;
		} else {
			return new IProcess[0];
		}
	}

	  private String[] getClasspath()
	    {
	        List<String> classpath = new ArrayList<String>();

	        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
	        Enumeration<?> mavenJars = bundle.findEntries("lib", "*.jar", true);
	        while (mavenJars.hasMoreElements())
	        {
	            URL jarFileURL = (URL)mavenJars.nextElement();
	            String absolutePath = transformToAbsolutePath(jarFileURL);
	            classpath.add(absolutePath);
	        }

	        return (String[])classpath.toArray(new String[classpath.size()]);
	    }
	  


	  private String transformToAbsolutePath(URL url)
	    {
	        String absolutePath;
	        try
	        {
	            URL transformedUrl = FileLocator.toFileURL(url);
	            File file = new File(transformedUrl.getFile());
	            absolutePath = file.getAbsolutePath();
	        }
	        catch (IOException e)
	        {
	            absolutePath = "";
	            e.printStackTrace();
	        }
	        return absolutePath;
	    }
	
}
