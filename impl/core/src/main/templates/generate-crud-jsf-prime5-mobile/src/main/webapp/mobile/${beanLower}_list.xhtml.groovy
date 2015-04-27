<%
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil as SU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
def relationshipsAnnotations = ['ManyToMany','ManyToOne','OneToMany','OneToOne']
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
xmlns:h="http://java.sun.com/jsf/html"
xmlns:f="http://java.sun.com/jsf/core"
xmlns:p="http://primefaces.org/ui"
xmlns:ui="http://java.sun.com/jsf/facelets"
xmlns:pm="http://primefaces.org/mobile">
  <h:body>
   <ui:include src="/mobile/template/main_mobile.xhtml" />
     <pm:page id="list_${beanLower}" title="Listagem de ${beanLower}">

	  <pm:header title="${beanLower}" swatch="b">
	    <p:tabMenu>
	     <p:menuitem icon="ui-icon-plus"
		      url="#{${beanLower}ListMB.nextView}" iconPos="top"
		      value="#{messages['button.new']}"
		      actionListener="#{${beanLower}ListMB.clear}" />
         <p:menuitem icon="ui-icon-home" url="./navigator.jsf" iconPos="top"
		    value="#{messages['main.app.title']}" />
      </p:tabMenu>
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
		</pm:page>
  </h:body>
</html>
