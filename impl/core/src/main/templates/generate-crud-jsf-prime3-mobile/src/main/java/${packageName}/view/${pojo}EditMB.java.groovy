<%
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
import br.gov.frameworkdemoiselle.tools.nimble.util.StringUtil as SU
def tmpFile = new File(beanPath+beanJavaName)
def attrList = PU.getAttributesFromClassFile(tmpFile)
%>
package ${packageName}.view;

import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import ${packageName}.business.*;
import ${packageName}.domain.*;
import javax.faces.model.*;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import java.util.*;

// To remove unused imports press: Ctrl+Shift+o

@ViewController
@PreviousView("./${beanLower}_list.jsf")
public class ${pojo}EditMB extends AbstractEditPageBean<${pojo}, ${idType}> {

	private static final long serialVersionUID = 1L;

	@Inject
	private ${pojo}BC ${beanLower}BC;
	
<%attrList.each() { attrName, attrValue ->	
	def varAnnotationsForField = PU.getAnnotationsForField(tmpFile, attrName)
	varAnnotationsForField.each { attrAnnotation -> 
		switch (attrAnnotation) {
			case ["OneToMany"]:
				String attrClassOfValue = SU.getClassNameOfListOf(attrValue)
				String attrNameFirstUp = SU.upperCaseFirstLetter(attrName)
				String attrClassFirstLower =  SU.lowerCaseFirstLetter(attrClassOfValue) %>
	private DataModel<${attrClassOfValue}> ${attrClassFirstLower}List;
	
	public void add${attrClassOfValue}() {
		getBean().get${attrNameFirstUp}().add(new ${attrClassOfValue}());
	}
	public void delete${attrClassOfValue}() {
	   getBean().get${attrNameFirstUp}().remove(get${attrClassOfValue}List().getRowData());
	}
	public DataModel<${attrClassOfValue}> get${attrClassOfValue}List() {
	   if (${attrClassFirstLower}List == null) {
		   ${attrClassFirstLower}List = new ListDataModel<${attrClassOfValue}>(getBean().get${attrNameFirstUp}());
	   }
	   return ${attrClassFirstLower}List;
	} <%		break
			case ["ManyToOne","OneToOne"]:
				String varAttrValueFirstLower = SU.lowerCaseFirstLetter(attrValue) %>
	@Inject
	private ${attrValue}BC ${varAttrValueFirstLower}BC;
	
	public List<${attrValue}> get${attrValue}List(){
		return ${varAttrValueFirstLower}BC.findAll();
	}
			<%	break
			case ["ManyToMany"]:
				String attrClassOfValue = SU.getClassNameOfListOf(attrValue)
				String attrNameFirstUp = SU.upperCaseFirstLetter(attrName)
				String attrClassFirstLower =  SU.lowerCaseFirstLetter(attrClassOfValue) %>

	private DualListModel<${attrClassOfValue}> ${attrClassFirstLower}List;
	
	@Inject
	private ${attrClassOfValue}BC ${attrClassFirstLower}BC;

	public void set${attrClassOfValue}List(DualListModel<${attrClassOfValue}> ${attrClassFirstLower}List) {
		this.${attrClassFirstLower}List = ${attrClassFirstLower}List;
	}
		
	public void add${attrClassOfValue}List(List<${attrClassOfValue}> ${attrClassFirstLower}List) {
		getBean().get${attrNameFirstUp}().addAll(${attrClassFirstLower}List);
	}

	public void delete${attrClassOfValue}List(List<${attrClassOfValue}> ${attrClassFirstLower}List) {
		getBean().get${attrNameFirstUp}().removeAll(${attrClassFirstLower}List);
	}
	
	
	public DualListModel<${attrClassOfValue}> get${attrClassOfValue}List() {
		if (this.${attrClassFirstLower}List == null) {

			List<${attrClassOfValue}> source = ${attrClassFirstLower}BC.findAll();
			List<${attrClassOfValue}> target = getBean().get${attrNameFirstUp}();

			if (source == null) {
				source = new ArrayList<${attrClassOfValue}>();
			}
			if (target == null) {
				target = new ArrayList<${attrClassOfValue}>();
			}else{
				source.removeAll(target);
			}
			this.${attrClassFirstLower}List = new DualListModel<${attrClassOfValue}>(source, target);

		}
		return this.${attrClassFirstLower}List;
	}
	
	@SuppressWarnings("unchecked")
	public void onTransfer(TransferEvent event) {
		if (event.isAdd()){
			this.add${attrClassOfValue}List((List<${attrClassOfValue}>) event.getItems());
		}
		if (event.isRemove()) {
			this.delete${attrClassOfValue}List((List<${attrClassOfValue}>) event.getItems());
		 }
	} <%		break
			case "Enumerated":
			String attrNameFirstUp =  SU.upperCaseFirstLetter(attrName) %>
	public List<SelectItem> get${attrNameFirstUp}() {
		return ${beanLower}BC.get${attrNameFirstUp}();
	}<%			break			
			default:
				break
		}
	} 
 }%>
	
	@Override
	@Transactional
	public String delete() {
		this.${beanLower}BC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		this.${beanLower}BC.insert(getBean());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		this.${beanLower}BC.update(getBean());
		return getPreviousView();
	}
	
	@Override
	protected ${pojo} handleLoad(${idType} id) {
		return this.${beanLower}BC.load(id);
	}
		
}