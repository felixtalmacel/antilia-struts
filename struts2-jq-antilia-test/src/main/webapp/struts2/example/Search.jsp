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
       
<h2>Search Countries</h2>

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
