<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil as SU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)


%>
<ui:composition xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core"
	xmlns:p="http://primefaces.prime.com.tr/ui" xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets" template="/template/main.xhtml">

	<ui:define name="body">
		<h:form prependId="false">

			<p:toolbar>
				<p:toolbarGroup align="left">
					<p:commandButton value="#{messages['button.save']}" action="#{${beanLower}EditMB.insert}"
						rendered="#{!${beanLower}EditMB.updateMode}" ajax="false" />
					<p:commandButton value="#{messages['button.save']}" action="#{${beanLower}EditMB.update}"
						rendered="#{${beanLower}EditMB.updateMode}" ajax="false" />
					<p:commandButton value="#{messages['button.delete']}" onclick="confirmation.show()"
						rendered="#{${beanLower}EditMB.updateMode}" type="button" immediate="true" ajax="false" />
					<p:confirmDialog message="#{messages['label.confirm.delete']}" showEffect="bounce" hideEffect="explode"
						header="#{messages['label.dialog.delete']}" severity="alert" widgetVar="confirmation">
						<h:commandButton value="#{messages['button.dialog.yes']}" action="#{${beanLower}EditMB.delete}" immediate="true"
							ajax="false" />
						<h:commandButton value="#{messages['button.dialog.no']}" onclick="confirmation.hide()" type="button" />
					</p:confirmDialog>
				</p:toolbarGroup>
			</p:toolbar>

			<br />

			<p:fieldset legend="#{messages['${beanLower}.label']}" toggleable="true" toggleSpeed="500">
				<h:panelGrid id="fields" columns="3">
					<%
					if (attrList.isEmpty()) {
					%>
					<h:outputLabel value="#{messages['${beanLower}.label.id']}: " for="id" styleClass="text-input" />
					<h:outputText id="id" value="#{${beanLower}EditMB.bean.id}" />
					<p:message for="id" />

					<h:outputLabel value="#{messages['${beanLower}.label.text']}: " for="text" styleClass="text-input" />
					<h:inputText id="text" value="#{${beanLower}EditMB.bean.text}"
						title="#{messages['${beanLower}.alt.text']}" />
					<p:message for="text" />
					<%
					} else {
						attrList.each() { attrName, attrValue ->
							def attrLow = attrName.substring(0,1).toLowerCase()+attrName.substring(1);
							if (attrName.equalsIgnoreCase('id')) {
					%>
					<h:outputLabel value="#{messages['${beanLower}.label.id']}: " for="id" styleClass="text-input" />
					<h:outputText id="id" value="#{${beanLower}EditMB.bean.id}" />
					<p:message for="id" />
					<%
							} else {
								if (attrValue.equalsIgnoreCase('Date')) {
									%>
					<h:outputLabel value="#{messages['${beanLower}.label.${attrLow}']}: " for="${attrLow}" styleClass="text-input" />
					<p:calendar id="${attrLow}" value="#{${beanLower}EditMB.bean.${attrLow}}"
					   pattern="#{messages['label.date.pattern']}" navigator="true" showButtonPanel="true"
					   locale="#{currentLocale}" title="#{messages['${beanLower}.alt.${attrLow}']}" />
					   <p:message for="${attrLow}" />
								<%
								}else{							
					%>
					<h:outputLabel value="#{messages['${beanLower}.label.${attrLow}']}: " for="${attrLow}" styleClass="text-input" />
					<h:inputText id="${attrLow}" value="#{${beanLower}EditMB.bean.${attrLow}}"
						title="#{messages['${beanLower}.alt.${attrLow}']}" />
					<p:message for="${attrLow}" />
					<%
								}
							}
						}
					}
					%>
				</h:panelGrid>
			</p:fieldset>
		</h:form>
	</ui:define>
</ui:composition>