<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html lang="vi"><head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${product.name}</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ecomart.css">
</head><body>
<jsp:include page="/views/components/header.jsp" />
<main class="section"><div class="container-eco"><article class="detail-card">
    <div class="detail-image"><img src="${pageContext.request.contextPath}/image?fname=${product.image}" alt="${product.name}"></div>
    <div class="detail-info">
        <span class="eyebrow" style="background:var(--eco-green-soft);color:var(--eco-green)">${product.category.name}</span>
        <h1>${product.name}</h1>
        <div class="price"><fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0"/> ₫</div>
        <hr style="border:0;border-top:1px solid var(--eco-border);margin:24px 0">
        <h3>Mô tả sản phẩm</h3><p class="detail-description">${product.description}</p>
        <a class="btn-eco btn-eco--light" href="${pageContext.request.contextPath}/product">← Quay lại danh sách</a>
    </div>
</article></div></main>
<jsp:include page="/views/components/footer.jsp" />
</body></html>
