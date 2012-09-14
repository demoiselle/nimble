<%
import br.gov.frameworkdemoiselle.tools.nimble.util.RegexUtil as RU
def attrList = RU.getClassAttributesFromFile(beanJavaName, beanPath)
%>
<f:view xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:p="http://primefaces.org/ui"
	xmlns:pm="http://primefaces.org/mobile"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	template="/template/main_mobile.xhtml" contentType="text/html"
	renderKitId="PRIMEFACES_MOBILE">
	<pm:page title="Edição de ${beanLower}">
	
			<!-- Main View -->
			<pm:view id="main">
			
				<pm:header title="Edição de ${beanLower}" swatch="b">
					<f:facet name="left">
						<p:button style="width:10; height:30; border:1px solid #000;" 
							value="#{messages['button.back']}" icon="back"
							href="#{${beanLower}EditMB.previousView}"/>
					</f:facet>
					<f:facet name="right">
						<p:button value="#{messages['main.app.title']}" icon="home"
									 href="./navigator.jsf"/>
					 </f:facet>
				</pm:header>
			
				<pm:content>
					<h:form>
					<%
					if (attrList.isEmpty()) {
					%>					
					<h:outputText id="id"  label="#{messages['${beanLower}.label.id']}: "
					value="#{${beanLower}EditMB.bean.id}"/>
					<p:inputText id="text"   label="#{messages['${beanLower}.label.text']}:"
					value="#{${beanLower}EditMB.bean.text}" required="true"/>
					<%
					} else {
						attrList.each() { attrName, attrValue ->
							def attrLow = attrName.substring(0,1).toLowerCase()+attrName.substring(1);
							if (attrName.equalsIgnoreCase(idName)) {
					%>					
					<h:outputText id="${attrLow}"  label="#{messages['${beanLower}.label.${attrLow}']}: "
					value="#{${beanLower}EditMB.bean.${attrLow}}"/>
					
					<%
							} else {
								if (attrValue.equalsIgnoreCase('Date')) {
							%>
							<h:outputLabel value="#{messages['${beanLower}.label.${attrLow}']}: " for="${attrLow}" styleClass="text-input" />
							<p:calendar id="${attrLow}" value="#{${beanLower}EditMB.bean.${attrLow}}"
							   pattern="#{messages['label.date.pattern']}" navigator="true" showButtonPanel="true"
							   locale="#{currentLocale}" title="#{messages['${beanLower}.alt.${attrLow}']}" />							   
						<%}else{%>
					<p:inputText id="${attrLow}"  label="#{messages['${beanLower}.label.${attrLow}']}: "
						value="#{${beanLower}EditMB.bean.${attrLow}}" required="true"/>					
					<%			
								}
							}
						}
					}
					%>					
					<pm:buttonGroup orientation="horizontal" >
						<p:commandButton value="#{messages['button.save']}"
							action="#{${beanLower}EditMB.update}" icon="check"
							iconPosition="center" />
						<p:commandButton value="#{messages['button.delete']}"
							action="#{${beanLower}EditMB.delete}"
							rendered="#{${beanLower}EditMB.updateMode}" icon="delete"
							iconPosition="center" />
					</pm:buttonGroup>
										
					</h:form>
				</pm:content>
			</pm:view>
		</pm:page>
	
</f:view>