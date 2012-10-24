~~ Demoiselle Framework
~~ Copyright (C) 2010 SERPRO
~~ ============================================================================
~~ This file is part of Demoiselle Framework.
~~ 
~~ Demoiselle Framework is free software; you can redistribute it and/or
~~ modify it under the terms of the GNU Lesser General Public License version 3
~~ as published by the Free Software Foundation.
~~ 
~~ This program is distributed in the hope that it will be useful,
~~ but WITHOUT ANY WARRANTY; without even the implied warranty of
~~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
~~ GNU General Public License for more details.
~~ 
~~ You should have received a copy of the GNU Lesser General Public License version 3
~~ along with this program; if not,  see <http://www.gnu.org/licenses/>
~~ or write to the Free Software Foundation, Inc., 51 Franklin Street,
~~ Fifth Floor, Boston, MA  02110-1301, USA.
~~ ============================================================================
~~ Este arquivo é parte do Framework Demoiselle.
~~ 
~~ O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
~~ modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
~~ do Software Livre (FSF).
~~ 
~~ Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
~~ GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
~~ APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
~~ para maiores detalhes.
~~ 
~~ Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
~~ "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
~~ ou escreva para a Fundação do Software Livre (FSF) Inc.,
~~ 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.

                                    ------
                                    Sobre o Demoiselle Nimble

Sobre o Demoiselle Nimble

  O Demoiselle Nimble é um processador automatizado de templates. Ele é parte do projeto Tools que tem como objetivo fornecer ferramentas de apoio
  ao desenvolvedor que utiliza o Framework Demoiselle. Um processador de templates (i.e., template processor, também conhecido como template engine 
  ou template parser) é um software ou um componente de software projetado para combinar um ou mais templates com um modelo de dados a fim de produzir
  um ou mais documentos resultantes, podendo estes serem páginas web, arquivos de texto ou códigos fontes. 
  No caso específico do Demoiselle Nimble, no template são definidas estruturas de arquivos e scripts de transformação utilizando linguagens como Velocity
  (http://velocity.apache.org/) e Groovy (http://groovy.codehaus.org/).
  Uma vez iniciado o processamento, este faz uso de variáveis cujos valores foram definidos pelo usuário e que permitem com que os diretórios e arquivos 
  resultantes possam ser dinamicamente criados e ou modificados.  
  

Informações para o Desenvolvedor.

  A estrutura do projeto contém as pastas:
  
  - Impl: abriga os projetos de implementação.
  
  - Documentation: Documentação em formato Docbook.
  
  - Site: Informações do projeto em formato Maven (.apt).


Configuração do Ambiente de Desenvolvimento:

  Devido à algumas particularidades deste projeto, algumas configurações no ambiente são necessárias para facilitar o processo de desenvolvimento.
  Primeiramente, é claro, é preciso clonar o projeto do Git (recomendamos o GitEclipse).
  Depois disto faça a importação do Projeto para o Eclipse usando a opção Import -> Maven -> Existing Maven Projects, selecionando o repositório local.
  Serão criados os projeto de Documentação(Demoiselle-Guide-Nimble) e também o Core (Demoiselle-Nimble), por serem projetos Maven.
  
  Os outros projetos (Eclipse Plugin, Eclipse Feature e Eclipse Update Site) deverão ser importados um a um usando a opção Import -> General -> 
  Existing Projects into Workspace.
  
  Groovy: 
   O Demoiselle Nimble é desenvolvido utilizando a linguagem Groovy (http://groovy.codehaus.org/Portuguese+Home), para os desenvolvedores 
  que utilizam o Eclipse, recomendamos o plugin: http://groovy.codehaus.org/Eclipse+Plugin.   
  