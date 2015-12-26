package actors

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorRef
import play.api.libs.iteratee.Iteratee
import play.api.libs.iteratee.Enumeratee
import play.api.libs.iteratee.Enumerator
import scala.concurrent.ExecutionContext.Implicits.global

class ActorB(sourceActor: ActorRef) extends Actor with ActorLogging {
    println("[ActorB] I am called: " + self)
    
    val sinkIteratee: Iteratee[String, Unit] = Iteratee.ignore
    
    class senderReturner(str: String, sourceActor: ActorRef) {
        // Create enumeratee that will send back
        val sendBackEnum: Enumeratee[String, String] = Enumeratee.map((s: String) => {
            println("Sending it back to: " + sourceActor)
            sourceActor ! s
            s
        })
        
        def run() = {
            Enumerator(str) |>> sendBackEnum &>> sinkIteratee
        }
    }
    
    def receive() = {
        case "ask" => {
            println("[ActorB] Got a message from: " + sender)
            println("[ActorB] Source actor is: " + sourceActor)
            
            // Make it send back using our Enumeratee
            val sr = new senderReturner("I got your ask!", sender)
            sr.run
        }
        case _ => println("[ActorB] Got an unexpected message")
    }
}