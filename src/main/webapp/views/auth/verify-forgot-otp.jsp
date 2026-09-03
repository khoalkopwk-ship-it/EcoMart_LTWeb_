<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Xác nhận OTP</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css"></head><body>
<main class="auth-page"><section class="auth-visual"><div><h1>Xác nhận yêu cầu</h1><p>Nhập mã OTP trong email để mở biểu mẫu đặt mật khẩu mới.</p></div></section>
<section class="auth-panel"><div class="auth-card">
    <a class="brand" href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/assets/img/eco.png" alt="EcoMart"><span>UTE EcoMart</span></a>
    <h2>Xác nhận OTP</h2><p class="auth-card__lead">Mã có hiệu lực trong 5 phút.</p>
    <c:if test="${not empty error}"><div class="alert-eco alert-eco--error">${error}</div></c:if>
    <form class="auth-form" method="post" action="${pageContext.request.contextPath}/forgot-password/verify">
        <div><label>Mã OTP</label><input name="otp" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" required autofocus></div>
        <button class="btn-eco" type="submit">Xác nhận</button>
    </form><div class="auth-links"><a href="${pageContext.request.contextPath}/forgot-password">Gửi lại yêu cầu</a></div>
</div></section></main></body></html>
