
menu.new.${bean} = ${pojo}
<%


import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as RU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = RU.getAttributesFromClassFile(tmpFile)
if (!attrList.isEmpty()) {
	attrList.each() { attrName, attrValue -> 
		String attrLow = attrName.toLowerCase() 
%>
${bean}.label.${attrLow} = ${attrName}
${bean}.prompt.${attrLow} = ${attrName} de ${pojo} 
<%
	}
} else {
%>
${bean}.label.text = Texto
${bean}.prompt.text = ${pojo}'s Text 
<%
}
%>