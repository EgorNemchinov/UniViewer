package tools

import common.Controller

/**
 * Created by Egor Nemchinov on 04.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class InputThread(val controller: Controller) : Thread() {
    override fun run() {
        var input: String = ""
        while(input.trim() != "quit") {
            input = readLine() ?: continue
            controller.handleInput(input)
        }
    }
}