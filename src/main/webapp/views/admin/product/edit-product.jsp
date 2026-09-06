<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Sửa sản phẩm</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css"></head><body class="admin-layout">
<c:set var="activeMenu" value="product" scope="request"/><jsp:include page="/views/components/admin-sidebar.jsp" />
<div class="admin-main"><header class="admin-topbar"><strong>Hệ thống quản trị EcoMart</strong><span>Chỉnh sửa sản phẩm</span></header><main class="admin-content">
<div class="admin-title"><div><h1>Chỉnh sửa sản phẩm</h1><p>Không chọn ảnh mới thì hệ thống giữ ảnh hiện tại.</p></div></div>
<div class="panel"><c:if test="${not empty error}"><div class="alert-eco alert-eco--error">${error}</div></c:if>
<form class="form-grid" method="post" action="${pageContext.request.contextPath}/admin/product/edit" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${product.id}">
    <div class="form-group"><label for="name">Tên sản phẩm</label><input id="name" class="form-control-eco" name="name" value="${hasSubmittedValues ? enteredName : product.name}" maxlength="255" required></div>
    <div class="form-group"><label for="price">Giá</label><input id="price" class="form-control-eco" type="number" min="0" max="9999999999999999.99" step="0.01" name="price" value="${hasSubmittedValues ? enteredPrice : product.price}" required></div>
    <div class="form-group"><label for="categoryId">Danh mục</label><select id="categoryId" class="form-control-eco" name="categoryId" required><c:forEach items="${categories}" var="cate"><option value="${cate.id}" ${(hasSubmittedValues ? enteredCategory == cate.id : cate.id == product.category.id) ? 'selected' : ''}>${cate.name}</option></c:forEach></select></div>
    <div class="form-group"><label>Ảnh hiện tại</label><c:if test="${not empty product.image}"><img class="preview-image" src="${pageContext.request.contextPath}/image?fname=${product.image}" alt="${product.name}"></c:if></div>
    <div class="form-group form-group--full"><label for="image">Thay ảnh sản phẩm</label><input id="image" class="form-control-eco" type="file" name="image" accept=".jpg,.jpeg,.png,.gif,.webp"></div>
    <div class="form-group form-group--full"><label for="description">Mô tả</label><textarea id="description" class="form-control-eco" rows="5" name="description" maxlength="255">${hasSubmittedValues ? enteredDescription : product.description}</textarea></div>
    <div class="form-actions"><button class="btn-eco" type="submit">Lưu thay đổi</button><a class="btn-eco btn-eco--light" href="${pageContext.request.contextPath}/admin/product/list">Quay lại</a></div>
</form></div></main></div></body></html>
