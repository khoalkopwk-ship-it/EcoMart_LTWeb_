<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Thêm danh mục</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css"></head><body class="admin-layout">
<c:set var="activeMenu" value="category" scope="request"/><jsp:include page="/views/components/admin-sidebar.jsp" />
<div class="admin-main"><header class="admin-topbar"><strong>Hệ thống quản trị EcoMart</strong><span>Danh mục mới</span></header><main class="admin-content">
    <div class="admin-title"><div><h1>Thêm danh mục</h1><p>Biểu mẫu Multipart tương thích Jakarta Servlet.</p></div></div>
    <div class="panel"><c:if test="${not empty error}"><div class="alert-eco alert-eco--error">${error}</div></c:if>
        <form class="form-grid" method="post" action="${pageContext.request.contextPath}/admin/category/add" enctype="multipart/form-data">
            <div class="form-group"><label>Tên danh mục</label><input class="form-control-eco" name="name" value="${enteredName}" required></div>
            <div class="form-group"><label>Trạng thái</label><select class="form-control-eco" name="status"><option value="1">Hoạt động</option><option value="0" ${enteredStatus == '0' ? 'selected' : ''}>Tạm ẩn</option></select></div>
            <div class="form-group form-group--full"><label>Icon danh mục</label><input class="form-control-eco" type="file" name="icon" accept=".jpg,.jpeg,.png,.gif,.webp"></div>
            <div class="form-actions"><button class="btn-eco" type="submit">Lưu danh mục</button><a class="btn-eco btn-eco--light" href="${pageContext.request.contextPath}/admin/category/list">Quay lại</a></div>
        </form>
    </div>
</main></div></body></html>
