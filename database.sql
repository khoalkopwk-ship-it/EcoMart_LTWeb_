IF DB_ID(N'BT02_CRUD_JPA') IS NULL
    CREATE DATABASE BT02_CRUD_JPA;
GO

USE BT02_CRUD_JPA;
GO

IF OBJECT_ID(N'dbo.Category', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Category
    (
        cate_id INT IDENTITY(1,1) PRIMARY KEY,
        cate_name NVARCHAR(255) NOT NULL UNIQUE,
        icons NVARCHAR(255) NULL,
        status INT NOT NULL CONSTRAINT DF_Category_Status DEFAULT 1
    );
END;
GO

IF COL_LENGTH('dbo.Category', 'status') IS NULL
    ALTER TABLE dbo.Category ADD status INT NOT NULL
        CONSTRAINT DF_Category_Status_Added DEFAULT 1;
GO

IF OBJECT_ID(N'dbo.Users', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Users
    (
        id INT IDENTITY(1,1) PRIMARY KEY,
        email NVARCHAR(255) NOT NULL UNIQUE,
        username NVARCHAR(100) NOT NULL UNIQUE,
        fullname NVARCHAR(255) NOT NULL,
        password NVARCHAR(512) NOT NULL,
        phone NVARCHAR(20) NULL,
        images NVARCHAR(255) NULL,
        status INT NOT NULL CONSTRAINT DF_Users_Status DEFAULT 1,
        createdDate DATETIME2 NOT NULL
            CONSTRAINT DF_Users_Created DEFAULT SYSDATETIME()
    );
END;
GO

IF OBJECT_ID(N'dbo.Products', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Products
    (
        product_id INT IDENTITY(1,1) PRIMARY KEY,
        product_name NVARCHAR(255) NOT NULL,
        price DECIMAL(18,2) NOT NULL,
        description NVARCHAR(MAX) NULL,
        image NVARCHAR(255) NULL,
        cate_id INT NOT NULL,
        createdDate DATETIME2 NOT NULL CONSTRAINT DF_Products_Created DEFAULT SYSDATETIME(),
        CONSTRAINT FK_Products_Category FOREIGN KEY(cate_id) REFERENCES dbo.Category(cate_id)
    );
END;
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Quần Áo Nam')
    INSERT dbo.Category(cate_name, icons, status) VALUES (N'Quần Áo Nam', N'category/ao-nam.png', 1);
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Quần Áo Nữ')
    INSERT dbo.Category(cate_name, icons, status) VALUES (N'Quần Áo Nữ', N'category/ao-nu.png', 1);
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Giày Dép')
    INSERT dbo.Category(cate_name, icons, status) VALUES (N'Giày Dép', N'category/giay-dep.png', 1);
GO

DECLARE @Nam INT = (SELECT TOP 1 cate_id FROM dbo.Category WHERE cate_name = N'Quần Áo Nam');
DECLARE @Nu INT = (SELECT TOP 1 cate_id FROM dbo.Category WHERE cate_name = N'Quần Áo Nữ');
DECLARE @Giay INT = (SELECT TOP 1 cate_id FROM dbo.Category WHERE cate_name = N'Giày Dép');

IF NOT EXISTS (SELECT 1 FROM dbo.Products)
BEGIN
    INSERT dbo.Products(product_name, price, description, image, cate_id) VALUES
    (N'Áo Polo Nam Basic', 249000, N'Áo polo cotton thoáng mát, form trẻ trung.', N'product/ao-polo-nam.png', @Nam),
    (N'Áo Sơ Mi Nam', 329000, N'Áo sơ mi nam form rộng, dễ phối đồ.', N'product/ao-so-mi-nam.png', @Nam),
    (N'Áo Thun Nam Cotton', 179000, N'Áo thun nam cơ bản, chất cotton mềm.', N'product/ao-thun-nam.png', @Nam),
    (N'Quần Kaki Nam', 399000, N'Quần kaki nam trẻ trung và lịch sự.', N'product/quan-kaki-nam.png', @Nam),
    (N'Quần Jean Nam', 459000, N'Quần jean nam slim fit.', N'product/quan-jean-nam.png', @Nam),
    (N'Áo Khoác Nam', 549000, N'Áo khoác nam phong cách tối giản.', N'product/ao-khoac-nam.png', @Nam),
    (N'Quần Short Nam', 269000, N'Quần short nam thoáng mát.', N'product/quan-short-nam.png', @Nam),
    (N'Áo Thun Nữ', 189000, N'Áo thun nữ cổ tròn.', N'product/ao-thun-nu.png', @Nu),
    (N'Áo Công Sở Nữ', 299000, N'Áo nữ phong cách công sở.', N'product/ao-cong-so-nu.png', @Nu),
    (N'Váy Nữ Dáng Ngắn', 359000, N'Váy nữ trẻ trung.', N'product/vay-nu.png', @Nu),
    (N'Đầm Nữ Công Sở', 489000, N'Đầm nữ thanh lịch.', N'product/dam-cong-so-nu.png', @Nu),
    (N'Quần Jean Nữ', 429000, N'Quần jean nữ ống rộng.', N'product/quan-jean-nu.png', @Nu),
    (N'Áo Croptop Nữ', 219000, N'Áo croptop nữ trẻ trung.', N'product/ao-croptop-nu.png', @Nu),
    (N'Chân Váy Chữ A', 319000, N'Chân váy chữ A dễ phối.', N'product/chan-vay-chu-a.png', @Nu),
    (N'Giày Sneaker Nam', 499000, N'Giày sneaker nam thể thao.', N'product/giay-sneaker-nam.png', @Giay),
    (N'Giày Sneaker Nữ', 469000, N'Giày sneaker nữ thời trang.', N'product/giay-sneaker-nu.png', @Giay),
    (N'Sandal Nữ', 289000, N'Sandal nữ nhẹ và thoáng.', N'product/sandal-nu.png', @Giay),
    (N'Dép Quai Ngang', 159000, N'Dép quai ngang unisex.', N'product/dep-quai-ngang.png', @Giay),
    (N'Giày Thể Thao', 599000, N'Giày thể thao unisex.', N'product/giay-the-thao.png', @Giay),
    (N'Giày Lười Nam', 519000, N'Giày lười nam lịch sự.', N'product/giay-luoi-nam.png', @Giay);
END;
GO
