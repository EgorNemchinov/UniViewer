package common

import image.Image
import parsers.Parser
import views.View

/**
 * Created by Egor Nemchinov on 03.05.17.
 * SPbU, 2017
 */
class ViewObserver(var parser: Parser?, var views: List<View> = emptyList<View>()) {
    constructor(parser: Parser?, view: View): this(parser, mutableListOf(view))

    fun addView(view: View) {
        views += view
    }

    fun drawImage(image: Image) {
        for(view in views)
            view.drawImage(image)
    }
}