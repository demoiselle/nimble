
menu.new.${bean} = ${pojo}
<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.RegexUtil as RU
def attrList = RU.getClassAttributesFromFile(pojoFileName)
if (!attrList.isEmpty()) {
	attrList.each() { attrName, attrValue -> 
		String attrLow = attrName.toLowerCase() 
%>
${bean}.label.${attrLow} = ${attrName}
${bean}.prompt.${attrLow} = ${pojo}'s ${attrName}
<%
	}
} else {
%>
${bean}.label.text = Text
${bean}.prompt.text = ${pojo}'s Text 
<%
}
%>