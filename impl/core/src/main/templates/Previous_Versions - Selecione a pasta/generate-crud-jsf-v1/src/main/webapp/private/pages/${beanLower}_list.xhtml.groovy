<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.RegexUtil as RU
def attrList = RU.getClassAttributesFromFile(beanJavaName, beanPath)
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax"
	xmlns:demoiselle="http://www.frameworkdemoiselle.gov.br">
<ui:composition template="/public/templates/default.xhtml">
	<ui:define name="body">
		<h:form>
			<rich:toolBar height="34" itemSeparator="line" styleClass="richPanelBodyClass">
				<rich:toolBarGroup>
					<h:outputLabel value="${pojo}" />
				</rich:toolBarGroup>
				<rich:toolBarGroup>
					<h:commandLink value="Incluir" action="#{${bean}MB.preInsert}" title="Incluir novo registro"/>
				</rich:toolBarGroup>
				<rich:toolBarGroup>
					<h:commandLink value="Listar" action="#{${bean}MB.list}" title="Listar todos os registros"/>
				</rich:toolBarGroup>
			</rich:toolBar>
			<rich:panel styleClass="richPanelStyleClass" bodyClass="richPanelBodyClass" headerClass="richPanelHeaderClass">
				<h:outputText value="Total de Registros: " />
				<h:outputText value="#{${bean}MB.list.rowCount}" />
<!-- TODO: arrumar isso
				<rich:datascroller align="center" for="disciplinaTable"
					maxPages="#{${bean}MB.maxPages}"
					page="#{${bean}MB.disciplinaPage.pageNumber}"
					reRender="disciplinaTable"
					scrollerListener="#{${bean}MB.updatePage}" />
-->
				<rich:dataTable width="100%" styleClass="richDataTableStyleClass"
					value="#{${bean}MB.list}" rows="#{${bean}MB.rows}"
					headerClass="richDataTableHeaderClass"
					rowClasses="richDataTableRowClassFirst, richDataTableRowClassSecond"
					columnClasses="richDataTableColumnClass"
					var="element" rowKeyVar="row" id="${bean}Table">
					<rich:column>
						<f:facet name="header">
							<t:outputText value=" " />
						</f:facet>
						<t:outputText styleClass="outputText" value="#{row+1}" />
					</rich:column>
				<%
				if (!attrList.isEmpty()) {
					attrList.each() { attrName, attrValue ->
						def attrLow = attrName.substring(0,1).toLowerCase() + attrName.substring(1)						
						if (!attrName.equalsIgnoreCase('id')) {
				%>
					<rich:column>
						<f:facet name="header">
							<t:outputText value="${attrName}" />
						</f:facet>
						<h:commandLink styleClass="outputText" value="#{element.${attrLow}}" action="#{${bean}MB.view}">
							<f:setPropertyActionListener target="#{${bean}MB.${bean}}" value="#{element}" />
						</h:commandLink>
					</rich:column>
						<%
						}
					}
				} else {
				%>
					<rich:column>
						<f:facet name="header">
							<t:outputText value="Text" />
						</f:facet>
						<h:commandLink styleClass="outputText" value="#{element.text}" action="#{${bean}MB.view}">
							<f:setPropertyActionListener target="#{${bean}MB.${bean}}" value="#{element}" />
						</h:commandLink>
					</rich:column>
				<%
				}
				%>
					<rich:column>
						<f:facet name="header">
							<t:outputText value="Alterar" />
						</f:facet>
						<h:commandLink action="#{${bean}MB.preUpdate}">
							<t:graphicImage value="/private/resources/images/edit.png" width="15" border="0" alt="Editar" title="Alterar registro"/>
							<f:setPropertyActionListener target="#{${bean}MB.${bean}}" value="#{element}" />
						</h:commandLink>
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<t:outputText value="Excluir" />
						</f:facet>
						<h:commandLink action="#{${bean}MB.preDelete}">
							<t:graphicImage value="/private/resources/images/delete.png" width="15" border="0" alt="Excluir" title="Excluir registro"/>
							<f:setPropertyActionListener target="#{${bean}MB.${bean}}" value="#{element}" />
						</h:commandLink>
					</rich:column>
				</rich:dataTable>
			</rich:panel>
		</h:form>
	</ui:define>
</ui:composition>
</html>