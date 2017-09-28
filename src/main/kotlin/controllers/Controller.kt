package controllers

import common.ViewObserver

/**
 * Created by Egor Nemchinov on 08.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
interface Controller {
    var viewObserver: ViewObserver

    fun openFile(pathString: String)
}