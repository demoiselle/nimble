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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * 
 * @author Emerson Sachio Saito
 *
 */
class RemoteUtil {

	static def getRemoteTemplateFile (String url){

		InputStream remoteFile;
		java.net.HttpURLConnection con = null;

		try {
			URL http = new URL(url);
			con = (java.net.HttpURLConnection) http.openConnection();
			con.setConnectTimeout(60 * 1000); // tempo de timeout de conexao = 20 segundos
			con.setReadTimeout(120 * 1000); // tempo de timeout de leitura = 2 minutos
			remoteFile = con.getInputStream();
		}catch (SocketTimeoutException e1){
			remoteFile =  null;
		}catch (MalformedURLException e) {
			remoteFile = null;
		} catch (IOException e) {

			remoteFile = null;
		}
		return remoteFile;
	}
}

