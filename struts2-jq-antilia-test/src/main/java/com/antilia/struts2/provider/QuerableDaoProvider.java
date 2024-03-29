/**
 * 
 */
package com.antilia.struts2.provider;

import java.io.Serializable;

import com.antilia.common.dao.IQuerableDao;
import com.antilia.common.query.IOrder;
import com.antilia.common.query.IQuery;
import com.antilia.common.query.MatchMode;
import com.antilia.common.query.Order;
import com.antilia.common.query.Query;
import com.antilia.common.query.Restrictions;
import com.antilia.common.util.ReflectionUtils;
import com.antilia.struts2.jquery.model.SortInfo;
import com.antilia.struts2.jquery.model.SortOrder;
import com.antilia.struts2.jquery.provider.IDataProvider;

/**
 * @author Ernesto Reinaldo Barreiro (reirn70@gmail.com)
 *
 */
public class QuerableDaoProvider<B extends Serializable> implements IDataProvider<B> {

	private static final long serialVersionUID = 1L;
	
	private IQuerableDao<B>  querableDao;
	
	
	@SuppressWarnings("unchecked")
	public Iterable<? extends B> getData(int start, int size, B searchBean, SortInfo sortInfo, String... searchFields) {
		IQuery<B> query = new Query<B>((Class<B>) searchBean.getClass());
		configureFilters(query, searchBean, searchFields);
		if(sortInfo != null) {
			IOrder<B> order = Order.des(sortInfo.getProperty());
			if(sortInfo.getSortOrder().equals(SortOrder.asc)) {
				order = Order.asc(sortInfo.getProperty());			
			}
			query.addOrder(order);
		}
		
		query.setFirstResult(start);
		query.setMaxResults(size);	
		return getData(query);
	};
	
	@SuppressWarnings("unchecked")
	public int getSize(B searchBean, SortInfo sortInfo, String... searchFields) {
		IQuery<B> query = new Query<B>((Class<B>) searchBean.getClass());
		configureFilters(query, searchBean, searchFields);
		if(sortInfo != null) {
			IOrder<B> order = Order.des(sortInfo.getProperty());
			if(sortInfo.getSortOrder().equals(SortOrder.asc)) {
				order = Order.asc(sortInfo.getProperty());			
			}
			query.addOrder(order);
		}
		return getSize(query);
	};
	
	private void configureFilters(IQuery<B> query, B searchBean, String... searchFields) {
		if(searchFields != null) {
			for(String seachField: searchFields) {
				try {
					Object value = ReflectionUtils.getPropertyValue(searchBean, seachField);
					if(value != null) {
						if(value instanceof String) {
							query.addRestriction(Restrictions.ilike(seachField, "%"+value.toString()+"%", MatchMode.ANYWHERE));
						} else {
							query.addRestriction(Restrictions.eq(seachField, value));
						}
					}
				} catch (Exception e) {
					// do nothing and continue
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public QuerableDaoProvider(IQuerableDao<B>  querableDao) {
		this.querableDao = querableDao;
	}

	public Iterable<? extends B> getData(IQuery<B> query) {
		return querableDao.findAll(query);
	}
	
	public int getSize(IQuery<B> query) {
		Long size = querableDao.count(query);
		if(size != null)
			return size.intValue();
		return 0;
	}
	
	
	public void detach() {
		// do nothing as I'm not caching anything
	}
}
