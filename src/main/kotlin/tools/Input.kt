package tools

import controllers.Controller

/**
 * Created by Egor Nemchinov on 04.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class Input(val controller: Controller) : Runnable {
    val INIT_STRING = """Type "help" to find out possible commands."""
    val HELP_STRING = """Help: To open file, type "open %fileName%, for example: "open lena.bmp" """

    init {
        println(INIT_STRING)
    }

    override fun run() {
        var input: String = ""
        while(input.trim() != "quit") {
            input = readLine() ?: continue
            if(input.trim().toLowerCase() == "help") {
                println(HELP_STRING)
                continue
            }
            if(input.contains("open")) {
                val cmdSplit = input.split(" ")
                if (cmdSplit.size < 2) {
                    Logger.warning("Specify file path.")
                    return
                } else if (cmdSplit.size > 2) {
                    Logger.warning("Too many arguments.")
                } else {
                    controller.openFile(cmdSplit[1])
                    continue
                }
            }
        }
    }
}