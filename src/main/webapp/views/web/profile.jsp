<%--
  Created by IntelliJ IDEA.
  User: mkhoa
  Date: 9/3/2026
  Time: 9:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Thông tin cá nhân | UTE EcoMart</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/profile.css">
</head>

<body>
<main class="eco-main">
<section class="profile-page">
    <div class="container-eco">

        <div class="profile-heading">
            <span class="eyebrow">Tài khoản EcoMart</span>
            <h1>Thông tin cá nhân</h1>
            <p>Cập nhật họ tên, số điện thoại và ảnh đại diện.</p>
        </div>

        <c:if test="${param.updated eq '1'}">
            <div class="alert-eco alert-eco--success">
                Cập nhật thông tin thành công.
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert-eco alert-eco--error">
                    ${error}
            </div>
        </c:if>

        <div class="profile-card">

            <div class="profile-avatar">
                <c:choose>
                    <c:when test="${not empty profile.images}">
                        <c:url var="avatarUrl" value="/image">
                            <c:param name="fname"
                                     value="${profile.images}"/>
                        </c:url>

                        <img src="${avatarUrl}"
                             alt="Ảnh đại diện">
                    </c:when>

                    <c:otherwise>
                        <img
                                src="${pageContext.request.contextPath}/images/admin.png"
                                alt="Ảnh đại diện mặc định">
                    </c:otherwise>
                </c:choose>

                <strong>${profile.fullname}</strong>
                <span>${profile.email}</span>
            </div>

            <form class="profile-form"
                  method="post"
                  action="${pageContext.request.contextPath}/profile"
                  enctype="multipart/form-data">

                <div class="form-group">
                    <label>Tên đăng nhập</label>
                    <input type="text"
                           value="${profile.username}"
                           readonly>
                </div>

                <div class="form-group">
                    <label>Email</label>
                    <input type="email"
                           value="${profile.email}"
                           readonly>
                </div>

                <div class="form-group">
                    <label for="fullname">Họ và tên</label>
                    <input id="fullname"
                           name="fullname"
                           type="text"
                           maxlength="255"
                           value="${profile.fullname}"
                           required>
                </div>

                <div class="form-group">
                    <label for="phone">Số điện thoại</label>
                    <input id="phone"
                           name="phone"
                           type="tel"
                           maxlength="20"
                           minlength="8"
                           pattern="[0-9+() .-]{8,20}"
                           value="${profile.phone}"
                           placeholder="Ví dụ: 0912345678">
                </div>

                <div class="form-group">
                    <label for="image">Ảnh đại diện mới</label>
                    <input id="image"
                           name="image"
                           type="file"
                           accept=".jpg,.jpeg,.png,.gif,.webp">
                    <small>Kích thước tối đa 5 MB.</small>
                </div>

                <button class="btn-eco"
                        type="submit">
                    Lưu thay đổi
                </button>
            </form>

        </div>
    </div>
</section>
</main>
</body>
</html>
