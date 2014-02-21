<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
%>

menu.${beanLower}=${pojo}s
${beanLower}.label=${pojo}

${beanLower}.delete.ok=${pojo} removido: {0}
${beanLower}.insert.ok=${pojo} inserido: {0}
${beanLower}.update.ok=${pojo} atualizado: {0}

${beanLower}.delete.nok=erro ao excluir ${pojo}: {0}
${beanLower}.insert.nok=erro ao inserir ${pojo}: {0}
${beanLower}.update.nok=erro ao atualizar ${pojo}: {0}

${beanLower}.list.table.title=Lista de ${pojo}s
<%
if (!attrList.isEmpty()) {
	attrList.each() { attrName, attrValue ->
%>
${beanLower}.label.${attrName}=${attrName}
${beanLower}.alt.${attrName}=${attrName}
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