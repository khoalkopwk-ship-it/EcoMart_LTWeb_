<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đăng nhập</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css"></head><body>
<main class="auth-page"><section class="auth-visual"><div><h1>Chào mừng trở lại</h1><p>Đăng nhập để tiếp tục trải nghiệm UTE EcoMart trong giao diện thân thiện lấy cảm hứng từ EcoMarts.</p></div></section>
<section class="auth-panel"><div class="auth-card">
    <a class="brand" href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/assets/img/eco.png" alt="EcoMart"><span>UTE EcoMart</span></a>
    <h2>Đăng nhập</h2><p class="auth-card__lead">Dùng tên đăng nhập hoặc email của bạn.</p>
    <c:if test="${not empty error}"><div class="alert-eco alert-eco--error">${error}</div></c:if>
    <c:if test="${not empty sessionScope.success}"><div class="alert-eco alert-eco--success">${sessionScope.success}</div><c:remove var="success" scope="session"/></c:if>
    <form class="auth-form" method="post" action="${pageContext.request.contextPath}/login">
        <div><label>Tài khoản hoặc email</label><input name="login" value="${login}" required autofocus></div>
        <div><label>Mật khẩu</label><input type="password" name="password" required></div>
        <button class="btn-eco" type="submit">Đăng nhập</button>
    </form>
    <div class="auth-links"><a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a><a href="${pageContext.request.contextPath}/register">Tạo tài khoản</a></div>
</div></section></main></body></html>
