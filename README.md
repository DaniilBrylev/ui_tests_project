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
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-web.xml
```

## 5. Как запустить Mobile
1. Установить Android Studio, создать и запустить эмулятор.
2. Установить Wikipedia из Google Play (или APK).
3. Запустить Appium Server на 127.0.0.1:4723.
4. Запустить:
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-mobile.xml
```
5. Примечание: если первый запуск показывает онбординг, тест его закрывает автоматически.

## 6. Общий запуск
```bash
mvn test
```

## 7. На чем проверено
- Windows 10/11
- Chrome
- Android Emulator (например Android 12/13)
