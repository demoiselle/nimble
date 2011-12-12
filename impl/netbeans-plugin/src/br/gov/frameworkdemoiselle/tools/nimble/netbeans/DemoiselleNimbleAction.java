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
package br.gov.frameworkdemoiselle.tools.nimble.netbeans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JOptionPane;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Tools",
id = "br.gov.frameworkdemoiselle.tools.nimble.netbeans.DemoiselleNimbleAction")
@ActionRegistration(iconBase = "br/gov/frameworkdemoiselle/tools/nimble/netbeans/demoiselleIcon.png",
displayName = "#CTL_DemoiselleNimbleAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 1250, separatorBefore = 1225),
    @ActionReference(path = "Toolbars/Build", position = 0)
})
@Messages("CTL_DemoiselleNimbleAction=Demoiselle Nimble")
public final class DemoiselleNimbleAction implements ActionListener {

    private final Project context;

    public DemoiselleNimbleAction(Project context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        executeNimble();
        //TODO: Se o retorno foi ok, executar um refresh no projeto selecionado
    }

    public final Integer executeNimble() {
        Callable<Process> processCallable = new Callable<Process>() {
            @Override
            public Process call() throws IOException {
                ProcessBuilder processBuilder = null;
                if (SystemUtil.isWindows()){
                    processBuilder = new ProcessBuilder("cmd", "/c", "demoiselle", "-g", "-o", context.getProjectDirectory().getPath());
                }else{
                    processBuilder = new ProcessBuilder("demoiselle", "-g", "-o", context.getProjectDirectory().getPath());
                }
                
                return processBuilder.start();
            }
        };

        ExecutionDescriptor descriptor = new ExecutionDescriptor()
                .frontWindow(true).controllable(true);

        ExecutionService service = ExecutionService.newService(processCallable,
                descriptor, "ls command");

        Future<Integer> task = service.run();
        try {
            return task.get();
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "InterruptedException");
            //Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            JOptionPane.showMessageDialog(null, "You should first install Demoiselle Nimble.\nPlease see http://demoiselle.sf.net/tools/nimble", "Demoiselle Nimble", JOptionPane.ERROR_MESSAGE);
            //Exceptions.printStackTrace(ex);
        } finally {
            return null;
        }
    }
}
