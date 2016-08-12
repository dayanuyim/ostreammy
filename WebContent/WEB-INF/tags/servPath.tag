<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="empty" %> 
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ attribute name="local"        rtexprvalue="true" required="true" type="java.lang.String" description="The Local Path" %> 
<%@ attribute name="local_prefix" rtexprvalue="true" required="true" type="java.lang.String" description="The prefix local path you want to change" %> 
<%@ attribute name="serv_prefix"  rtexprvalue="true" required="true" type="java.lang.String" description="The prefix server path you changed to" %> 


<c:set var="path" value="${fn:substringAfter(local, local_prefix)}" />
<%--
${local}<br>
${local_prefix}<br>
${serv_prefix}<br>

<c:set var="path2" value="${fn:replace(path, '\\', '/')}" />
<c:out value="${path2}" />
<c:out value="${serv_prefix}${fn:replace(path, '\\', '/')}" />
--%>
${path}