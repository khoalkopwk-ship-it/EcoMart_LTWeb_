Trong PowerShell, cấu hình cho tài khoản Windows để có thể gửi otp:
[Environment]::SetEnvironmentVariable(
    "MAIL_USERNAME",
    "email-gui@gmail.com",
    "User"
)

[Environment]::SetEnvironmentVariable(
    "MAIL_PASSWORD",
    "mat-khau-ung-dung-16-ky-tu",
    "User"
)