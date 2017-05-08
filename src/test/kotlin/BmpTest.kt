/**
 * Created by Egor Nemchinov on 08.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */

import controllers.Controller
import controllers.MainController
import image.Image
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import views.View
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

class BmpTest : View{
    var controller: Controller = MainController()

    var currentImage: Image? = null
    var newImageChecked = false

    override fun drawImage(image: Image) {
        println("Image received.")
        currentImage = image
        newImageChecked = true
    }

    @Before
    fun prepare() {
        println("Starting tests.")
        controller.viewObserver.addView(this)
    }

    @Test
    fun bmp8BitsAndLessTest() {
        val folder: File = File("${System.getProperty("user.dir")}/src/main/resources/bmp/")
        for(fileEntry: String in folder.list({ _, string -> string.contains("8bit")})) {
            val fullPath = "${folder.absolutePath}/$fileEntry"
            doesFileMatch(Paths.get(fullPath))
        }
    }

    @Test
    fun bmp16BitsAndMoreTest() {
        val folder: File = File("${System.getProperty("user.dir")}/src/main/resources/bmp/")
        for(fileEntry: String in folder.list({ _, string -> string.contains("24bit") || string.contains("32bit")})) {
            val fullPath = "${folder.absolutePath}/$fileEntry"
            assertTrue(doesFileMatch(Paths.get(fullPath)))
        }
    }

    fun doesFileMatch(fullPath: Path): Boolean {
        controller.openFile(fullPath.toString())
        println("Waiting for image \'${fullPath.fileName}\"")
        while(!newImageChecked) {} //receiving parsed image
        var doMatch = currentImage == Image(File(fullPath.toString()))
        println("Do they match? $doMatch")
        newImageChecked = false
        assertTrue(doMatch)
        return doMatch
    }
}