<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil as SU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)


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
				<rich:toolBarGroup rendered="#{${bean}MB.${bean}.id != null}">
					<h:commandLink value="Salvar" action="#{${bean}MB.update}" />
				</rich:toolBarGroup>
				<rich:toolBarGroup rendered="#{${bean}MB.${bean}.id == null}">
					<h:commandLink value="Incluir" action="#{${bean}MB.insert}" actionFor="subFormId" />
				</rich:toolBarGroup>
				<rich:toolBarGroup>
					<h:commandLink value="Voltar" action="#{${bean}MB.list}" />
				</rich:toolBarGroup>
			</rich:toolBar>
			<rich:panel styleClass="richPanelStyleClass" bodyClass="richPanelBodyClass" headerClass="richPanelHeaderClass">
			<%
			if (!attrList.isEmpty()) {
				attrList.each() { attrName, attrValue ->
					def attrLow = attrName.substring(0,1).toLowerCase() + attrName.substring(1)
					int tabCount = 0
					if (!attrName.equalsIgnoreCase('id')) {
						tabCount++
			%>
			<fieldset>
				<legend>
					<h:outputLabel value="${attrLow}" for="${bean}_${attrLow}" />
				</legend>
				<t:inputText id="${bean}_${attrLow}" styleClass="inputText" value="#{${bean}MB.${bean}.${attrLow}}" tabindex="${tabCount}" />
			</fieldset>
					<%
					}
				}
			} else {
			%>
			<fieldset>
				<legend>
					<h:outputLabel value="Text" for="${bean}_text" />
				</legend>
				<t:inputText id="${bean}_text" styleClass="inputText" value="#{${bean}MB.${bean}.text}" tabindex="1" />
			</fieldset>
			<%
			}
			%>
		</rich:panel>
		</h:form>
	</ui:define>
</ui:composition>
</html>