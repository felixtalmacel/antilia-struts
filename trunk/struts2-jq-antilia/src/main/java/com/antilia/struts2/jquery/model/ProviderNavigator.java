/**
 * 
 */
package com.antilia.struts2.jquery.model;

import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.antilia.common.query.IOrder;
import com.antilia.common.query.IQuery;
import com.antilia.common.query.Order;
import com.antilia.common.query.Query;
import com.antilia.struts2.jquery.utils.ReflectionUtils;

/**
 * @author Ernesto Reinaldo Barreiro (reirn70@gmail.com)
 *
 */
public  class ProviderNavigator<B extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private IDataProvider<B> dataProvider;
	
	private GridModel<B> gridModel;
	
	private IQuery<B> query;
	
	/**
	 * 
	 * @param dataProvider
	 * @param columnModel
	 */
	public ProviderNavigator(IDataProvider<B> dataProvider, GridModel<B> gridModel) {
		this.dataProvider = dataProvider;
		this.gridModel = gridModel;
		this.query = new Query<B>(gridModel.getBeanClass());
	}
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public final void renderData() throws Exception {
		configureProvider(dataProvider);
		if(this.gridModel.getTransferProtocol().equals(GridModel.TransferProtocol.xml))
			renderXML(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		else {
			renderJson(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		}
	}
	
	/**
	 * Give a chance to configure the provider.
	 * 
	 * @param dataProvider
	 */
	protected void configureProvider(IDataProvider<B> dataProvider) {
		SortOrder sortOrder = getSortOrder(ServletActionContext.getRequest());
		String propertyPath = getSortColumn(ServletActionContext.getRequest());
		getQuery().clearOrders();
		IOrder<B> order = Order.des(propertyPath);
		if(sortOrder.equals(SortOrder.asc)) {
			order = Order.asc(propertyPath);			
		}
		getQuery().addOrder(order);
		int rows = getNumberOfRows(ServletActionContext.getRequest());
		int page = getCurrentPage(ServletActionContext.getRequest());
		getQuery().setFirstResult(rows*(page-1));
		getQuery().setMaxResults(rows);
	}
	
	protected String renderJson(HttpServletRequest request, HttpServletResponse response) throws Exception {    	   
		response.setContentType("text/json-comment-filtered"); 
		PrintWriter writer = response.getWriter(); 
		writer.print("{\"page\":\"1\",\"total\":2,\"records\":\"13\",");
		writer.print("\"rows\":[");
		writer.print("{\"id\":\"13\",\"cell\":[\"13\",\"2007-10-06\",\"Client 3\",\"1000.00\",\"0.00\",\"1000.00\",null]}");
		writer.print(",{\"id\":\"12\",\"cell\":[\"12\",\"2007-10-06\",\"Client 2\",\"700.00\",\"140.00\",\"840.00\",null]}");
		writer.print(",{\"id\":\"11\",\"cell\":[\"11\",\"2007-10-06\",\"Client 1\",\"600.00\",\"120.00\",\"720.00\",null]}");
		writer.print(",{\"id\":\"10\",\"cell\":[\"10\",\"2007-10-06\",\"Client 2\",\"100.00\",\"20.00\",\"120.00\",null]}");
		writer.print(",{\"id\":\"9\",\"cell\":[\"9\",\"2007-10-06\",\"Client 1\",\"200.00\",\"40.00\",\"240.00\",null]}");
		writer.print(",{\"id\":\"8\",\"cell\":[\"8\",\"2007-10-06\",\"Client 3\",\"200.00\",\"0.00\",\"200.00\",null]}");
		writer.print(",{\"id\":\"7\",\"cell\":[\"7\",\"2007-10-05\",\"Client 2\",\"120.00\",\"12.00\",\"134.00\",null]}");
		writer.print(",{\"id\":\"6\",\"cell\":[\"6\",\"2007-10-05\",\"Client 1\",\"50.00\",\"10.00\",\"60.00\",null]}");
		writer.print(",{\"id\":\"5\",\"cell\":[\"5\",\"2007-10-05\",\"Client 3\",\"100.00\",\"0.00\",\"100.00\",\"no tax\"]}");
		writer.print(",{\"id\":\"4\",\"cell\":[\"4\",\"2007-10-04\",\"Client 3\",\"150.00\",\"0.00\",\"150.00\",\"no tax\"]}]");
		writer.print(",\"userdata\":{\"amount\":3220,\"tax\":342,\"total\":3564,\"name\":\"Totals:\"}}"); 		
		writer.flush();		
		return null;
    }
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	protected void renderXML(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml"); 
		PrintWriter writer = response.getWriter();
		int rows = getNumberOfRows(request);
		int page = getCurrentPage(request);
		int records = totalRecords(getQuery());
		int totalPages = (records/rows)+1;
		writer.println("<?xml version='1.0' encoding='utf-8'?>");
		writer.println("<rows>");
		writer.print("<page>");
		writer.print(page);
		writer.println("</page>");
		writer.print("<total>");
		writer.print(totalPages);
		writer.println("</total>");
		writer.print("<records>");
		writer.print(records);;		
		writer.println("</records>");		
		int row = 1;
		for(B bean: getRows(getQuery()) ) {
			writer.print("<row id=\"");
			writer.print("row"+row);
			writer.println("\">");
		    int column = 1;
		    for(GridColumnModel<B> columnModel: this.gridModel.getColumnModels()) {
		    	writer.print("<cell><![CDATA[");	
		    	if(columnModel.getCellRenderer() != null)
		    		writer.print(columnModel.getCellRenderer().renderCell(bean, columnModel.getPropertyPath(), column, row));		    		
		    	else
		    		writer.print(createCellContent(row, column, columnModel.getPropertyPath(), bean));		    	
		    		
		    	writer.println("]]></cell>");			     
		    }
		    writer.println("</row>");
		    row++;
		}				
		writer.println("</rows>");
		this.dataProvider.detach(); 
		writer.flush();
	}
	
	protected String createCellContent(int row, int column, String propertyPath, B bean) throws NoSuchFieldException {
		//return propertyPath+"_"+column;
		Object object = ReflectionUtils.getPropertyValue(bean, propertyPath);
		if(object != null) {
			return object.toString();
		}
		return "";
	}	
	
	private Iterable<? extends B> getRows(IQuery<B> query) {
		return this.dataProvider.getData(query);
	}
	
	private int totalRecords(IQuery<B> query) {
		return this.dataProvider.getSize(query);
	}
	
	private int getCurrentPage(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("page"));
		} catch (Exception e) {
			return 10;
		}
	}
	
	private int getNumberOfRows(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			return 10;
		}
	}
	
	
	private SortOrder getSortOrder(HttpServletRequest request) {
		try {
			return SortOrder.valueOf(request.getParameter("sord"));
		} catch (Exception e) {
			return SortOrder.asc;
		}
	}
	
	private String getSortColumn(HttpServletRequest request) {
		try {
			return request.getParameter("sidx");
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * @return the query
	 */
	public IQuery<B> getQuery() {
		return query;
	}


	/**
	 * @param query the query to set
	 */
	public void setQuery(IQuery<B> query) {
		this.query = query;
	}
	
		
}
