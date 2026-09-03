<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đặt mật khẩu mới</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css"></head><body>
<main class="auth-page"><section class="auth-visual"><div><h1>Tạo mật khẩu mới</h1><p>Mật khẩu được băm PBKDF2 trước khi lưu trong SQL Server.</p></div></section>
<section class="auth-panel"><div class="auth-card">
    <a class="brand" href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/assets/img/eco.png" alt="EcoMart"><span>UTE EcoMart</span></a>
    <h2>Đặt mật khẩu mới</h2><p class="auth-card__lead">Mật khẩu phải có ít nhất 6 ký tự.</p>
    <c:if test="${not empty error}"><div class="alert-eco alert-eco--error">${error}</div></c:if>
    <form class="auth-form" method="post" action="${pageContext.request.contextPath}/reset-password">
        <div><label>Mật khẩu mới</label><input type="password" name="password" minlength="6" required autofocus></div>
        <div><label>Xác nhận mật khẩu</label><input type="password" name="confirmPassword" minlength="6" required></div>
        <button class="btn-eco" type="submit">Cập nhật mật khẩu</button>
    </form>
</div></section></main></body></html>
