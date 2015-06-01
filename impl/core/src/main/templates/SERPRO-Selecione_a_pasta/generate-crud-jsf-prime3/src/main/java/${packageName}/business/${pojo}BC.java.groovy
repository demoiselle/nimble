<%
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil as SU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
%>
package ${packageName}.business;

import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import ${packageName}.entity.*;
import java.util.*;
import javax.faces.model.*;
import ${packageName}.persistence.${pojo}DAO;

// To remove unused imports press: Ctrl+Shift+o

@BusinessController
public class ${pojo}BC extends DelegateCrud<${pojo}, ${idType}, ${pojo}DAO> {
	private static final long serialVersionUID = 1L;
	
	<%attrList.each() { attrName, attrValue ->
		if (PU.hasAnnotationForField(tmpFile, attrName, "Enumerated")){ %>
	public List<SelectItem> get${attrValue}() {
		List<SelectItem> var${attrValue} = new ArrayList<SelectItem>();
		for (${attrValue} each${attrValue} : ${attrValue}.values()) {
			var${attrValue}.add(new SelectItem(each${attrValue}));
		}
		return var${attrValue};
	}
	<% }		
	} %>
}
