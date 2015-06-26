<% import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil as SU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
def relationshipsAnnotations = ['ManyToMany','ManyToOne','OneToMany','OneToOne']
def relationshipsFields = [:]
%>
<ui:composition xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core"
	xmlns:p="http://primefaces.org/ui" xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets" template="/template/main.xhtml">

	<ui:define name="body">
		<h:form prependId="false">
			<p:toolbar>
				<p:toolbarGroup align="left">
					<p:commandButton value="#{messages['button.save']}" action="#{${beanLower}EditMB.insert}"
						rendered="#{!${beanLower}EditMB.updateMode}" ajax="false" />
					<p:commandButton value="#{messages['button.save']}" action="#{${beanLower}EditMB.update}"
						rendered="#{${beanLower}EditMB.updateMode}" ajax="false" />
					<p:commandButton value="#{messages['button.delete']}" onclick="PF('confirmation').show()"
						rendered="#{${beanLower}EditMB.updateMode}" type="button" immediate="true" ajax="false" />
					<p:confirmDialog message="#{messages['label.confirm.delete']}" showEffect="bounce" hideEffect="explode"
						header="#{messages['label.dialog.delete']}" severity="alert" widgetVar="confirmation">
						<h:commandButton value="#{messages['button.dialog.yes']}" action="#{${beanLower}EditMB.delete}" immediate="true"
							ajax="false" />
						<h:commandButton value="#{messages['button.dialog.no']}" onclick="PF('confirmation').hide();" type="button" />
					</p:confirmDialog>
				</p:toolbarGroup>
			</p:toolbar>

			<br />

			<p:fieldset legend="#{messages['${beanLower}.label']}" toggleable="true" toggleSpeed="500">
				<h:panelGrid id="fields${beanLower}" columns="3">
				<%	if (attrList.isEmpty()) { %>
					<h:outputLabel value="#{messages['${beanLower}.label.id']}: " for="id" styleClass="text-input" />
					<h:outputText id="id" value="#{${beanLower}EditMB.bean.id}" />
					<p:message for="id" />

					<h:outputLabel value="#{messages['${beanLower}.label.text']}: " for="text" styleClass="text-input" />
					<h:inputText id="text" value="#{${beanLower}EditMB.bean.text}"
						title="#{messages['${beanLower}.alt.text']}" />
					<p:message for="text" />
			  <%	} else {
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
				    <p:selectOneMenu id="${attrName}" value="#{${beanLower}EditMB.bean.${attrName}}"  effect="fade" converter="Conversor${attrValue}">
						<f:selectItem itemLabel="Selecione" itemValue="" />
						<f:selectItems value="#{${beanLower}EditMB.${attrName}List}" var="var${attrValue}" itemLabel="#{var${attrValue}.${varId}}"  itemValue="#{var${attrValue}}" />						
					</p:selectOneMenu>
					<p:message for="${attrName}" />								  
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
					<p:selectOneMenu id="${attrName}" effect="fade" value="#{${beanLower}EditMB.bean.${attrName}}">
						<f:selectItems value="#{${beanLower}EditMB.${attrName}}" />
					</p:selectOneMenu>					
								<%	}else{	%>
					<h:inputText id="${attrName}" value="#{${beanLower}EditMB.bean.${attrName}}"
						title="#{messages['${beanLower}.alt.${attrName}']}" />
					<%  			}
								}
							}%>
					<p:message for="${attrName}" />
						<% }
						}
					}%>
				</h:panelGrid>
			</p:fieldset>
			<% if (relationshipsFields != null && relationshipsFields.size() > 0){
				relationshipsFields.each { varField, varRelationship ->
					switch ( varRelationship.toString() ) {			 
						case ["OneToMany"]:
							String varListOfClassValue = PU.getFieldValue(tmpFile,varField)
							String attrClassOfValue = SU.getClassNameOfListOf(varListOfClassValue);
							String varBeanOnetoMany = attrClassOfValue+".java"
							String attrClassOfValueLower = SU.lowerCaseFirstLetter(attrClassOfValue)
							def varFileOneToMany = new File(beanPath+varBeanOnetoMany)
							def varAttrListOneToMany = PU.getAttributesFromClassFile(varFileOneToMany) %>
		<p:fieldset legend="#{messages['${attrClassOfValueLower}.label']}" toggleable="true" toggleSpeed="500">
			<h:panelGrid id="fields${attrClassOfValueLower}" columns="1"> 
				<p:commandButton value="#{messages['button.new']}" action="#{${beanLower}EditMB.add${attrClassOfValue}}" update="list${attrClassOfValue}"/>
				<p:dataTable id="list${attrClassOfValue}" var="${attrClassOfValueLower}" value="#{${beanLower}EditMB.${attrClassOfValueLower}List}" rowIndexVar="index">
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
					   <p:selectOneMenu id="${varAttrName}" value="#{${attrClassOfValueLower}.${varAttrName}}" effect="fade">
						   <f:selectItems value="#{${attrClassOfValueLower}EditMB.${varAttrName}}" />
					   </p:selectOneMenu>
					                       <% }else { %>
			           <h:inputText id="${varAttrName}" value="#{${attrClassOfValueLower}.${varAttrName}}"
				          title="#{messages['${attrClassOfValueLower}.alt.${varAttrName}']}" />
						                   <% }
							          }
						      }%>
					   <p:message for="${varAttrName}" />
			  	   </p:column>					  
				<%  }
				} %>
				   <p:column>
					   <f:facet name="header">#{messages['label.action']}</f:facet>
					   <p:commandButton value="#{messages['button.delete']}" action="#{${beanLower}EditMB.delete${attrClassOfValue}}" ajax="false"/>
				   </p:column>
				</p:dataTable>
			</h:panelGrid>
		</p:fieldset>
						<%	break
						case ["ManyToMany"]:
							String varListOfClassValue = PU.getFieldValue(tmpFile,varField)
							String attrClassOfValue = SU.getClassNameOfListOf(varListOfClassValue);
							String varBeanManytoMany = attrClassOfValue+".java"
							String attrClassOfValueLower = SU.lowerCaseFirstLetter(attrClassOfValue)
							def varFileManyToMany = new File(beanPath+varBeanManytoMany)
							def varId = PU.getFieldAnnotatedWith(varFileManyToMany, 'Id')%>
		<p:fieldset legend="#{messages['${attrClassOfValueLower}.label']}" toggleable="true">
			<p:pickList id="pickList${attrClassOfValue}" value="#{${beanLower}EditMB.${attrClassOfValueLower}List}"
				var="var${attrClassOfValue}" effect="bounce" itemValue="#{var${attrClassOfValue}}"
				itemLabel="#{var${attrClassOfValue}.${varId}}" converter="Conversor${attrClassOfValue}"
				showSourceControls="true" showTargetControls="true" showCheckbox="true"
				showSourceFilter="true" showTargetFilter="true" filterMatchMode="contains" immediate="true">
				<f:facet name="sourceCaption">"#{messages['${attrClassOfValueLower}.label']}"</f:facet>
				<f:facet name="targetCaption"> #{messages['${attrClassOfValueLower}.label']} "do(a)" #{messages['${beanLower}.label']} </f:facet>
				<p:ajax  event="transfer" listener="#{${beanLower}EditMB.onTransfer}" update="pickList${attrClassOfValue}" />
			</p:pickList>
		</p:fieldset>						
						<%	break
						default:
							break
					}
				}
			} %>
		</h:form>
	</ui:define>
</ui:composition>