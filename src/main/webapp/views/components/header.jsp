<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="store-header">
    <div class="container-eco store-header__row">
        <a class="brand" href="${pageContext.request.contextPath}/home">
            <img src="${pageContext.request.contextPath}/assets/img/eco.png" alt="UTE EcoMart">
            <span>UTE EcoMart</span>
        </a>
        <nav class="main-nav">
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/product">Sản phẩm</a>
            <a href="${pageContext.request.contextPath}/admin/category/list">Danh mục</a>
            <a href="${pageContext.request.contextPath}/admin/product/list">Quản trị</a>
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    <span class="nav-user">
                        Xin chào, ${sessionScope.account.fullname}
                    </span>

                    <a href="${pageContext.request.contextPath}/profile">
                        Tài khoản
                    </a>

                    <a href="${pageContext.request.contextPath}/logout">
                        Đăng xuất
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    <a class="btn-eco btn-eco--small" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
