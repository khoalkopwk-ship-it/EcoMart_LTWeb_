<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><title>Danh mục sản phẩm</title></head><body>
<main class="page-section"><div class="container-eco">
    <div class="section-heading">
        <div><span class="eyebrow">Khám phá EcoMart</span><h1>Danh mục sản phẩm</h1></div>
        <p>Chọn nhóm sản phẩm phù hợp với nhu cầu của bạn.</p>
    </div>
    <div class="category-grid">
        <c:forEach items="${categories}" var="category"><c:if test="${category.status == 1}">
            <article class="category-card">
                <c:choose><c:when test="${not empty category.icon}">
                    <img src="${pageContext.request.contextPath}/image?fname=${category.icon}" alt="${category.name}">
                </c:when><c:otherwise><div class="category-card__placeholder">EcoMart</div></c:otherwise></c:choose>
                <div><h2>${category.name}</h2><p>Danh mục đang hoạt động</p></div>
            </article>
        </c:if></c:forEach>
    </div>
    <c:if test="${empty categories}"><p class="empty-state">Hiện chưa có danh mục sản phẩm.</p></c:if>
</div></main>
</body></html>
