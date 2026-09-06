<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html lang="vi"><head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tất cả sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ecomart.css">
</head><body>
<section class="page-hero"><div class="container-eco"><h1>Tất cả sản phẩm</h1><p>Danh sách được phân trang trực tiếp bằng JPA.</p></div></section>
<main class="section"><div class="container-eco">
    <div class="result-bar"><span>Tìm thấy <strong>${totalProducts}</strong> sản phẩm</span><span>Trang ${currentPage}/${totalPages == 0 ? 1 : totalPages}</span></div>
    <div class="product-grid">
        <c:forEach items="${products}" var="p">
            <article class="product-card">
                <a class="product-card__image" href="${pageContext.request.contextPath}/product/detail?id=${p.id}">
                    <img src="${pageContext.request.contextPath}/image?fname=${p.image}" alt="${p.name}">
                </a>
                <div class="product-card__body">
                    <span class="product-card__category">${p.category.name}</span><h3>${p.name}</h3>
                    <div class="price"><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> ₫</div>
                    <a class="btn-eco" href="${pageContext.request.contextPath}/product/detail?id=${p.id}">Xem chi tiết</a>
                </div>
            </article>
        </c:forEach>
        <c:if test="${empty products}"><div class="empty-state">Chưa có sản phẩm.</div></c:if>
    </div>
    <c:if test="${totalPages > 1}"><nav class="pagination-eco">
        <c:if test="${currentPage > 1}"><a href="${pageContext.request.contextPath}/product?page=${currentPage - 1}">‹</a></c:if>
        <c:forEach begin="1" end="${totalPages}" var="i">
            <a class="${i == currentPage ? 'active' : ''}" href="${pageContext.request.contextPath}/product?page=${i}">${i}</a>
        </c:forEach>
        <c:if test="${currentPage < totalPages}"><a href="${pageContext.request.contextPath}/product?page=${currentPage + 1}">›</a></c:if>
    </nav></c:if>
</div></main>
</body></html>
