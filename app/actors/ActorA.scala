package actors

import akka.actor.ActorLogging
import akka.actor.Actor
import play.api.Play
import scala.concurrent.Await
import play.api.libs.concurrent.Akka
import scala.concurrent.Future
import akka.actor.ActorRef
import akka.pattern.ask
import scala.concurrent.duration.DurationInt
import play.api.Play.current
import akka.util.Timeout

class ActorA() extends Actor with ActorLogging {
    implicit val timeout = Timeout(5 seconds)
    
    // Set up the remote actor, being actor B
    val location = "akka.tcp://application@" +
        Play.current.configuration.getString("remotelocation").getOrElse("") + ":" +
        Play.current.configuration.getString("remoteport").getOrElse("") +
        "/user/Master"
    val remoteActor = Await.result((Akka.system.actorSelection(location) ?
            new CreateRemoteRequest(self)).asInstanceOf[Future[ActorRef]], timeout.duration)
    println("[ActorA] I am called: " + self)
    println("[ActorA] I got the remote ActorB: " + remoteActor)
    
    def receive() = {
        case "ask" => {
            // Perform the ask to actor B
            val result = Await.result(remoteActor ? "ask", timeout.duration) 
            println("[ActorA] I got the result we were after -- " + result)
        }
        case _ => println("[ActorA] Got an unexpected message")
    }
}