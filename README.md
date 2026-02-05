# ui-tests-project

## 1. О проекте
Учебный проект по UI-автотестам для веб- и мобильных приложений.
Реализованы автоматизированные тесты с использованием Selenium WebDriver и Appium.
Тесты запускаются через TestNG suites или по отдельности.

## 2. Технологии
- Java 17+
- Maven
- TestNG
- Selenium WebDriver (WebDriverManager)
- Appium Java Client

## 3. Что тестируется

### Web
Сайт: https://the-internet.herokuapp.com  
Покрытые сценарии:
- Checkboxes
- Dropdown
- Form Authentication
- Add / Remove Elements

### Mobile
Android-приложение Wikipedia:
- Загрузка главного экрана
- Поиск статьи по ключевому слову
- Открытие первой статьи из результатов
- Проверка заголовка статьи

## 4. Запуск Web (PowerShell)

Требования:
- JDK 17
- Maven
- Установленный Google Chrome

Запуск web suite:
```powershell
mvn test -Dsurefire.suiteXmlFile=src/test/resources/testng-web.xml
Запуск одного web-теста:

powershell
Копировать код
mvn -Dtest=tests.web.AddRemoveElementsWebTest test
5. Запуск Mobile (Wikipedia, PowerShell)
Требования:

Android SDK (например: D:\Android\Sdk)

Запущенный Android Emulator (например: emulator-5554)

Appium Server 3.x

Установить UDID эмулятора:

powershell
Копировать код
$env:ANDROID_UDID="emulator-5554"
Запустить Appium Server:

powershell
Копировать код
appium --address 127.0.0.1 --port 4723 --base-path /wd/hub
Запуск mobile suite:

powershell
Копировать код
mvn test "-Dsurefire.suiteXmlFile=src/test/resources/testng-mobile.xml" `
  "-Dadb.path=D:\Android\Sdk\platform-tools\adb.exe" `
  "-Dandroid.udid=emulator-5554"
Запуск одного mobile-теста:

powershell
Копировать код
mvn -Dtest=tests.mobile.SearchMobileTest test `
  "-Dadb.path=D:\Android\Sdk\platform-tools\adb.exe" `
  "-Dandroid.udid=emulator-5554"
Примечание:
Онбординг и системные разрешения в приложении Wikipedia закрываются автоматически.

6. Общий запуск
Запуск всех тестов:

powershell
Копировать код
mvn test
7. На чем проверено
Windows 10 / 11

Google Chrome

Android Emulator (Android 12 / 13 / 14)

markdown
Копировать код
