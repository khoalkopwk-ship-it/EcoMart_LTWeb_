<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title>UTE EcoMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ecomart.css">
</head>
<body>
<section class="hero">
    <div class="container-eco"><div class="hero__content">
        <span class="eyebrow">Phong cách EcoMarts</span>
        <h1>Mua sắm xanh, lựa chọn thông minh</h1>
        <p>Khám phá các sản phẩm mới nhất với trải nghiệm mua sắm nhẹ nhàng, rõ ràng và thân thiện.</p>
        <a class="btn-eco" href="${pageContext.request.contextPath}/product">Khám phá sản phẩm →</a>
    </div></div>
</section>

<section class="section section--soft">
    <div class="container-eco">
        <div class="section-heading"><div><h2>Danh mục nổi bật</h2><p>Chọn nhanh nhóm sản phẩm bạn quan tâm</p></div></div>
        <div class="category-grid">
            <c:forEach items="${categories}" var="cate">
                <c:if test="${cate.status == 1}">
                    <a class="category-card" href="${pageContext.request.contextPath}/product">
                        <c:choose>
                            <c:when test="${not empty cate.icon}"><img src="${pageContext.request.contextPath}/image?fname=${cate.icon}" alt="${cate.name}"></c:when>
                            <c:otherwise><img src="${pageContext.request.contextPath}/assets/img/eco.png" alt="${cate.name}"></c:otherwise>
                        </c:choose>
                        <div><h3>${cate.name}</h3><span>Xem sản phẩm →</span></div>
                    </a>
                </c:if>
            </c:forEach>
        </div>
    </div>
</section>

<section class="section">
    <div class="container-eco">
        <div class="section-heading">
            <div><h2>10 sản phẩm mới nhất</h2><p>Cập nhật tự động theo ngày tạo trong database</p></div>
            <a href="${pageContext.request.contextPath}/product">Xem tất cả →</a>
        </div>
        <div class="product-grid">
            <c:forEach items="${products}" var="p">
                <article class="product-card">
                    <a class="product-card__image" href="${pageContext.request.contextPath}/product/detail?id=${p.id}">
                        <span class="product-card__badge">Mới</span>
                        <img src="${pageContext.request.contextPath}/image?fname=${p.image}" alt="${p.name}">
                    </a>
                    <div class="product-card__body">
                        <span class="product-card__category">${p.category.name}</span>
                        <h3>${p.name}</h3>
                        <div class="price"><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> ₫</div>
                        <a class="btn-eco btn-eco--light" href="${pageContext.request.contextPath}/product/detail?id=${p.id}">Xem chi tiết</a>
                    </div>
                </article>
            </c:forEach>
            <c:if test="${empty products}"><div class="empty-state">Chưa có sản phẩm. Hãy chạy file database.sql hoặc thêm sản phẩm mới.</div></c:if>
        </div>
    </div>
</section>
</body></html>
