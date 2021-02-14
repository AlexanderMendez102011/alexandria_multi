package kernel

import `object`.Server
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import database.Database
import exchange.ExchangeServer
import kernel.EmulatorInfo
import login.LoginServer
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.*

object Main {
    @JvmField
    val database = Database()
    private val logger =
        LoggerFactory.getLogger(Main::class.java) as Logger

    @JvmStatic
    fun main(arg: Array<String>) {
        try {
            System.setOut(PrintStream(System.out, true, "IBM850"))
            File("Logs").mkdir()
            File("Logs/Error").mkdir()
            System.setErr(PrintStream(FileOutputStream("Logs/Error/err - " + System.currentTimeMillis() + ".txt")))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        start()
    }

    private fun start() {
        logger.info(
            "You use " + System.getProperty("java.vendor") + " with the version " + System.getProperty(
                "java.version"
            )
        )
        Console.instance = Console()
        Logging.getInstance().initialize()
        Logging.getInstance().write("Login", "starting")
        logger.debug(EmulatorInfo.HARD_NAME.toString())
        Config.verify("config.properties")
        database.initializeConnection()
        logger.level = Level.OFF
        Console.instance.write(" > Creation of connexion server.")
        database.serverData.load(null)
        Config.exchangeServer = ExchangeServer()
        Config.exchangeServer.start()
        Config.loginServer = LoginServer()
        Config.loginServer.start()
        val root =
            LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        root.level = Level.OFF
        Console.instance.write(" > The login server started in " + (System.currentTimeMillis() - Config.startTime) + " ms.")
        Config.isRunning = true
        Console.instance.initialize()
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                Server.servers.values.stream()
                    .filter { server: Server? -> server != null && server.client != null }
                    .forEach { obj: Server -> obj.setFreePlaces() }
            }
        }, 1000 * 30.toLong(), 1000 * 30.toLong())
    }

    @JvmStatic
    fun exit() {
        Console.instance.write(" > The server going to be closed.")
        Logging.getInstance().write("Login", "exiting")
        Logging.getInstance().stop()
        if (Config.isRunning) {
            Config.isRunning = false
            Config.loginServer.stop()
            Config.exchangeServer.stop()
            Console.instance.interrupt()
        }
        Console.instance.write(" > The emulator is now closed.")
    }

    init {
        Runtime.getRuntime().addShutdownHook(Thread(Runnable { exit() }))
    }
}