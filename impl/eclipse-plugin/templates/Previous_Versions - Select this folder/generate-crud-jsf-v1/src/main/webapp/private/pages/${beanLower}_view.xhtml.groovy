<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.RegexUtil as RU
def attrList = RU.getClassAttributesFromFile(pojoFileName)
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
				<rich:toolBarGroup rendered="#{!${bean}MB.deletion}">
					<h:commandLink value="Alterar" action="#{${bean}MB.preUpdate}" title="Alterar registro" />
				</rich:toolBarGroup>
				<rich:toolBarGroup rendered="#{!${bean}MB.deletion}">
					<h:commandLink value="Excluir" action="#{${bean}MB.preDelete}" title="Excluir registro" />
				</rich:toolBarGroup>
				<rich:toolBarGroup rendered="#{!${bean}MB.deletion}">
					<h:commandLink value="Voltar" action="#{${bean}MB.list}" title="Voltar à tela de listagem" />
				</rich:toolBarGroup>
				<rich:toolBarGroup rendered="#{${bean}MB.deletion}">
					<h:commandLink value="Confirmar" action="#{${bean}MB.delete}" title="Excluir definitivamente" />
				</rich:toolBarGroup>
				<rich:toolBarGroup rendered="#{${bean}MB.deletion}">
					<h:commandLink value="Cancelar" action="#{${bean}MB.list}" title="Voltar à tela de listagem" />
				</rich:toolBarGroup>
			</rich:toolBar>
			<rich:panel styleClass="richPanelStyleClass" bodyClass="richPanelBodyClass" headerClass="richPanelHeaderClass">
			<%
			if (!attrList.isEmpty()) {
				attrList.each() { attrName, attrValue ->
					def attrLow = attrName.substring(0,1).toLowerCase()+attrName.substring(1)
			%>
				<fieldset>
					<legend>
						<h:outputLabel value="${attrName}" />
					</legend>
					<t:outputText value="#{${bean}MB.${bean}.${attrLow}}" />
				</fieldset>
			<%
				}
			} else {
			%>
				<fieldset>
					<legend>
						<h:outputLabel value="Text" />
					</legend>
					<t:outputText value="#{${bean}MB.${bean}.text}" />
				</fieldset>
			<%
			}
			%>
			</rich:panel>
		</h:form>
	</ui:define>
</ui:composition>
</html>