import scala.util.Random._
import scala.math._
import scala.collection.mutable.ListBuffer
import javax.sound.midi.Sequence

trait State { 
  def mutate(): State
  def score(): Long
  def widen(): State
  def toInput(): String 
  def compile(): Unit
}

class SequenceState(
  val sequence: ListBuffer[Int]
) extends State {
  
  val excName = "seq"
  val fileName = "seq.cpp"
  val inputFileName = "input.txt"
  val outputFileName = "output.txt"

  def mutate(): State = 
    random() match {
      case x if x < 0.5 => 
        val index = nextInt(sequence.length)
        val newSequence = sequence.clone()
        newSequence(index) = nextInt(101) // 0 to 100
        new SequenceState(newSequence)
      case _ =>
        val idx = nextInt(sequence.length)
        val length = nextInt(ceil(sequence.length / 10.0).toInt)
        val newSequence = sequence.clone()
        var add = nextInt(11) - 5
        for 
          i <- idx until min(idx + length, sequence.length)
        do
          newSequence(i) = min(max(newSequence(i) + add, 0), 100)
        new SequenceState(newSequence)
    }   
  
  def score(): Long = {
    val input = toInput()
    val writer = new java.io.PrintWriter(new java.io.File(inputFileName))
    writer.write(input)
    writer.close()

    val (exitCode, runningTime) = ProcessRunner.runWithTimeout(s"./$excName < $inputFileName > $outputFileName", 5000)
    if (exitCode != 0) {
      throw new Exception(s"Error in running $excName: $exitCode, $runningTime ns")
    }
    
    // read a single Long from output.txt 
    val reader = new java.io.BufferedReader(new java.io.FileReader(outputFileName))
    val score = reader.readLine().toLong
    reader.close()
    
    score
  }
  
  def widen(): State = {
    val t = for 
      elem <- sequence
    yield
      val newElem = () => min(max(elem + nextInt(10) - 5,100), 100)
      ListBuffer(newElem(), newElem(), newElem(), newElem(), newElem())
    SequenceState(t.flatMap(x => x))
  }
  
  def toInput(): String = {
    val input = new StringBuilder
    input.append(sequence.length)
    input.append("\n")
    input.append(sequence.mkString(" "))
    input.append("\n")
    input.toString()
  }
  
  def compile(): Unit = {
    val (exitCode, runningTime) = ProcessRunner.runWithTimeout(s"g++ -o $excName $fileName", 1000)
    if(exitCode != 0) {
      throw new Exception("Error in compiling $excName")
    } else {
      println(s"Compilation time: ${runningTime / 1000000} ms")
    }
  }
  
  override def toString(): String = 
    s"(${sequence.mkString(" ")})"
}