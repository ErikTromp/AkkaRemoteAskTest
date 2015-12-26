package actors

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorRef

class ActorB(sourceActor: ActorRef) extends Actor with ActorLogging {
    println("[ActorB] I am called: " + self)
    
    def receive() = {
        case "ask" => {
            println("[ActorB] Got a message from: " + sender)
            println("[ActorB] Source actor is: " + sourceActor)
            sender ! "I got your message"
            // Note: sourceActor ! "I got your message" doesn't work, even locally
        }
        case _ => println("[ActorB] Got an unexpected message")
    }
}