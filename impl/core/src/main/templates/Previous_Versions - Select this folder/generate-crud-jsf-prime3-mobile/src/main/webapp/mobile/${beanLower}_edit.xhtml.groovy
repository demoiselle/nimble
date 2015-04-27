<%
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil as SU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
def relationshipsAnnotations = ['ManyToMany','ManyToOne','OneToMany','OneToOne']
def relationshipsFields = [:]
def stringDolar = '$'

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
				<pm:buttonGroup orientation="horizontal" >
					<h:commandButton value="#{messages['button.save']}" action="#{${beanLower}EditMB.insert}"
						rendered="#{!${beanLower}EditMB.updateMode}" immediate="true" ajax="false" />
					<h:commandButton value="#{messages['button.save']}" action="#{${beanLower}EditMB.update}"
						rendered="#{${beanLower}EditMB.updateMode}" immediate="true" ajax="false" />
					<h:commandButton value="#{messages['button.delete']}" action="#{${beanLower}EditMB.delete}"
						rendered="#{${beanLower}EditMB.updateMode}" immediate="true" ajax="false" />					
				</pm:buttonGroup>
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
						  def annotationsForAField = PU.getAnnotationsForField(tmpFile, attrName)
						  String varRelationship = SU.hasOneInList(annotationsForAField, relationshipsAnnotations)
						  if (varRelationship != null && !varRelationship.isEmpty() ){
							  switch (varRelationship) {
								  case ["ManyToOne", "OneToOne"]:
								   		String varBeanAnnotated = attrValue+".java"
										def varFileBeanAnnotated = new File(beanPath+varBeanAnnotated)
										def varId = PU.getFieldAnnotatedWith(varFileBeanAnnotated, 'Id') 
								  %>
					<h:outputLabel value="#{messages['${attrName}.label']}: " for="${attrName}" styleClass="text-input" />
				    <h:selectOneMenu id="${attrName}" value="#{${beanLower}EditMB.bean.${attrName}}"  converter="Conversor${attrValue}">
						<f:selectItem itemLabel="Selecione" itemValue="" />
						<f:selectItems value="#{${beanLower}EditMB.${attrName}List}" var="var${attrValue}" itemLabel="#{var${attrValue}.${varId}}"  itemValue="#{var${attrValue}}" />						
					</h:selectOneMenu>
					<h:message for="${attrName}" />
								 <% break
								  default:
								  	relationshipsFields.put attrName.toString(),  varRelationship
									break
							  }							   
						  }
						  else{ %>
					<h:outputLabel value="#{messages['${beanLower}.label.${attrName}']}: " for="${attrName}" styleClass="text-input" />
					<%		if (PU.hasAnnotationForField(tmpFile, attrName, 'GeneratedValue' )) {%>					
					<h:outputText id="${attrName}" value="#{${beanLower}EditMB.bean.${attrName}}" />
					<%		} else {
								if (attrValue.equalsIgnoreCase('Date')) {	%>
					<p:calendar id="${attrName}" value="#{${beanLower}EditMB.bean.${attrName}}"
					   pattern="#{messages['label.date.pattern']}" navigator="true" showButtonPanel="true"
					   locale="#{locales.currentLocale}" title="#{messages['${beanLower}.alt.${attrName}']}" />
							 <% }else{
									if (PU.hasAnnotationForField(tmpFile, attrName, 'Enumerated' )) {%>
					<h:selectOneMenu id="${attrName}" value="#{${beanLower}EditMB.bean.${attrName}}">
						<f:selectItems value="#{${beanLower}EditMB.${attrName}}" />
					</h:selectOneMenu>					
								<%	}else{	%>
					<h:inputText id="${attrName}" value="#{${beanLower}EditMB.bean.${attrName}}" title="#{messages['${beanLower}.alt.${attrName}']}" />
					<%  			}
								}
							}%>
					<h:message for="${attrName}" />
						<% }
						}
					}
                   if (relationshipsFields != null && relationshipsFields.size() > 0){
					relationshipsFields.each { varField, varRelationship ->
						switch ( varRelationship.toString() ) {
							case ["OneToMany"]:
								String varListOfClassValue = PU.getFieldValue(tmpFile,varField)
								String attrClassOfValue = SU.getClassNameOfListOf(varListOfClassValue);
								String varBeanOnetoMany = attrClassOfValue+".java"
								String attrClassOfValueLower = SU.lowerCaseFirstLetter(attrClassOfValue)
								def varFileOneToMany = new File(beanPath+varBeanOnetoMany)
								def varAttrListOneToMany = PU.getAttributesFromClassFile(varFileOneToMany) %>
			<p:panel header="#{messages['${attrClassOfValueLower}.label']}" toggleable="true" toggleSpeed="500">
		 		<h:panelGrid id="fields${attrClassOfValueLower}" columns="1">
				 <p:outputPanel>
					<h:commandButton value="#{messages['button.new']}" action="#{${beanLower}EditMB.add${attrClassOfValue}}" update="list${attrClassOfValue}"/>
					<p:dataTable id="list${attrClassOfValue}" var="${attrClassOfValueLower}" value="#{${beanLower}EditMB.${attrClassOfValueLower}List}" rowIndexVar="index">
						<f:attribute name="columnToggle" value="true" />					
							<%	varAttrListOneToMany.each() { varAttrName, varAttrValue ->
								def annotationsForAField = PU.getAnnotationsForField(varFileOneToMany, varAttrName)
								def hasRelationship = SU.hasOneInList(annotationsForAField, relationshipsAnnotations)
								  if (hasRelationship == null){
								  %>
						<p:column>
							 <h:outputLabel value="#{messages['${attrClassOfValueLower}.label.${varAttrName}']}: " for="${varAttrName}" styleClass="text-input" />
								<%	if (PU.hasAnnotationForField(varFileOneToMany, varAttrName, 'GeneratedValue' )) { %>
						   <h:outputText id="${varAttrName}" value="#{${attrClassOfValueLower}.${varAttrName}}" />
								 <% }  else {
										   if (varAttrValue.equalsIgnoreCase('Date')) { %>
						   <p:calendar id="${varAttrName}" value="#{${attrClassOfValueLower}.${varAttrName}}"
						   		pattern="#{messages['label.date.pattern']}" navigator="true" showButtonPanel="true"
								locale="#{locales.currentLocale}" title="#{messages['${attrClassOfValueLower}.alt.${varAttrName}']}" />
										<% } else{
												if (PU.hasAnnotationForField(varFileOneToMany, varAttrName, 'Enumerated' )) { %>
						   <h:selectOneMenu id="${varAttrName}" value="#{${attrClassOfValueLower}.${varAttrName}}">
							   <f:selectItems value="#{${attrClassOfValueLower}EditMB.${varAttrName}}" />
						   </h:selectOneMenu>
											   <% }else { %>
						   <h:inputText id="${varAttrName}" value="#{${attrClassOfValueLower}.${varAttrName}}" title="#{messages['${attrClassOfValueLower}.alt.${varAttrName}']}" />
											   <% }
										  }
								  }%>
						   <h:message for="${varAttrName}" />
						 </p:column>
					<%  }
					} %>
					   <p:column>
						   <f:facet name="header">#{messages['label.action']}</f:facet>
						   <h:commandButton value="#{messages['button.delete']}" action="#{${beanLower}EditMB.delete${attrClassOfValue}}"/>
					   </p:column>
					</p:dataTable>
				  </p:outputPanel>
				</h:panelGrid>
			</p:panel>	<%	break
							case ["ManyToMany"]:
							// TODO - PickList JQueryUI 
								break
							default:
								break
						}
					}
				} %>															
					</h:form>
				</pm:content>
			</pm:view>
		</pm:page>	
</f:view>