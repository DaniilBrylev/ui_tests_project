# ui-tests-project
 
 ## 1. О проекте
 Учебный проект по UI-автотестам. Есть web и mobile сценарии. Запуск через TestNG suites и отдельные тесты.
 
 ## 2. Технологии
 - Java 17+
 - Maven
 - TestNG
 - Selenium WebDriver (WebDriverManager)
 - Appium Java Client
 
 ## 3. Что тестируется
 - Web: Home -> Checkboxes, Dropdown, Form Authentication, Add/Remove Elements
 - Mobile: Wikipedia main screen, search "Java", open first result and check title
 
-## 4. Как запустить Web

-1. Установить JDK 17 и Maven.

-2. Убедиться, что Chrome установлен.

-3. Запуск suite:
-```powershell
-mvn test -Dsurefire.suiteXmlFile=src/test/resources/testng-web.xml

-4. Запуск одного теста:
-powershell +mvn -Dtest=tests.web.AddRemoveElementsWebTest test +
+## 4. Запуск Web (PowerShell)
+Требования: JDK 17, Maven, установленныи Chrome.
+
+Web suite:
+```powershell
+mvn test -Dsurefire.suiteXmlFile=src/test/resources/testng-web.xml
+```
+
+Один web тест:
+```powershell
+mvn -Dtest=tests.web.AddRemoveElementsWebTest test
+```
 
-5. Как запустить Mobile
-1. Android SDK (например D:\Android\Sdk) и эмулятор.
-2. Добавить adb в PATH:
+## 5. Запуск Mobile (Wikipedia, PowerShell)
+Требования:
+- Android SDK (например D:\Android\Sdk)
+- Android Emulator запущен (например emulator-5554)
+- Appium Server 3.x
+
+Добавить adb в PATH (опционально):
+```powershell
+$env:Path="D:\Android\Sdk\platform-tools;$env:Path"
+```
 
-$env:Path="D:\Android\Sdk\platform-tools;$env:Path"
-3. Установить UDID:
+Установить UDID:
+```powershell
+$env:ANDROID_UDID="emulator-5554"
+```
 
-$env:ANDROID_UDID="emulator-5554"
-4. Добавить npm bin в PATH:
+Запустить Appium Server (base-path /wd/hub):
+```powershell
+appium --address 127.0.0.1 --port 4723 --base-path /wd/hub
+```
 
-$env:Path="C:\Users\Даниил\AppData\Roaming\npm;$env:Path"
-5. Запустить Appium Server (base-path /wd/hub):
+Mobile suite:
+```powershell
+mvn test "-Dsurefire.suiteXmlFile=src/test/resources/testng-mobile.xml" `
+  "-Dadb.path=D:\Android\Sdk\platform-tools\adb.exe" `
+  "-Dandroid.udid=emulator-5554"
+```
 
-appium --address 127.0.0.1 --port 4723 --base-path /wd/hub
-6. Запуск suite:
+Один mobile тест:
+```powershell
+mvn -Dtest=tests.mobile.SearchMobileTest test `
+  "-Dadb.path=D:\Android\Sdk\platform-tools\adb.exe" `
+  "-Dandroid.udid=emulator-5554"
+```
 
--mvn test "-Dsurefire.suiteXmlFile=src/test/resources/testng-mobile.xml" `
-  "-Dadb.path=D:\Android\Sdk\platform-tools\adb.exe" `
-  "-Dandroid.udid=emulator-5554"
-7. Запуск одного теста:
-```powershell
-mvn -Dtest=tests.mobile.SearchMobileTest test `
-
-"-Dadb.path=D:\Android\Sdk\platform-tools\adb.exe" `
-"-Dandroid.udid=emulator-5554"
-```
-8. Примечание: онбординг и разрешения закрываются автоматически.
-Чеклист PowerShell:
+Примечание: онбординг и системные разрешения закрываются автоматически.
 
-@@ -51,11 +64,11 @@ netstat -ano | findstr :4723
-6. Общий запуск
--bash +powershell
+## 6. Общии запуск
+Запуск всех тестов:
+```powershell
 mvn test
+```
 
-
 ## 7. На чем проверено
 - Windows 10
 - Chrome
 - Android Emulator (например Android 14)
-Команды PowerShell
-Web suite
-
-mvn test -Dsurefire.suiteXmlFile=src/test/resources/testng-web.xml
-Web one test
-
-mvn -Dtest=tests.web.AddRemoveElementsWebTest test
-Mobile suite
-
-mvn test "-Dsurefire.suiteXmlFile=src/test/resources/testng-mobile.xml" `
-  "-Dadb.path=D:\Android\Sdk\platform-tools\adb.exe" `
-  "-Dandroid.udid=emulator-5554"
-Mobile one test
-
-mvn -Dtest=tests.mobile.SearchMobileTest test `
-  "-Dadb.path=D:\Android\Sdk\platform-tools\adb.exe" `
-  "-Dandroid.udid=emulator-5554"
