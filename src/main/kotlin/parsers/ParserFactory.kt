package parsers

import parsers.Parser
import tools.Logger

/**
 * Created by Egor Nemchinov on 03.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class ParserFactory {
    //or just extension

    companion object {
        /** Depending on file name determines it's type
         *  and returns an appropriate parser.
         *  This is only called from controller
         *  if such file exists.
         */
        fun getParser(fileName: String): Parser? {
            Logger.debugInfo("ParserFactory($fileName) called.")
            var splitByDot = fileName.split(".")
            if(splitByDot.size != 2) {
                Logger.error("Incorrect file format.")
                return null
            }
            when(splitByDot[1]) {
                "bmp" -> return BmpParser()
                else -> return null
            }
        }
    }
}