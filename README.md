#### Для запуска проекта введите слишком команду (предварительно установив Maven):
```
mvn spring-boot:run
```
#### Локально приложение разворачивается на порту 8080 и доступно по ссылке http://localhost:8080
#### В приложении реализован один эндпойнт: 
- POST /api/v1/calcFrequency
--body: {"data":"ваша_строка"} 
--content-type:"application/json;charset=utf-8"

#### Все тесты находятся в папке test/java/ru/andrew, в файле TestControllerTest.java