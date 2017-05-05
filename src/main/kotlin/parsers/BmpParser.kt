package parsers

import common.ViewObserver
import image.BmpInfo
import image.Image
import image.Pixel
import tools.Logger
import views.ImageView
import java.awt.Color

/**
 * Created by Egor Nemchinov on 03.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class BmpParser: Parser {
    override var viewObserver: ViewObserver = ViewObserver(this, ImageView())

    override fun parseFile(bytes: Array<Byte>) {
        val bmpInfo = BmpInfo()

        if(intEqualsString(readLittleEndian(bytes, 0, 2), "BM")) {
            Logger.debugInfo("Current file is BMP, checked type in headInfo.")
        } else {
            Logger.error("File's extension doesn't match it's content.")
            return
        }

        parseHeader(bytes, bmpInfo)
        Logger.debugInfo("BmpInfo finished. Here it is: \n \t$bmpInfo")

        val colorTable: Array<Color> = createColorTable(bytes, bmpInfo)
        //there is another case of using colorTable which is not considered

        val pixels: Array<Array<Pixel>> = createPixelArray(bytes, bmpInfo, colorTable)

        val image: Image = Image(bmpInfo, pixels)
        viewObserver.drawImage(image)
    }

    fun parseHeader(bytes: Array<Byte>, bmpInfo: BmpInfo) {
        bmpInfo.fileSize = readLittleEndian(bytes, 2, 4)
        bmpInfo.pixelDataOffset = readLittleEndian(bytes, 10, 4)
        //offset to pixelArray we aren't using

        //DIB Header:
        bmpInfo.headerSize = readLittleEndian(bytes, 14, 4)
        when(bmpInfo.headerSize) {
            12 -> {
                bmpInfo.width = readLittleEndian(bytes, 18, 2)
                bmpInfo.height = readLittleEndian(bytes, 20, 2)
                bmpInfo.bitsPerPixel = readLittleEndian(bytes, 24, 2)
            }
            40 -> {
                bmpInfo.width = readLittleEndian(bytes, 18, 4)
                bmpInfo.height = readLittleEndian(bytes, 22, 4)
                bmpInfo.bitsPerPixel = readLittleEndian(bytes, 28, 2)
                bmpInfo.compression = BmpInfo.CompressionType.getByValue(readLittleEndian(bytes, 30, 4))
                bmpInfo.pixelDataSize = readLittleEndian(bytes, 34, 4)
                bmpInfo.xPixelsPerMeter = readLittleEndian(bytes, 38, 4)
                bmpInfo.yPixelsPerMeter = readLittleEndian(bytes, 42, 4)
                bmpInfo.colorsUsed = readLittleEndian(bytes, 46, 4)
                bmpInfo.colorsImportant = readLittleEndian(bytes, 50, 4)
                //ignored field - biPlanes
            }
            108 -> TODO("108th BMP version is not implemented.")
            124 -> TODO("124th BMP version is not implemented.")
        }
    }

    private fun createPixelArray(bytes: Array<Byte>, bmpInfo: BmpInfo, colorTable: Array<Color>): Array<Array<Pixel>> {
        val pixels: Array<Array<Pixel>> = Array(bmpInfo.width) {Array<Pixel>(bmpInfo.height) {Pixel(Color.RED)}}
        //TODO: parse pixels data
        return pixels
    }

    private fun createColorTable(bytes: Array<Byte>, bmpInfo: BmpInfo): Array<Color> {
        var colorTable: Array<Color> = Array(0) {Color.BLACK}
        if(bmpInfo.bitsPerPixel <= 8) {
            colorTable = Array(1.shl(bmpInfo.bitsPerPixel)) {Color.BLACK}

            val startingByte: Int
            when(bmpInfo.headerSize) {
                12 ->startingByte = 26
                40 ->{
                    when(bmpInfo.compression) {
                        BmpInfo.CompressionType.BI_BITFIELDS -> startingByte = 66
                        BmpInfo.CompressionType.BI_ALPHABITFIELDS -> startingByte = 70
                        else -> startingByte = 54
                    }
                }
                108 -> startingByte = 122
                124 -> startingByte = 136
                else -> startingByte = 54
            }

            val bytesPerColor = 4 //RGBQUAD
            for(i in 0..colorTable.size) {
                val red = bytes[startingByte + i*bytesPerColor].toInt()
                val green = readLittleEndian(bytes, startingByte + i*bytesPerColor + 1, 1)
                val blue = readLittleEndian(bytes, startingByte + i*bytesPerColor + 2, 1)
                colorTable += Color(if(red < 0) red + 256 else red,
                                    if(green < 0) green + 256 else green,
                                    if(blue < 0) blue + 256 else blue)
            }
            //OP, vnezapnoya paskhalochka
            //kak u vas dela?
        }
        return colorTable
    }
}

private fun intEqualsString(bytesToInt: Int, string: String): Boolean {
    var charIndex = 0
    val sum = (string).sumBy { it.toInt().shl(8*charIndex++) }
//    Logger.debugInfo("intEqualsString($bytesToInt, \"$string\") called. Comparing $bytesToInt and $sum")
    return sum == bytesToInt
}