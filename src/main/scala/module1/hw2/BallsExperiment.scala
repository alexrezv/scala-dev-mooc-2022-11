package module1.hw2

import scala.util.Random

/**
 * В урне 3 белых и 3 черных шара. Из урны дважды вынимают по одному шару, не возвращая их обратно.
 * Найти вероятность появления белого шара при втором испытании (событие В),
 * если при первом испытании был извлечен черный шар (событие А).
 * <p>
 * Как будем делать:
 * <ol>
 * <li>создать класс с моделированием эксперимента, в нем должна быть коллекция (List) моделирующая урну с шариками (1 - белый шарик, 0 - черный шарик) и функция случайного выбора 2х шариков без возвращения (scala.util.Random), возвращать эта функция должна true (если первый шарик был черный, а второй белый) и false (в противном случае)</li>
 * <li>создать коллекцию обьектов этих классов, скажем 10000 элементов, и провести этот эксперимент (функция map)</li>
 * <li>посчитать количество элементов массива из пункта 2 где функция вернула true, это количество поделенное на общее количество элементов массива</li>
 * </ol>
 * PS: чем больше будет количество опытов в пункте 2, тем ближе будет результат моделирования к аналитическому решению
 * <p>
 * Критерии оценки:
 * <ol>
 * <li>Результат должен быть близок к ожидаемому</li>
 * <li>Использовать как можно меньше явных циклов, отдавая предпочтение высокоуровневым функциям, как то map, foreach, fold, filter</li>
 * <li>Используйте только стандартные библиотеки из базового набора</li>
 * </ol>
 */
class BallsExperiment {

  private sealed trait Ball {}

  private object BlackBall extends Ball {}

  private object WhiteBall extends Ball {}

  private val balls: List[Ball] = List(BlackBall, BlackBall, BlackBall, WhiteBall, WhiteBall, WhiteBall)

  def isFirstBlackSecondWhite(): Boolean = {
    val firstBallIndex = Random.between(0, 6)
    val firstBall = balls(firstBallIndex)
    val secondBall = balls.zipWithIndex.filter(_._2 != firstBallIndex)(Random.between(0, 5))._1
    (firstBall, secondBall) match {
      case (BlackBall, WhiteBall) => true
      case _ => false
    }
  }

}

object BallsTest {
  def main(args: Array[String]): Unit = {
    val count = 1_000_000
    val experiments: Seq[BallsExperiment] = (1 to count).map(_ => new BallsExperiment())
    val resultsOfExperiments = experiments.map(_.isFirstBlackSecondWhite())
    val countOfPositiveExperiments: Float = resultsOfExperiments.count(_ == true)
    println(countOfPositiveExperiments / count)
  }

}
