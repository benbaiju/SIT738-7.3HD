<%@ page import="org.springframework.security.web.csrf.CsrfToken" %>
<%
{
    CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
    if (token == null) {
        token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
    if (token != null) {
%>
<input type="hidden"
       name="<%= token.getParameterName() %>"
       value="<%= token.getToken() %>">
<%
    }
}
%>
