package parsers

import common.ViewObserver
import tools.Logger

/**
 * Created by Egor Nemchinov on 03.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
interface Parser {
    var viewObserver: ViewObserver

    fun parseFile(bytes: Array<Byte>)

    //thx to Sergey Gorbatuk for .shl() idea C;
    fun readLittleEndian(bytesArray: Array<Byte>, startIndex: Int, bytesAmount:Int): Int{
        if(bytesArray.size < startIndex + bytesAmount) {
            Logger.debugInfo("File ended sooner than expected.")
            //FIXME: stop parsing file if it happened, mb using thread.interrupt or smth
        }
        return (0..bytesAmount-1).sumBy { bytesArray[startIndex+it].toInt().shl(it *8) }
    }

    fun readBigEndian(bytesArray: Array<Byte>, startIndex: Int, bytesAmount:Int): Int{
        if(bytesArray.size < startIndex + bytesAmount) {
            Logger.debugInfo("File ended sooner than expected.")
        }
        return (0..bytesAmount-1).sumBy { bytesArray[startIndex+it].toInt().shl((bytesAmount - 1)*8) }
    }
}