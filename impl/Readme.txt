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
                                    Organização da Implementação.

Informações para o Desenvolvedor.

  A parte de Desenvolvimento do projeto Demoiselle Nimble está divido atualmente em 5 (cinco) subprojetos:
  
  - Core: é onde está a implementação principal. Desenvolvido em linguagem Groovy,
  e contém o motor para processamento dos templates e também alguns templates padronizados 
  para alguns dos arquétipos MAVEN fornecidos pelo Demoiselle (http://demoiselle.sourceforge.net/repository/archetype-catalog.xml).
  
  - Eclipse Plugin: É o projeto para disponibilizar um plugin para o Eclipe contendo o Nimble.
  - Eclipse Feature: Projeto necessário para gerenciamento das versões do plugin para Eclipse.
  - Eclipse Update Site: Projeto para geração do Update-Site para o plugin do Eclipse.
  
  - Netbeans Plugin: É um projeto ainda inicial para disponibilizar o Nimble através de um plugin para o Netbeans.


Configuração do Ambiente de Desenvolvimento:

  Devido à algumas particularidades deste projeto, algumas configurações no ambiente são necessárias para facilitar o processo de desenvolvimento.
  Primeiramente, é claro, é preciso clonar o projeto do Git (recomendamos o GitEclipse).
  Depois disto faça a importação do Projeto para o Eclipse usando a opção Import -> Maven -> Existing Maven Projects, selecionando o repositório local.
  Serão criados os projeto de Documentação(Demoiselle-Guide-Nimble) e também o Core (Demoiselle-Nimble), por serem projetos Maven.
  
  Os outros projetos (Eclipse Plugin, Eclipse Feature e Eclipse Update Site) deverão ser importados um a um usando a opção Import -> General -> 
  Existing Projects into Workspace.  
  