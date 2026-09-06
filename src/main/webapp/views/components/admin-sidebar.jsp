<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="admin-sidebar">
    <a class="brand" href="${pageContext.request.contextPath}/home">
        <img src="${pageContext.request.contextPath}/assets/img/eco.png" alt="EcoMart">
        <span>EcoMart Admin</span>
    </a>
    <div class="admin-sidebar__label">Cửa hàng</div>
    <nav>
        <a href="${pageContext.request.contextPath}/home">⌂ Trang chủ</a>
        <a class="${activeMenu eq 'category' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/category/list">▦ Quản lý danh mục</a>
        <a class="${activeMenu eq 'product' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/admin/product/list">▣ Quản lý sản phẩm</a>
    </nav>
    <div class="admin-sidebar__label">Tài khoản</div>
    <nav>
        <c:choose>
            <c:when test="${not empty sessionScope.account}">
                <a href="${pageContext.request.contextPath}/logout">↪ Đăng xuất</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">→ Đăng nhập</a>
            </c:otherwise>
        </c:choose>
    </nav>
</aside>
