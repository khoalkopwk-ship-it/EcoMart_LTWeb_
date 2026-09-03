<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Thêm sản phẩm</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css"></head><body class="admin-layout">
<c:set var="activeMenu" value="product" scope="request"/><jsp:include page="/views/components/admin-sidebar.jsp" />
<div class="admin-main"><header class="admin-topbar"><strong>Hệ thống quản trị EcoMart</strong><span>Sản phẩm mới</span></header><main class="admin-content">
<div class="admin-title"><div><h1>Thêm sản phẩm</h1><p>Ảnh được upload bằng Jakarta Multipart và lưu vào thư mục product.</p></div></div>
<div class="panel"><c:if test="${not empty error}"><div class="alert-eco alert-eco--error">${error}</div></c:if>
<form class="form-grid" method="post" action="${pageContext.request.contextPath}/admin/product/add" enctype="multipart/form-data">
    <div class="form-group"><label>Tên sản phẩm</label><input class="form-control-eco" name="name" value="${enteredName}" required></div>
    <div class="form-group"><label>Giá</label><input class="form-control-eco" type="number" min="0" step="1000" name="price" value="${enteredPrice}" required></div>
    <div class="form-group"><label>Danh mục</label><select class="form-control-eco" name="categoryId" required><option value="">-- Chọn danh mục --</option><c:forEach items="${categories}" var="cate"><option value="${cate.id}" ${enteredCategory == cate.id ? 'selected' : ''}>${cate.name}</option></c:forEach></select></div>
    <div class="form-group"><label>Ảnh sản phẩm</label><input class="form-control-eco" type="file" name="image" accept=".jpg,.jpeg,.png,.gif,.webp" required></div>
    <div class="form-group form-group--full"><label>Mô tả</label><textarea class="form-control-eco" rows="5" name="description">${enteredDescription}</textarea></div>
    <div class="form-actions"><button class="btn-eco" type="submit">Lưu sản phẩm</button><a class="btn-eco btn-eco--light" href="${pageContext.request.contextPath}/admin/product/list">Quay lại</a></div>
</form></div></main></div></body></html>
