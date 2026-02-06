# ui-tests-project

## 1. О проекте
Небольшой учебный проект по UI автотестам. Есть web и mobile сценарии. Все тесты запускаются через TestNG suites.

## 2. Технологии
- Java 11+
- Maven
- TestNG
- Selenium WebDriver
- Appium

## 3. Что тестируется
- Web: Home -> Checkboxes, Dropdown, Form Authentication
- Mobile: Wikipedia main screen, search "Java", open first result and check title

## 4. Как запустить Web
1. Установить JDK и Maven.
2. Убедиться, что Chrome установлен.
3. Запустить:
```bash
mvn test -Dsurefire.suiteXmlFile=src/test/resources/testng-web.xml
```

## 5. Как запустить Mobile
1. Добавить adb в PATH:
```powershell
$env:Path="D:\Android\Sdk\platform-tools;$env:Path"
```
2. Установить UDID:
```powershell
$env:ANDROID_UDID="emulator-5554"
```
3. Добавить npm bin в PATH:
```powershell
$env:Path="C:\Users\Даниил\AppData\Roaming\npm;$env:Path"
```
4. Запустить Appium Server на 127.0.0.1:4723 (base-path /wd/hub):
```powershell
appium --address 127.0.0.1 --port 4723 --base-path /wd/hub
```
5. Запустить:
```powershell
mvn test "-Dsurefire.suiteXmlFile=src/test/resources/testng-mobile.xml"
```
6. Примечание: если первый запуск показывает онбординг, тест его закрывает автоматически.

Чеклист PowerShell:
```powershell
netstat -ano | findstr :4723
```

## 6. Общий запуск
```bash
mvn test
```

## 7. На чем проверено
- Windows 10/11
- Chrome
- Android Emulator (например Android 12/13)
