package actors

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import akka.routing.SmallestMailboxPool
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor.ActorRef

case class CreateRemoteRequest(
        sourceActor: ActorRef
)

class Master() extends Actor with ActorLogging {
    def receive() = {
        case "init" => {}
        case crr: CreateRemoteRequest => {
            println("[Master] Creating remote actor B")
            // Create ActorB and send its ref back to ActorA. Here we make a pool of actors
            sender ! Akka.system.actorOf(
                SmallestMailboxPool(1).props(
                    Props(classOf[actors.ActorB], crr.sourceActor)
                ), name = "ActorB" + java.util.UUID.randomUUID().toString
            )
        }
        case _ => println("[Master] Got an unexpected message")
    }
}