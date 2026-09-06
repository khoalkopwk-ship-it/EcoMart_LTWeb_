<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Sửa danh mục</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css"></head><body class="admin-layout">
<c:set var="activeMenu" value="category" scope="request"/><jsp:include page="/views/components/admin-sidebar.jsp" />
<div class="admin-main"><header class="admin-topbar"><strong>Hệ thống quản trị EcoMart</strong><span>Chỉnh sửa danh mục</span></header><main class="admin-content">
    <div class="admin-title"><div><h1>Chỉnh sửa danh mục</h1><p>Giữ icon cũ nếu không chọn tệp mới.</p></div></div>
    <div class="panel"><c:if test="${not empty error}"><div class="alert-eco alert-eco--error">${error}</div></c:if>
        <form class="form-grid" method="post" action="${pageContext.request.contextPath}/admin/category/edit" enctype="multipart/form-data">
            <input type="hidden" name="id" value="${category.id}">
            <div class="form-group"><label for="name">Tên danh mục</label><input id="name" class="form-control-eco" name="name" value="${category.name}" maxlength="255" required></div>
            <div class="form-group"><label for="status">Trạng thái</label><select id="status" class="form-control-eco" name="status" required><option value="1" ${category.status == 1 ? 'selected' : ''}>Hoạt động</option><option value="0" ${category.status == 0 ? 'selected' : ''}>Tạm ẩn</option></select></div>
            <div class="form-group"><label>Icon hiện tại</label><c:if test="${not empty category.icon}"><img class="preview-image" src="${pageContext.request.contextPath}/image?fname=${category.icon}" alt="${category.name}"></c:if></div>
            <div class="form-group"><label for="icon">Thay icon</label><input id="icon" class="form-control-eco" type="file" name="icon" accept=".jpg,.jpeg,.png,.gif,.webp"></div>
            <div class="form-actions"><button class="btn-eco" type="submit">Lưu thay đổi</button><a class="btn-eco btn-eco--light" href="${pageContext.request.contextPath}/admin/category/list">Quay lại</a></div>
        </form>
    </div>
</main></div></body></html>
