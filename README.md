# KtWordle - Модель игры Wordle и её решатель
Суть и правила игры: https://wordle.belousov.one/
### Цель проекта:
Разработать алгоритм, позволяющий максимально сократить количество попыток угадать слово на любом шаге.
Использовать инструменты статистики и теории вероятности для решения.

Реализация:
-----------
Решение задачи представлено классом WordAnalyzer и его методами.  
При вводе игроком очередного слова вызываются последовательно функции:  
> analyzeWord() - Сравнивает введенное слово с ответом. На основе чего оставляет комбинацию подошедших/имеющихся/отсутствующих букв
>
> analyzeEntropy() - Составляет список 5 слов, дающих наибольшую энтропию после введения слова.
>> buildMatches() - Отбирает только те слова из возможных, которые подходят текущей комбинации 
>>
>> getE() - Получает энтропию для слова по ранее отфильтрованному списку (см. buildMatches())  
>>> buildPos() - Аналогично buildMatches() отбирает слова подошедшие под конкретную комбинацию. Возвращает количество таких слов.

Наибольший интерес представляет функция getE(). Именно здесь происходит основной анализ будущих подсказок.  
Так как решатель apriori не знает ответа, но необходимо как можно скорее прийти к нему, то следует получить
как можно больше информации из произошедшего события. и, по возможности, отбросить как можно больше малоинформативных слов. 
Для этой цели оценивается информационная энтропия слов из списка.


Теоретическая часть:
--------------------
  Информационная энтропия - это средняя скорость генерирования значений 
некоторым случайным источником данных. Величина информационной энтропии, 
связанная с определенным значением данных, вычисляется по формуле:
![image](https://user-images.githubusercontent.com/70843205/195662602-f89c9ed7-8021-49f0-bee8-ace39bf2a4d8.png) 
где p(x) - вероятность состояния x некоторой системы.

  Когда источник данных генерирует значение, имеющее низкую вероятность (т.е. 
когда происходит маловероятное событие), с ним связана большая 
информация, чем с более вероятным событием. Количество информации, 
выражаемое событием, связанным с появлением определенного значения данных, 
можно рассматривать как случайную переменную, математические ожидание 
которой и равно информационной энтропии. Чем ближе распределение к 
равномерному, тем выше энтропия.  
  Для нашего случая информационная энтропия позволяет оценить - как много слов по всем возможным комбинациям 
в игре может отбросить введенное игроком слово.

Вычисление энтропии слова и получение слов-подсказок
----------------------------------------------------
```
fun getE(word: String): Double{
        var entropy = 0.0
        val illegalVal = arrayOf(242,241,239,233,215,161)
        for (i in 0 until 243) {
            if (illegalVal.contains(i)) continue
            val pos = buildPos(word, conv3(i)) / matchList.size
            if (pos != 0.0)
                entropy += pos*(log(1/pos,2.0))
        }
        return entropy
}
```
После получения энтропии очередного слова, составляется список 5 слов по убыванию значений информационной энтропии.
