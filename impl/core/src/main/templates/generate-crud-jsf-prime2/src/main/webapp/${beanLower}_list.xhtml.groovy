<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.RegexUtil as RU
def attrList = RU.getClassAttributesFromFile(beanJavaName, beanPath)
%>
<ui:composition xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core"
	xmlns:p="http://primefaces.prime.com.tr/ui" xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets" template="/template/main.xhtml">

	<ui:define name="body">
		<h:form>
			<p:toolbar>
				<p:toolbarGroup align="left">
					<p:commandButton title="#{messages['button.new']}" image="ui-icon-document" action="#{${beanLower}ListMB.getNextView}"
						actionListener="#{${beanLower}ListMB.clear}" ajax="false" />

					<p:commandButton title="#{messages['button.delete']}" image="ui-icon-trash" onclick="confirmation.show()"
						type="button" immediate="true" ajax="false" />

					<p:confirmDialog message="#{messages['label.confirm.delete']}" showEffect="bounce" hideEffect="explode"
						header="#{messages['label.dialog.alert']}!" severity="alert" widgetVar="confirmation">

						<h:commandButton value="#{messages['button.dialog.yes']}" action="#{${beanLower}ListMB.deleteSelection}"
							actionListener="#{${beanLower}ListMB.clear}" />
						<h:commandButton value="#{messages['button.dialog.no']}" onclick="confirmation.hide()" type="button" />
					</p:confirmDialog>
				</p:toolbarGroup>
			</p:toolbar>
			<p:dataTable id="list" var="bean" value="#{${beanLower}ListMB.resultList}">
				<f:facet name="header">#{messages['${beanLower}.list.table.title']}</f:facet>
				<p:column style="width:1%;">
					<h:selectBooleanCheckbox value="#{${beanLower}ListMB.selection[bean.id]}"></h:selectBooleanCheckbox>
				</p:column>
				<%
				if (!attrList.isEmpty()) {
					attrList.each() { attrName, attrValue ->
						def attrLow = attrName.substring(0,1).toLowerCase()+attrName.substring(1);
						if (attrName.equalsIgnoreCase('id')) {	
				%>
				<p:column style="width:5%;" sortBy="#{bean.id}">
					<f:facet name="header">#{messages['${beanLower}.label.id']}</f:facet>
					<h:outputText value="#{bean.id}" />
				</p:column>
						<%
						} else {
						%>
				<p:column sortBy="#{bean.${attrLow}}">
					<f:facet name="header">#{messages['${beanLower}.label.${attrLow}']}</f:facet>
					<h:commandLink action="#{${beanLower}ListMB.getNextView}" actionListener="#{${beanLower}ListMB.clear}">
						<h:outputText value="#{bean.${attrLow}}" />
						<f:param name="id" value="#{bean.id}" />
					</h:commandLink>
				</p:column>
						<%
						}
					}
				} else {
				%>
				<p:column style="width:5%;" sortBy="#{beanLower.id}">
					<f:facet name="header">#{messages['${beanLower}.label.id']}</f:facet>
					<h:outputText value="#{bean.id}" />
				</p:column>
				<p:column sortBy="#{bean.text}">
					<f:facet name="header">#{messages['${beanLower}.label.text']}</f:facet>
					<h:commandLink action="#{${beanLower}ListMB.getNextView}" actionListener="#{${beanLower}ListMB.clear}">
						<h:outputText value="#{bean.text}" />
						<f:param name="id" value="#{bean.id}" />
					</h:commandLink>
				</p:column>
				<%
				}
				%>
			</p:dataTable>
		</h:form>

	</ui:define>
</ui:composition>