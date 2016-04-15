# The code #

### Search.jsp ###

The JSP just contains:

  * two search text field
  * a button that will collect form values and repaint the grid clicked
  * and the grid itself

The grid uses  the action (XMLDataDB.action) to get the drid's data as well as an object
(gridModel) defining properties about the grid.

```
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="antsj" uri="/struts-ant-jquery-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>	
    <title><s:text name="HelloWorld.message"/></title>
   	<antsj:head locale="es" jqgrid="true"/>   
</head>

<body>
<s:url id="url" action="Index">
</s:url>
<s:a href="%{url}">Back to examples</s:a>
       
<h2>Search Contries</h2>

<script type="text/javascript">
function doSearch(ev){
	if(!flAuto)
		return;
//	var elem = ev.target||ev.srcElement;
	if(timeoutHnd)
		clearTimeout(timeoutHnd)
	timeoutHnd = setTimeout(gridReload,500)
}

function gridReload(){
	var name = jQuery("#name").val();
	var domain = jQuery("#domain").val();
	jQuery("#list1").setGridParam({url:"XMLDataDB.action?name="+name+"&domain="+domain,page:1}).trigger("reloadGrid");
}    
</script>

<table>
	<tbody>
		<tr>
			<td><label for="name">Name:</label></td>
			<td><input id="name" name="name"/></td>
		</tr>
		<tr>
			<td><label for="domain">Domain:</label></td>
			<td><input id="domain" name="domain"/></td>
		</tr>
	</tbody>
</table>

<button onclick="gridReload();"  id="submitButton">Search</button>

<antsj:grid id="list1" url="XMLDataDB.action" gridModel="gridModel"/>
</body>
</html>

```

### The action ###

The relevant actions are defined at the example.xml file. Namely

```
 <action name="XMLDataDB" method="getData" class="com.antilia.struts2.JQueryGridDBXMLAction">            
 </action>

 <action name="SearchCountry"  class="com.antilia.struts2.JQueryGridDBXMLAction">            
	<result>/struts2/example/Search.jsp</result>
 </action>
```

The relevant parts of of JQueryGridDBXMLAction (or the class JQueryGridDBAction from which the former inherits)
are the following.

```
	private GridModel<Country>  gridModel;
  
    private String name;
    
    private String domain;
	
	public String execute() throws Exception {    	   
    	setRequest_locale((String)ServletActionContext.getRequest().getParameter("request_locale"));
        setMessage(getText(MESSAGE));                
        initModel();

        return SUCCESS;
    }
	
	private void initModel() {
		gridModel = new GridModel<Country>(Country.class);
		gridModel.setCaption("Countries");
		// transfer protocol will determine how the tag communicates with the back end.
		gridModel.setTransferProtocol(getTransferProtocol());
		gridModel.setAutowidth(true);
		gridModel.setShrinkToFit(false);
		gridModel.setRownumbers(true);
		gridModel.setHidegrid(false);
		gridModel.setMultiselect(true);
		gridModel.setSortOrder(SortOrder.asc);
		GridColumnModel<Country> columnModel = new GridColumnModel<Country>("name", 100);
        columnModel.setInitialSort(true);
        columnModel.setCellRenderer(new IGridCellRenderer<Country>() {
        	private static final long serialVersionUID = 1L;

			public String renderCell(Country bean, String propertyPath, int column, int row) {
        		return "<span style=\"color: red;\">"+bean.getName()+"</span>";
        	}
        });
        gridModel.addColumnModel(columnModel);
        
        columnModel = new GridColumnModel<Country>("id", 100);
        columnModel.setSortable(false);
        columnModel.setResizable(false);
        gridModel.addColumnModel(columnModel);
        
        columnModel = new GridColumnModel<Country>("domain", 200);
        gridModel.addColumnModel(columnModel);
               
	}
```

On the initModel() we initialize the GridModel which will define the look and feel properties of the table. This object
will be used by the grid tag to generate appropriate JQuery Grid (columns, which columns are sortable, etc).

Once the grid is rendered it will generate a HTTP request to the the same action: asking for data. This is handle by the method

```
     public String getData() throws Exception {
		.....	
		try {
			.....		
			initModel();
			
			.....
			
			Country country = new Country();
			country.setName(getName());
			country.setDomain(getDomain());
			ProviderNavigator<Country> navigator = new ProviderNavigator<Country>(new CountriesProvider(), gridModel, country, "name", "domain");
			navigator.renderData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		..... 
		return null;
	}
```

The ProviderNavigator is an utility class that will generate all the data needed by the GRID (either on XML or JSON) based on
the given model and an instance of the interface:

```
public interface IDataProvider<B extends Serializable> extends Serializable {
		
	/**
	 * Returns an Iterable over the records starting at first and ending at first+size.
	 * 
	 * @param start first record to return
	 * @param size number of records to return
	 * @param searchBean Bean to be used for filtering the results.
	 * @param sortInfo  which column should be used for ordering data.
	 * @param searchFields Which fields of the seacrBean should be used to filter data.
	 * @return
	 */
	Iterable<? extends B> getData(int start, int size,  B searchBean, SortInfo sortInfo, String... searchFields);
	
	/**
	 * Returns the number of records.
	 * 
	 * @param searchBean Bean to be used for filtering the results
	 * @param sortInfo which column should be used for ordering data.
	 * @param searchFields Which fields of the seacrBean should be used to filter data.
	 * @return Returns the number of records.
	 */
	int getSize(B searchBean, SortInfo sortInfo, String... searchFields); 
	
	
	/**
	 * Can be used to detach any heavy resources that where created while
	 * finding the data produced by the query. Detach will always be called  
	 * after getData() and getSize() methods have been both executed.
	 */
	void detach();

}
```

CountriesProvider is just an implementation of the interfaces that uses Hibernate to retrieve data.

## The result ##

http://antilia-struts.googlecode.com/svn/wiki/Search1.JPG

and

http://antilia-struts.googlecode.com/svn/wiki/Search2.JPG

