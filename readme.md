# Алгоритм для создания карты сайта

## Описание приложения
Данный проект выполнялся мной во время обучения.  
Приложение даёт возможность составить карту сайта.  

**Особенности:**
- Используется библиотека `JSOUP`
- Используется `*.concurrent.ForkJoinPool` для получения данных и записи в файл

#### Входные данные
1. URL для получения карты устанавливается в классе `Main` 
в переменной `private static final String URL`
2. Пусть к файлу с результатом указывается в переменной
`private static final String SITE_MAP_PATH` 

#### Выходные данные
Текстовый файл будет содержать иерархию набора url адресов, как вложенный список.  
Каждый вложенный уровень отделяется дополнительным знаком табуляции, как в примере ниже.  
Несколько примеров содержаться в папке [data/](./data) данного репозитория.
```
https://lenta.ru/
	https://lenta.ru/news/2022/09/12/spadanet/
	https://lenta.ru/rubrics/sport/
		https://lenta.ru/rubrics/sport/other/
			https://lenta.ru/rubrics/sport/other/2/
		https://lenta.ru/rubrics/sport/auto/
			https://lenta.ru/rubrics/sport/auto/2/
   ...
```
