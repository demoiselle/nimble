<% 
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
%>
package ${packageName}.persistence;

import java.util.List;
import javax.persistence.TypedQuery;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;
import ${packageName}.entity.${pojo};

@PersistenceController
public class ${pojo}DAO extends JPACrud<${pojo}, ${idType}> {

	private static final long serialVersionUID = 1L;
	
	
	public List<${pojo}> find(String filter) {
		StringBuffer ql = new StringBuffer();
		ql.append("  from ${pojo} p ");
// use where and or operator as filter		
//		ql.append(" where lower(p.attribute) like :attribute ");
//		ql.append("    or lower(p.anotherAttribute) like :anotherAttribute ");
// list of attributes
<%
if (!attrList.isEmpty()) {
	attrList.each() { attrName, attrValue ->
		if (!PU.hasAnnotationForField(tmpFile, attrName, 'GeneratedValue' )) {
	
%>
	//		${attrName} 
<%		}
	}
}
%>
		TypedQuery<${pojo}> query = getEntityManager().createQuery(ql.toString(), ${pojo}.class);
// use setParameter to fill attributes
//		query.setParameter("attribute", "%" + filter.toLowerCase() + "%");

		return query.getResultList();
	}
}
