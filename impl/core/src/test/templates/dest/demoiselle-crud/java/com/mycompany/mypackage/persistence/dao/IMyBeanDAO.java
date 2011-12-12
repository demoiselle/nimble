package com.mycompany.mypackage.persistence.dao;

import com.mycompany.mypackage.bean.MyBean;
import com.mycompany.mypackage.business.IMyBeanBC;
import br.gov.component.demoiselle.crud.layer.ICrudDAO;

public interface IMyBeanDAO extends ICrudDAO<MyBean> {
	private static final long serialVersionUID = 1L;
}