<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %><%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Quản lý sản phẩm</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css"></head><body class="admin-layout">
<c:set var="activeMenu" value="product" scope="request"/><jsp:include page="/views/components/admin-sidebar.jsp" />
<div class="admin-main"><header class="admin-topbar"><strong>Hệ thống quản trị EcoMart</strong><span>${empty sessionScope.account ? 'Khách quản trị' : sessionScope.account.fullname}</span></header>
<main class="admin-content"><div class="admin-title"><div><h1>Quản lý sản phẩm</h1><p>CRUD Product, quan hệ Category 1–n và Multipart.</p></div><a class="btn-eco" href="${pageContext.request.contextPath}/admin/product/add">+ Thêm sản phẩm</a></div>
<div class="panel"><table class="eco-table"><thead><tr><th>ID</th><th>Ảnh</th><th>Sản phẩm</th><th>Giá</th><th>Danh mục</th><th>Ngày tạo</th><th>Thao tác</th></tr></thead><tbody>
<c:forEach items="${products}" var="p"><tr>
    <td>${p.id}</td><td><c:if test="${not empty p.image}"><img class="table-image" src="${pageContext.request.contextPath}/image?fname=${p.image}" alt="${p.name}"></c:if></td>
    <td><strong>${p.name}</strong><br><small style="color:var(--eco-muted)">${p.description}</small></td>
    <td class="price" style="font-size:16px"><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> ₫</td>
    <td>${p.category.name}</td><td>${p.createdDate}</td>
    <td><div class="actions"><a class="btn-eco btn-eco--light btn-eco--small" href="${pageContext.request.contextPath}/admin/product/edit?id=${p.id}">Sửa</a>
        <form class="inline-form" method="post" action="${pageContext.request.contextPath}/admin/product/delete" onsubmit="return confirm('Xóa sản phẩm này?')"><input type="hidden" name="id" value="${p.id}"><button class="btn-eco btn-eco--danger btn-eco--small" type="submit">Xóa</button></form>
    </div></td>
</tr></c:forEach>
<c:if test="${empty products}"><tr><td colspan="7" style="text-align:center;padding:35px">Chưa có sản phẩm.</td></tr></c:if>
</tbody></table></div></main></div></body></html>
