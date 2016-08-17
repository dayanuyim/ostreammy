<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="empty" %> 
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ attribute name="local"       rtexprvalue="true" required="true" type="java.io.File"     description="The Local File" %> 
<%@ attribute name="local_home"  rtexprvalue="true" required="true" type="java.io.File"     description="The Local Home Dir containing the file" %> 
<%@ attribute name="serv_prefix" rtexprvalue="true" required="true" type="java.lang.String" description="The prefix server path you wnat to change to" %> 

<c:set var="path" value="${fn:substringAfter(local.absolutePath, local_home.absolutePath)}" />
${serv_prefix}${fn:replace(path, '\\', '/')}
<%--
<c:out value="${serv_prefix}${fn:replace(path, '\\', '/')}" />
--%>