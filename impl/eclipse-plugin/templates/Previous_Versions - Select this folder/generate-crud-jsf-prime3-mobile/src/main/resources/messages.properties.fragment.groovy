menu.${beanLower}=${pojo}s
${beanLower}.label=${pojo}
<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as RU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = RU.getAttributesFromClassFile(tmpFile)

%>
${beanLower}-delete-ok=${pojo} exclu\\u00EDdo: {0}
${beanLower}-insert-ok=${pojo} inserido: {0}
${beanLower}-update-ok=${pojo} atualizado: {0}

${beanLower}.list.table.title=Lista de ${pojo}s
<%
if (!attrList.isEmpty()) {
	attrList.each() { attrName, attrValue ->
    def attrLow = attrName.substring(0,1).toLowerCase()+attrName.substring(1); 
%>
${beanLower}.label.${attrLow}=${attrName}
${beanLower}.alt.${attrLow}=${attrName}
<%
	}
} else {
%>
${beanLower}.label.id=ID
${beanLower}.alt.id=ID
${beanLower}.label.text=Text
${beanLower}.alt.text=Text
<%
}
%>