import scala.math._

class SimulatedAnnealing(
    var state: State,
    val decay: Double
) {
  
  val BOLTZMANN_CONSTANT = 20.0
  
  private var temp = 1.0
  var debugCnt = 0
  var boltzmann = 1.0
  
  private def init() =  {
    temp = 1.0
    debugCnt = 0
  }

  private def debug(str: String): Unit =
    if (debugCnt % 1000 == 0) println(str)

  private def hammer: Unit = {
    debugCnt += 1

    val beforeState = state
    val nextState = state.mutate()

    val beforeScore = beforeState.score()
    val nextScore = nextState.score()

    val log = (x: Double) => log10(x) / log10(1.1)

    debug(s"Current Score: $beforeScore (count)")
    debug(s"Next Score: $nextScore (count)")
    debug(s"Temp: $temp")

    val prob = exp((nextScore - beforeScore) / (boltzmann * temp))
    if (prob > random) {
      debug(s"Prob: $prob, Accept")
      // if(prob < 1) println(s"Even prob is $prob, Accept")
      state = nextState
    } else debug(s"Prob: $prob, Decline")
    debug(s"---------------------")

    temp *= decay
  }

  def run: State = {
    init()
    state.compile()
    boltzmann = state.score() / BOLTZMANN_CONSTANT 
    while (temp > 0.1) {
      hammer
    }
    println(s"Final State: ${state}")
    println(s"Final Score: ${state.score()}")
    state
  }
  
  def runWithWidening(count: Int): State = {
    run
    for (_ <- 0 until count) {
      state = state.widen()
      run
    }
    state
  }
}
