# 🛒 Khorosh Java Project
## سامانه خرید و فروش دست دوم

یک سامانه دسکتاپ-سرور برای خرید و فروش کالاهای دست دوم که با استفاده از `Java`, `Spring Boot`, `JavaFX`, `Maven` و `CSS` توسعه داده شده است. این پروژه با معماری چندبخشی طراحی شده و شامل یک بک‌اند سرویس‌محور و یک فرانت‌اند گرافیکی مبتنی بر جاوا اف ایکس است.

---

## 📖 معرفی پروژه
این پروژه با هدف پیاده‌سازی یک سامانه کاربردی برای خرید و فروش کالاهای دست دوم توسعه داده شده است. کاربران می‌توانند در این سیستم ثبت‌نام کنند، وارد حساب خود شوند، آگهی‌ها را مشاهده کنند، جزئیات هر آگهی را ببینند و با سایر کاربران وارد گفتگو شوند. همچنین بخش مدیریت سیستم برای کنترل کاربران، بررسی بخش‌های مختلف سامانه و مدیریت فرایندهای اصلی در نظر گرفته شده است.

---

## 👥 اعضای تیم
- **آرین صفری** 🎨 (توسعه‌دهنده فرانت‌اند)
- **علی مهدی‌پور گنجی** ⚙️ (توسعه‌دهنده بک‌اند)

> با وجود تمرکز بیشتر هر عضو روی یک بخش خاص، هر دو نفر در توسعه کلی پروژه مشارکت مستقیم و مؤثر داشته‌اند.

---

## 🛠 تکنولوژی‌ها و ابزارها

- ☕ **Java**
- 🚀 **Spring Boot**
- 🖥 **JavaFX**
- 📦 **Maven**
- 🎨 **CSS**
- 🎨**HTML**
- 🐙 **Git**
- 📑 **Swagger & Postman**
- 🐘 **PostgreSQL**
- ☕ **JUnit 5** برای تعریف و اجرای تست‌ها
- 🎭 **Mockito** برای Mock کردن وابستگی‌های سرویس‌ها
- 📦 **Maven** برای مدیریت وابستگی‌ها و اجرای تست‌ها
- 🚀 **Maven Surefire Plugin** برای شناسایی و اجرای تست‌های JUnit



---

## 🔍 معرفی تکنولوژی‌ها و مفاهیم

- **Java**: زبان شیءگرا و قدرتمندی که پایه و اساس منطق سیستم را در هر دو بخش بک‌اند و فرانت‌اند تشکیل می‌دهد.
- **Spring Boot**: فریم‌ورک محبوب جاوا که با ساده‌سازی تنظیمات، امکان توسعه سریع سرویس‌های بک‌اند را فراهم می‌کند.
- **JavaFX**: پلتفرمی برای طراحی رابط کاربری گرافیکی  مدرن و منعطف برای اپلیکیشن‌های دسکتاپ.
- **Maven**: ابزاری برای مدیریت کتابخانه‌ها  تنظیمات پروژه و خودکارسازی فرآیند ساخت و اجرا.
- **CSS**: زبانی برای زیباسازی و مدیریت ظاهر المان‌های بصری در محیط فرانت‌اند.
- **Git**: ابزار حیاتی برای کنترل تغییرات کد، پیگیری تاریخچه پروژه و تسهیل همکاری بین اعضای تیم.
- **Swagger & Postman**: ابزارهایی برای مستندسازی خودکار ها و ارسال درخواست‌های تست جهت اطمینان از صحت عملکرد سرویس‌ها.
- **PostgreSQL**: یک سیستم مدیریت دیتابیس رابطه‌ای  پیشرفته، امن و با قابلیت اطمینان بالا برای ذخیره داده‌های اصلی برنامه.
- **SQLite**: دیتابیس مبتنی بر فایل و بسیار سبک که بدون نیاز به سرور، برای تست‌های سریع و محیط‌های توسعه ایده‌آل است.
- **JWT (JSON Web Token)**: استانداردی برای انتقال امن اطلاعات بین کلاینت و سرور. در این پروژه، با تولید توکن‌های رمزنگاری‌شده پس از ورود موفق کاربر، مدیریت نشست  و احراز هویت را به صورت   امن انجام می‌دهد.

---

## 🏗 معماری کلی پروژه

### Backend
- پیاده‌سازی منطق اصلی سیستم
- ارائه REST API
- مدیریت امنیت، داده‌ها و احراز هویت

### Frontend
- پیاده‌سازی رابط کاربری گرافیکی
- مدیریت ناوبری (Navigation)
- ارتباط با APIهای بک‌اند

---

## 🏗 ساختار کلی مخزن

divar-java-project/
├── Backend/
├── Frontend/
├── .gitignore
└── README.md

---
## 🏗 ساختار کلی بکند
Backend/src/main/java/org/example/backend/
├── config/          # تنظیمات (Security, JWT)
├── controller/      # REST APIها
├── service/         # منطق برنامه
├── repository/      # دیتابیس (JPA)
├── entity/          # موجودیت‌ها
└── dto/             # مدل‌های انتقال داده

---
## 🏗 ساختار کلی فرانت
Frontend/src/main/java/org/example/frontend/
├── auth/            # ورود و ثبت‌نام
├── chat/            # چت
├── advertisement/   # مدیریت آگهی‌ها
└── shared/          # کامپوننت‌های مشترک

قابلیت‌ها
✅ ثبت‌نام و ورود امن
📢 مشاهده آگهی‌ها
💬 چت بین کاربران
🛡 پنل مدیریت

---

⚡   راهنمای اجرا
First run BackendApplication
Second run HelloApplication
Every run that has not admin ,makes an admin with this information :
username: admin
pass: 123456

## 🧪 تست‌های واحد Backend

برای اطمینان از صحت عملکرد بخش‌های اصلی Backend، مجموعه‌ای از تست‌های واحد با استفاده از **JUnit 5** و **Mockito** پیاده‌سازی شده است.

این تست‌ها منطق سرویس‌ها را به‌صورت مستقل و ایزوله بررسی می‌کنند؛ بنابراین برای اجرای آن‌ها نیازی به راه‌اندازی کامل برنامه یا اتصال واقعی به پایگاه داده نیست. وابستگی‌هایی مانند Repositoryها نیز با استفاده از Mockito شبیه‌سازی یا Mock شده‌اند.



### 📁 محل قرارگیری تست‌ها

فایل‌های تست در مسیر استاندارد Maven قرار گرفته‌اند:

Backend/src/test/java/org/example/backend/service/impl/

کلاس‌های تست پیاده‌سازی‌شده عبارت‌اند از:
AdminDashboardServiceImplTest.java
AdminReviewServiceImplTest.java
UserServiceImplTest.java

---

## 📊 خلاصه تست‌های پیاده‌سازی‌شده

| کلاس تست | تعداد تست | بخش مورد بررسی |
| `AdminDashboardServiceImplTest` | 2 | آمار و اطلاعات داشبورد ادمین |
| `AdminReviewServiceImplTest` | 7 | بررسی، تأیید و رد آگهی‌ها توسط ادمین |
| `UserServiceImplTest` | 6 | مدیریت کاربران، وضعیت حساب و مجوزهای دسترسی |
| **مجموع** | **15** | **تست‌های واحد Backend** |

---

## 🔍 جزئیات تست‌ها

### 1️⃣ تست‌های داشبورد ادمین

کلاس زیر شامل **۲ تست واحد** است:
AdminDashboardServiceImplTest

این تست‌ها منطق مربوط به داشبورد مدیریتی را بررسی می‌کنند، از جمله:

- 📈 دریافت آمار موردنیاز داشبورد ادمین
- 🔗 بررسی تعامل صحیح سرویس با Repositoryهای مربوطه

---

### 2️⃣ تست‌های بررسی آگهی توسط ادمین

کلاس زیر شامل **۷ تست واحد** است:

AdminReviewServiceImplTest

این تست‌ها منطق مربوط به مدیریت و بررسی آگهی‌ها توسط ادمین را پوشش می‌دهند، از جمله:

- 📋 دریافت آگهی‌های در انتظار بررسی
- ✅ تأیید آگهی توسط ادمین
- ❌ رد آگهی توسط ادمین
- 🔄 بررسی و تغییر وضعیت آگهی
- 🔎 مدیریت شرایطی که آگهی موردنظر پیدا نمی‌شود
- 💾 بررسی ذخیره‌شدن صحیح تغییرات
- 🧩 بررسی تعامل صحیح سرویس با وابستگی‌های Mock‌شده

---

### 3️⃣ تست‌های مدیریت کاربران

کلاس زیر شامل **۶ تست واحد** است:
UserServiceImplTest

سناریوهای بررسی‌شده در این کلاس عبارت‌اند از:

- ✅ دریافت موفق کاربر با شناسه معتبر
- 🔎 مدیریت خطا در صورت پیدا نشدن کاربر
- 🛡️ جلوگیری از مسدودکردن حساب ادمین
- 🚫 جلوگیری از فعال‌سازی مجدد حساب حذف‌شده
- 🔐 جلوگیری از حذف حساب کاربر دیگر توسط کاربر عادی
- 🗑️ انجام حذف نرم یا **Soft Delete** برای حساب کاربر

---

## ▶️ اجرای تست‌ها

برای اجرای تمام تست‌های Backend، ابتدا وارد پوشه `Backend` شوید:


cd Backend

سپس دستور زیر را اجرا کنید:


mvn clean test

این دستور ابتدا خروجی‌های قبلی را پاک می‌کند، کدهای اصلی و تست‌ها را کامپایل می‌کند و در نهایت تمام تست‌های شناسایی‌شده را اجرا می‌کند.

---

## ✅ نتیجه اجرای تست‌ها

خروجی نهایی اجرای تست‌ها به شکل زیر بوده است:

AdminDashboardServiceImplTest: 2 tests
AdminReviewServiceImplTest:    7 tests
UserServiceImplTest:           6 tests
---------------------------------------
Total:                        15 tests

نتیجه گزارش‌شده توسط Maven:
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

| نتیجه | مقدار |
|:---|:---:|
| 🧪 تعداد کل تست‌ها | **15** |
| ❌ تست‌های ناموفق | **0** |
| ⚠️ خطاهای اجرایی | **0** |
| ⏭️ تست‌های ردشده | **0** |
| ✅ وضعیت نهایی Build | **SUCCESS** |

---

## 🖼️ تصویر نتیجه اجرای تست‌ها

تصویر زیر نتیجه اجرای موفق تست‌های واحد Backend با Maven را نشان می‌دهد:

<div align="center">
<figure>
  <img src="./images/test.png" alt="tests">
  <figcaption>test.png</figcaption>
</figure>

</div>

---

## 🎯 نتیجه‌گیری

در مجموع، **۱۵ تست واحد** برای بررسی بخش‌های مهم Backend پیاده‌سازی و اجرا شده است:

- 📊 **۲ تست** برای داشبورد ادمین
- 📝 **۷ تست** برای بررسی و مدیریت آگهی‌ها توسط ادمین
- 👤 **۶ تست** برای مدیریت کاربران و مجوزهای دسترسی

تمام تست‌ها با موفقیت اجرا شده‌اند و هیچ Failure یا Error در نتیجه اجرای آن‌ها وجود ندارد:


Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

بنابراین، منطق بررسی‌شده در سرویس‌های **داشبورد ادمین، مدیریت آگهی‌ها و مدیریت کاربران** در سناریوهای تعریف‌شده عملکرد صحیحی داشته است. ✅
`
### Pictures

<div align="center">

<figure>
  <img src="./images/login.jpg" alt="login">
  <figcaption>login.jpg</figcaption>
</figure>

<figure>
  <img src="./images/sign-up.jpg" alt="sign up">
  <figcaption>sign-up.jpg</figcaption>
</figure>

<figure>
  <img src="./images/Main-Page.jpg" alt="Main Page">
  <figcaption>Main-Page.jpg</figcaption>
</figure>

<figure>
  <img src="./images/statistical-admin-dashboard.png" alt="statistical admin dashboard">
  <figcaption>statistical-admin-dashboard.png</figcaption>
</figure>

<figure>
  <img src="./images/Cities.jpg" alt="Cities">
  <figcaption>Cities.jpg</figcaption>
</figure>

<figure>
  <img src="./images/User-Controller.jpg" alt="User Controller">
  <figcaption>User-Controller.jpg</figcaption>
</figure>

<figure>
  <img src="./images/Add-Advertisement.jpg" alt="Add Advertisement">
  <figcaption>Add-Advertisement.jpg</figcaption>
</figure>

<figure>
  <img src="./images/Add-to-Favorites.jpg" alt="Add to Favorites">
  <figcaption>Add-to-Favorites.jpg</figcaption>
</figure>

<figure>
  <img src="./images/Admin-Review-Advertisement.jpg" alt="Admin Review Advertisement">
  <figcaption>Admin-Review-Advertisement.jpg</figcaption>
</figure>

<figure>
  <img src="./images/Advertisement-Details.jpg" alt="Advertisement Details">
  <figcaption>Advertisement-Details.jpg</figcaption>
</figure>

<figure>
  <img src="./images/Comment-and-rate.jpg" alt="Comment and rate">
  <figcaption>Comment-and-rate.jpg</figcaption>
</figure>

<figure>
  <img src="./images/favorities.jpg" alt="favorities">
  <figcaption>favorities.jpg</figcaption>
</figure>

<figure>
  <img src="./images/Seller-Advertisement-Controller.jpg" alt="Seller Advertisement Controller">
  <figcaption>Seller-Advertisement-Controller.jpg</figcaption>
</figure>

<figure>
  <img src="./images/Advance-search.jpg" alt="Advance search">
  <figcaption>Advance-search.jpg</figcaption>
</figure>

<figure>
  <img src="./images/advance-search-1.jpg" alt="advance search 1">
  <figcaption>advance-search-1.jpg</figcaption>
</figure>

<figure>
  <img src="./images/advance-search-2.jpg" alt="advance search 2">
  <figcaption>advance-search-2.jpg</figcaption>
</figure>
<figure>
  <img src="./images/Edit-advertisement.png" alt="Edit advertisement">
  <figcaption>Edit-advertisement.png</figcaption>
</figure>
<figure>
  <img src="./images/Edit-advertisement1.png" alt="Edit advertisement detalis">
  <figcaption>Edit-advertisement1.png</figcaption>
</figure>

</div>

