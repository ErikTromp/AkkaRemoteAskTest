import play.api.GlobalSettings
import play.api.Application
import akka.actor.Props
import akka.routing.SmallestMailboxPool
import actors.Master
import play.api.libs.concurrent.Akka
import play.api.Play.current
import actors.ActorA

object Global extends GlobalSettings {
    override def onStart(app: Application) {
        // First create master
        val masterActor = Akka.system.actorOf(
            SmallestMailboxPool(5).props(Props(classOf[Master])), name = "Master")
        masterActor ! "init"
        
        // Now create actor A
        val actorA = Akka.system.actorOf(Props[ActorA], name = "ActorA")
        // Let actor A ask something to B
        actorA ! "ask"
    }
}