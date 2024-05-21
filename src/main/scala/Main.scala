import scala.collection.mutable.ListBuffer
import scala.util.Random._

@main def main: Unit = 

  val randomSequence = ListBuffer.fill(10)(nextInt(100))
  val initState = SequenceState(randomSequence)
  
  val sa = SimulatedAnnealing(initState, 0.99995)
  sa.runWithWidening(5) // 5^5 = 3125
  
  println(s"Score: ${sa.state.score()} (count)")
  val file = "result.txt"
  val writer = new java.io.PrintWriter(file)
  writer.write(sa.state.toString())
  writer.close()
  
  val rand = ListBuffer({
    val x = for 
      i <- 1 to sa.state.asInstanceOf[SequenceState].sequence.length
    yield nextInt(100)
    x.toList
  }:_*)
  println(s"RandomScore: ${SequenceState(rand).score()} (count)")  
