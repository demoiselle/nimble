package com.mycompany.mypackage.view.managedbean;

import com.mycompany.mypackage.bean.MyBean;
import br.gov.component.demoiselle.crud.annotation.CrudPaged;
import br.gov.component.demoiselle.crud.supercrud.SuperCrudMB;

@CrudPaged(title="MyBean", view = "mybean")
public class MyBeanMB extends SuperCrudMB<MyBean> {
	private static final long serialVersionUID = 1L;
}