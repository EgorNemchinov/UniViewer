package controllers

import common.ViewObserver
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
class MainController : Controller{
    private var parser: Parser? = null
    private var view: View = ImageView() //default

    override fun handleInput(input: String) {
        Logger.debugInfo("MainController.handlerInput($input) called")
    }

    override fun openFile(pathString: String) {
        parser = ParserFactory.getParser(pathString.split("/").last())
        if(parser != null) {
            val bytes: ByteArray
            try {
                val path = Paths.get(pathString)
                bytes = Files.readAllBytes(path) //exception?
            } catch(e: NoSuchFileException) {
                Logger.error("File not found.")
                return
            }
            parser!!.viewObserver = ViewObserver(parser!!, view)
            parser!!.parseFile(bytes.toTypedArray())
        }
    }
}