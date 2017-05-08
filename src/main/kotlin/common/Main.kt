package common

import controllers.MainController
import tools.Input

/**
 * Created by Egor Nemchinov on 04.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
fun main(args: Array<String>) {
    Thread(Input(MainController())).start()
}

