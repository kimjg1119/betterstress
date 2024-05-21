import scala.sys.process._
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object ProcessRunner {

  def runWithTimeout(command: String, timeoutMillis: Long): (Int, Long) = {
    val startTime = System.nanoTime()

    val process = Process(command).run()

    // Future to wait for process completion
    val processFuture = Future {
      process.exitValue()
    }

    // Wait for the process to complete or timeout
    val result = try {
      Await.result(processFuture, timeoutMillis.millis)
    } catch {
      case _: TimeoutException =>
        process.destroy()
        -1557 // Indicate timeout
    }

    val endTime = System.nanoTime()
    val runningTime = endTime - startTime // Running time in nanoseconds

    (result, runningTime)
  }
}
