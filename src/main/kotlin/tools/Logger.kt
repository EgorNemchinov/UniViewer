package tools

/**
 * Created by Egor Nemchinov on 03.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class Logger {
    companion object {
        var debug: Boolean = false

        fun debugInfo(s: String) {
            if(debug)
                println("Debug: $s")
        }

        fun error(s: String) {
            println("Error: $s")
        }

        fun warning(s: String) {
            println("Warning: $s")
        }

        fun debugEnable() {
            debug = true
        }

        fun debugDisable() {
            debug = false
        }
    }
}