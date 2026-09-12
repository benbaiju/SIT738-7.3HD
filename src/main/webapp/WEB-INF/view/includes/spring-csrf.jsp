<%@ page import="org.springframework.security.web.csrf.CsrfToken" %>
<%
    CsrfToken springCsrfToken = (CsrfToken) request.getAttribute("_csrf");
    if (springCsrfToken == null) {
        springCsrfToken = (CsrfToken) request.getAttribute(
                CsrfToken.class.getName());
    }
    if (springCsrfToken != null) {
%>
<input type="hidden"
       name="<%= springCsrfToken.getParameterName() %>"
       value="<%= springCsrfToken.getToken() %>">
<%
    }
%>
