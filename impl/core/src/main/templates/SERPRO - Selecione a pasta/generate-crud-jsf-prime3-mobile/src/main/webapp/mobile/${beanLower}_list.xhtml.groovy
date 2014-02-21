<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil as SU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
def relationshipsAnnotations = ['ManyToMany','ManyToOne','OneToMany','OneToOne']
%>
<f:view xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:p="http://primefaces.org/ui"
	xmlns:pm="http://primefaces.org/mobile"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	template="/template/main_mobile.xhtml" contentType="text/html"
	renderKitId="PRIMEFACES_MOBILE">
	<pm:page title="Listagem de ${beanLower}">
	
			<!-- Main View -->
			<pm:view id="main">
			
				<pm:header title="${beanLower}" swatch="b">
					<f:facet name="right">
						<p:button value="#{messages['button.new']}" icon="plus"
										 href="#{${beanLower}ListMB.nextView}"
										 actionListener="#{${beanLower}ListMB.clear}"/>
					</f:facet>
						<f:facet name="left">
						<p:button value="#{messages['main.app.title']}" icon="home"
										 href="./navigator.jsf"/>
					</f:facet>
				</pm:header>	
				<pm:content>
					<h:form>
						<!-- Edite o data list para deixar apenas um coommandLink -->
						<p:dataList id="list" var="bean" value="#{${beanLower}ListMB.resultList}">
						<%						
						if (!attrList.isEmpty()) {
							attrList.each() { attrName, attrValue -> 
								def annotationsForAField = PU.getAnnotationsForField(tmpFile, attrName)
								def hasRelationship = SU.hasOneInList(annotationsForAField, relationshipsAnnotations)
								if (hasRelationship == null){
									def attrLow = attrName.substring(0,1).toLowerCase()+attrName.substring(1);
									if (PU.hasAnnotationForField(tmpFile, attrName, 'Id' )) {
						%>
							<h:outputText styleClass="ui-li-count" value="#{bean.${attrLow}}" />
								 <% }else{ %>							
							<h:commandLink value="#{bean.${attrLow}}" action="#{${beanLower}ListMB.getNextView}">
								<f:param name="id" value="#{bean.${idName}}" />
							</h:commandLink>
								<% 	}
								}
							}							
						} else {
						%>
							<h:commandLink value="#{bean.text}"action="#{${beanLower}ListMB.getNextView}">
								<f:param name="id" value="#{bean.id}" />
							</h:commandLink>
						<%
						}
						%>						
						</p:dataList>
						
					</h:form>
				</pm:content>
			</pm:view>
		</pm:page>
	
</f:view>