package common

import parsers.Parser
import parsers.ParserFactory
import tools.Logger
import views.ImageView
import views.View
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Paths

/**
 * Created by Egor Nemchinov on 03.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class Controller {
    private var parser: Parser? = null
    private var view: View = ImageView() //default

    fun handleInput(command: String) {
        Logger.debugInfo("Controller.handlerInput($command) called")
        if(command.contains("open")) {
            val cmdSplit = command.split(" ")
            if(cmdSplit.size < 2) {
                Logger.warning("Specify file path.")
                return
            } else if(cmdSplit.size > 2) {
                Logger.warning("Too many arguments.")
            } else {
                var bytes: ByteArray
                try {
                    var path = Paths.get(cmdSplit[1])
                    bytes = Files.readAllBytes(path) //exception?
                } catch(e: NoSuchFileException) {
                    Logger.error("File not found.")
                    return
                }
                parser = ParserFactory.getParser(cmdSplit[1].split("/").last())
                if(parser != null) {
                    parser!!.viewObserver = ViewObserver(parser!!, view)
                    parser!!.parseFile(bytes.toTypedArray())
                }
            }
        }
        //may be open file and call ParserFactory
        //or close it, mb quit app
    }
}