package common

import image.Image
import parsers.Parser
import views.View

/**
 * Created by Egor Nemchinov on 03.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class ViewObserver(var parser: Parser, var view: View) {
    fun drawImage(image: Image) {
        view.drawImage(image)
    }
}