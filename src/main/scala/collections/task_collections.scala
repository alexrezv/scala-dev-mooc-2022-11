package collections

object task_collections {

  def isASCIIString(str: String): Boolean = str.matches("[A-Za-z]+")

  /**
   * Реализуйте метод который первый элемент списка не изменяет, а для последующих алгоритм следующий:
   * <ul>
   * <li>если isASCIIString is TRUE тогда пусть каждый элемент строки будет в ВЕРХНЕМ регистре</li>
   * <li>если isASCIIString is FALSE тогда пусть каждый элемент строки будет в нижнем регистре</li>
   * </ul>
   * Пример:<br>
   * capitalizeIgnoringASCII(List("Lorem", "ipsum" ,"dolor", "sit", "amet")) -> List("Lorem", "IPSUM", "DOLOR", "SIT", "AMET")<br>
   * capitalizeIgnoringASCII(List("Оказывается", "," "ЗвУк", "КЛАВИШ", "печатной", "Машинки", "не", "СТАЛ", "ограничивающим", "фактором")) ->
   * List("Оказывается", "," "звук", "КЛАВИШ", "печатной", "машинки", "не", "стал", "ограничивающим", "фактором")<br>
   * HINT: Тут удобно использовать collect и zipWithIndex
   *
   */
  def capitalizeIgnoringASCII(text: List[String]): List[String] = {
    text.head +: text.tail.map(it => (it, isASCIIString(it))).collect {
      case (str, false) => str.toLowerCase()
      case (str, true) => str.toUpperCase()
    }
  }

  /**
   *
   * Компьютер сгенерировал текст используя вместо прописных чисел, числа в виде цифр, помогите компьютеру заменить цифры на числа
   * В тексте встречаются числа от 0 до 9
   *
   * Реализуйте метод который цифровые значения в строке заменяет на числа: 1 -> one, 2 -> two
   *
   * HINT: Для всех возможных комбинаций чисел стоит использовать Map
   */
  def numbersToNumericString(text: String): String = {
    ""
  }

  /**
   *
   * У нас есть два дилера со списками машин которые они обслуживают и продают (case class Auto(brand: String, model: String)).<br>
   * Базы данных дилеров содержат тысячи и больше записей. Нет гарантии что записи уникальные и не имеют повторений.<br>
   * HINT: Set<br>
   * HINT2: Iterable стоит изменить
   */

  case class Auto(brand: String, model: String)

  /**
   * Хотим узнать какие машины можно обслужить учитывая этих двух дилеров.<br>
   * Реализуйте метод который примет две коллекции (два источника) и вернёт объединенный список уникальный значений.
   */
  def intersectionAuto(dealerOne: Iterable[Auto], dealerTwo: Iterable[Auto]): Iterable[Auto] = {
    Iterable.empty
  }

  /**
   * Хотим узнать какие машины обслуживается в первом дилеромском центре, но не обслуживаются во втором.<br>
   * Реализуйте метод который примет две коллекции (два источника)
   * и вернёт уникальный список машин обслуживающихся в первом дилерском центре и не обслуживающимся во втором.
   */
  def filterAllLeftDealerAutoWithoutRight(dealerOne: Iterable[Auto], dealerTwo: Iterable[Auto]): Iterable[Auto] = {
    Iterable.empty
  }

}