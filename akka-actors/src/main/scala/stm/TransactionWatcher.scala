/**
 * Copyright (C) 2009 Scalable Solutions.
 */

package se.scalablesolutions.akka.stm

/*
import kernel.util.Logging
import org.apache.zookeeper.jmx.ManagedUtil
import org.apache.zookeeper.server.persistence.FileTxnSnapLog
import org.apache.zookeeper.server.{ServerConfig, NIOServerCnxn}
import org.apache.zookeeper.{KeeperException, WatchedEvent, Watcher, ZooKeeper, DataMonitor}
*/
/**
 * @author <a href="http://jonasboner.com">Jonas Bon&#233;r</a>
 *
class TransactionWatcher extends Logging with Watcher {

  val SERVER_URL = "localhost"

  val ZOO_KEEPER_URL = SERVER_URL
  val ZOO_KEEPER_PORT = 2181
  val znode = "master"

  private[this] val db = new scala.collection.mutable.HashMap[String, String]

  private[this] val zk = new ZooKeeper(ZOO_KEEPER_URL + ":" + ZOO_KEEPER_PORT, 3000, this)
  private[this] val dm = new DataMonitor(zk, znode, null, this)

  override def process(event: WatchedEvent) = {
    log.debug("New ZooKeeper event: %s", event)
    val path = event.getPath();
    if (event.getType == Event.EventType.None) {
      // We are are being told that the state of the connection has changed
      event.getState match {
        case SyncConnected =>
                  // In this particular example we don't need to do anything
                  // here - watches are automatically re-registered with
                  // server and any watches triggered while the client was
                  // disconnected will be delivered (in order of course)
        case Expired =>
          dead = true
          listener.closing(KeeperException.Code.SessionExpired)
      }
    } else {
      if (path != null && path.equals(znode)) {
        // Something has changed on the node, let's find out
        zk.exists(znode, true, this, null)
      }
    }
    if (chainedWatcher != null) chainedWatcher.process(event);
  }



  def run: Unit = synchronized {
    try {
        while (!dm.dead) wait
    } catch {
      case e: InterruptedException => Thread.currentThread.interrupt
    }
  }

  def closing(rc: Int): Unit = synchronized { notifyAll() }
}

 */
object TransactionWatcher {
  def main(args: Array[String]): Unit = {
    println("Connecting to ZooKeeper...")
    //new TransactionWatcher
  }
}

  // private[akka] def startZooKeeper = {
  //   try {
  //     ManagedUtil.registerLog4jMBeans
  //     ServerConfig.parse(args)
  //   } catch {
  //     case e: JMException => log.warning("Unable to register log4j JMX control: s%", e)
  //     case e => log.fatal("Error in ZooKeeper config: s%", e)
  //   }
  //   val factory = new ZooKeeperServer.Factory() {
  //     override def createConnectionFactory = new NIOServerCnxn.Factory(ServerConfig.getClientPort)
  //     override def createServer = {
  //       val server = new ZooKeeperServer
  //       val txLog = new FileTxnSnapLog(
  //         new File(ServerConfig.getDataLogDir),
  //         new File(ServerConfig.getDataDir))
  //       server.setTxnLogFactory(txLog)
  //       server
  //     }
  //   }
  //   try {
  //     val zooKeeper = factory.createServer
  //     zooKeeper.startup
  //     log.info("ZooKeeper started")
  //     // TODO: handle clean shutdown as below in separate thread
  //     // val cnxnFactory = serverFactory.createConnectionFactory
  //     // cnxnFactory.setZooKeeperServer(zooKeeper)
  //     // cnxnFactory.join
  //     // if (zooKeeper.isRunning) zooKeeper.shutdown
  //   } catch { case e => log.fatal("Unexpected exception: s%",e) }
  // }

