<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Quản lý danh mục</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css"></head><body class="admin-layout">
<c:set var="activeMenu" value="category" scope="request"/><jsp:include page="/views/components/admin-sidebar.jsp" />
<div class="admin-main"><header class="admin-topbar"><strong>Hệ thống quản trị EcoMart</strong><span>${empty sessionScope.account ? 'Khách quản trị' : sessionScope.account.fullname}</span></header>
<main class="admin-content">
    <div class="admin-title"><div><h1>Quản lý danh mục</h1><p>Tìm kiếm, thêm, sửa, xóa và upload icon.</p></div><a class="btn-eco" href="${pageContext.request.contextPath}/admin/category/add">+ Thêm danh mục</a></div>
    <c:if test="${not empty error}"><div class="alert-eco alert-eco--error">${error}</div></c:if>
    <c:if test="${not empty sessionScope.categoryError}"><div class="alert-eco alert-eco--error">${sessionScope.categoryError}</div><c:remove var="categoryError" scope="session"/></c:if>
    <div class="panel">
        <form method="get" action="${pageContext.request.contextPath}/admin/category/list" style="display:flex;gap:10px;margin-bottom:20px">
            <input class="form-control-eco" style="max-width:360px" name="keyword" value="${keyword}" maxlength="255" placeholder="Tìm tên danh mục...">
            <button class="btn-eco" type="submit">Tìm kiếm</button>
            <a class="btn-eco btn-eco--light" href="${pageContext.request.contextPath}/admin/category/list">Làm mới</a>
        </form>
        <table class="eco-table"><thead><tr><th>ID</th><th>Icon</th><th>Tên danh mục</th><th>Trạng thái</th><th>Thao tác</th></tr></thead><tbody>
        <c:forEach items="${cateList}" var="cate"><tr>
            <td>${cate.id}</td><td><c:if test="${not empty cate.icon}"><img class="table-image" src="${pageContext.request.contextPath}/image?fname=${cate.icon}" alt="${cate.name}"></c:if></td>
            <td><strong>${cate.name}</strong></td>
            <td><span class="status ${cate.status == 1 ? 'status--active' : 'status--inactive'}">${cate.status == 1 ? 'Hoạt động' : 'Tạm ẩn'}</span></td>
            <td><div class="actions">
                <a class="btn-eco btn-eco--light btn-eco--small" href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.id}">Sửa</a>
                <form class="inline-form" method="post" action="${pageContext.request.contextPath}/admin/category/delete">
                    <input type="hidden" name="id" value="${cate.id}"><button class="btn-eco btn-eco--danger btn-eco--small" type="submit">Xóa</button>
                </form>
            </div></td>
        </tr></c:forEach>
        <c:if test="${empty cateList}"><tr><td colspan="5" style="text-align:center;padding:35px">Không có danh mục phù hợp.</td></tr></c:if>
        </tbody></table>
    </div>
</main></div></body></html>
