package parsers

import common.ViewObserver
import java.io.File

/**
 * Created by Egor Nemchinov on 03.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
interface Parser {
    var viewObserver: ViewObserver

    fun parseFile(bytes: Array<Byte>)
}