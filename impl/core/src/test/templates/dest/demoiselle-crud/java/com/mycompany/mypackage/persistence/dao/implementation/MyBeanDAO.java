package com.mycompany.mypackage.persistence.dao.implementation;

import com.mycompany.mypackage.bean.MyBean;
import com.mycompany.mypackage.persistence.dao.IMyBeanDAO;
import br.gov.component.demoiselle.crud.supercrud.SuperCrudDAO;

public class MyBeanDAO extends SuperCrudDAO<MyBean> implements IMyBeanDAO {
	private static final long serialVersionUID = 1L;
}
