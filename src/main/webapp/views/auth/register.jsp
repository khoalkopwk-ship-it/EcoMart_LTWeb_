<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đăng ký</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css"></head><body>
<main class="auth-page"><section class="auth-visual"><div><h1>Tham gia EcoMart</h1><p>Tài khoản chỉ được ghi vào database sau khi mã OTP gửi qua email được xác nhận thành công.</p></div></section>
<section class="auth-panel"><div class="auth-card">
    <a class="brand" href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/assets/img/eco.png" alt="EcoMart"><span>UTE EcoMart</span></a>
    <h2>Đăng ký tài khoản</h2><p class="auth-card__lead">Nhập thông tin để nhận mã OTP 6 chữ số.</p>
    <c:if test="${not empty error}"><div class="alert-eco alert-eco--error">${error}</div></c:if>
    <form class="auth-form" method="post" action="${pageContext.request.contextPath}/register">
        <div><label>Họ và tên</label><input name="fullname" value="${fullname}" required></div>
        <div><label>Tên đăng nhập</label><input name="username" value="${username}" required></div>
        <div><label>Email</label><input type="email" name="email" value="${email}" required></div>
        <div><label>Mật khẩu</label><input type="password" name="password" minlength="6" required></div>
        <div><label>Xác nhận mật khẩu</label><input type="password" name="confirmPassword" minlength="6" required></div>
        <button class="btn-eco" type="submit">Gửi mã OTP</button>
    </form><div class="auth-links"><a href="${pageContext.request.contextPath}/home">← Trang chủ</a><a href="${pageContext.request.contextPath}/login">Đã có tài khoản?</a></div>
</div></section></main></body></html>
