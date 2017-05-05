package parsers

import common.ViewObserver
import image.BmpInfo
import image.Image
import views.ImageView
import java.io.File

/**
 * Created by Egor Nemchinov on 03.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class BmpParser: Parser {
    override var viewObserver: ViewObserver = ViewObserver(this, ImageView())

    override fun parseFile(bytes: Array<Byte>) {
        var image: Image = Image(BmpInfo())

        //parsing itself
        viewObserver.drawImage(image)

    }
}