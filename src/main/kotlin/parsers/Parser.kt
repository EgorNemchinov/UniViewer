package parsers

import common.ViewObserver
import tools.Logger
import kotlin.experimental.and

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
        return (0..bytesAmount-1).sumBy { bytesArray[startIndex + it].toPositiveInt().shl(it *8)}
    }

    fun readBigEndian(bytesArray: Array<Byte>, startIndex: Int, bytesAmount:Int): Int{
        if(bytesArray.size < startIndex + bytesAmount) {
            Logger.debugInfo("File ended sooner than expected.")
        }
        return (0..bytesAmount-1).sumBy { bytesArray[startIndex+it].toPositiveInt().shl((bytesAmount - 1)*8) }
    }

    /** Returns number made of bits in (startIndex..startIndex+bitsAmount-1)*/
    fun partOfByte(byte: Int, startIndex: Int, bitsAmount: Int): Int {
        val byteWithoutBeginning = byte.and((1.shl(8-startIndex) - 1))
        return byteWithoutBeginning.shr(8-bitsAmount-startIndex)
    }

    //helps to avoid negative byte problem (255.toByte() is -128 => -128.toInt() is -128)
    fun Byte.toPositiveInt() = toInt() and 0xFF
}