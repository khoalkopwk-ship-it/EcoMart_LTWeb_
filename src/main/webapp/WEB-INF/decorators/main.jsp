<%--
  Created by IntelliJ IDEA.
  User: mkhoa
  Date: 9/3/2026
  Time: 9:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"
          pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title><sitemesh:write property="title"/></title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/ecomart.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/profile.css">

    <sitemesh:write property="head"/>
</head>

<body>

<jsp:include page="/views/components/header.jsp"/>

<main class="eco-main">
    <sitemesh:write property="body"/>
</main>

<jsp:include page="/views/components/footer.jsp"/>

</body>
</html>