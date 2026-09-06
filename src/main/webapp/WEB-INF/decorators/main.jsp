<%@ page contentType="text/html;charset=UTF-8"
          pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="requestUri" value="${pageContext.request.requestURI}"/>
<c:set var="storeLayout"
       value="${not fn:contains(requestUri, '/admin/') and (fn:endsWith(requestUri, '/home') or fn:endsWith(requestUri, '/profile') or fn:endsWith(requestUri, '/category') or fn:contains(requestUri, '/product'))}"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title"/></title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/ecomart.css">
    <sitemesh:write property="head"/>
</head>
<body>
    <c:if test="${storeLayout}">
        <jsp:include page="/views/components/header.jsp"/>
    </c:if>
    <sitemesh:write property="body"/>
    <c:if test="${storeLayout}">
        <jsp:include page="/views/components/footer.jsp"/>
    </c:if>
</body>
</html>
