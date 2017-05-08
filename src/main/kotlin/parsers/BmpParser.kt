package parsers

import common.ViewObserver
import image.BmpInfo
import image.Image
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

        val pixels: Array<Array<Color>> = createPixelArray(bytes, bmpInfo, colorTable)

        val image: Image = Image(bmpInfo, pixels)
        viewObserver.drawImage(image)
    }

    fun parseHeader(bytes: Array<Byte>, bmpInfo: BmpInfo) {
        bmpInfo.fileSize = readLittleEndian(bytes, 2, 4)
        bmpInfo.pixelDataOffset = readLittleEndian(bytes, 10, 4)

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

    private fun createColorTable(bytes: Array<Byte>, bmpInfo: BmpInfo): Array<Color> {
        var colorTable: Array<Color> = Array(bmpInfo.colorsUsed) {Color.BLACK}
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
            for(i in 0..colorTable.size-1) {
                val blue = readLittleEndian(bytes, startingByte + i*bytesPerColor, 1)
                val green = readLittleEndian(bytes, startingByte + i*bytesPerColor + 1, 1)
                val red = bytes[startingByte + i*bytesPerColor + 2].toInt()
                colorTable[i] =  Color(if(red < 0) red + 256 else red,
                        if(green < 0) green + 256 else green,
                        if(blue < 0) blue + 256 else blue)
//                Logger.debugInfo("$i th color is ${colorTable[i]}")
            }
            //OP, vnezapnoya paskhalochka
            //kak u vas dela?
        }
        Logger.debugInfo("ColorTable size is ${colorTable.size}")
        return colorTable
    }

    private fun createPixelArray(bytes: Array<Byte>, bmpInfo: BmpInfo, colorTable: Array<Color>): Array<Array<Color>> {
        val pixels: Array<Array<Color>> = Array(bmpInfo.width) {Array(bmpInfo.height) {Color.RED}}
        if(bmpInfo.headerSize == 12 || bmpInfo.compression == BmpInfo.CompressionType.BI_RGB ||
        bmpInfo.compression == BmpInfo.CompressionType.BI_BITFIELDS || bmpInfo.compression == BmpInfo.CompressionType.BI_ALPHABITFIELDS) {
            Logger.debugInfo("Parsing pixel data.")
            //array[][]
            var byteNum = 0
            var rowByteWidth = Math.ceil(bmpInfo.width*bmpInfo.bitsPerPixel / 8.toDouble()).toInt()
            if(rowByteWidth % 4 != 0)
                rowByteWidth += (4 - (rowByteWidth % 4))
            var x: Int = 0
            var y: Int = bmpInfo.height - 1
            while(y >= 0 && byteNum < bmpInfo.pixelDataSize) {
                if(bmpInfo.bitsPerPixel <= 8){
                    val pixelsInByte = 8 / bmpInfo.bitsPerPixel
                    val colorIndex = partOfByte(readLittleEndian(bytes, bmpInfo.pixelDataOffset + byteNum, 1),
                            ((x % (pixelsInByte))*bmpInfo.bitsPerPixel), bmpInfo.bitsPerPixel)
//                    Logger.debugInfo("Parsing pixels. x $x, y $y, byteNum $byteNum, color - ${colorTable[colorIndex]}")
                    pixels[x][y] = colorTable[colorIndex]
                    x++
                    byteNum += if(x % (pixelsInByte) == 0) 1 else 0
                    if(x % bmpInfo.width == 0) {
                        if(byteNum % rowByteWidth != 0)
                            byteNum += (rowByteWidth - (byteNum % rowByteWidth))
                        x = 0
                        y--
                    }
                } else if((bmpInfo.bitsPerPixel == 32 || bmpInfo.bitsPerPixel == 24)
                        && bmpInfo.compression != BmpInfo.CompressionType.BI_BITFIELDS
                        && bmpInfo.compression != BmpInfo.CompressionType.BI_ALPHABITFIELDS) {
                    //no bit masks, byte per r g & b
                    val blue: Int = readLittleEndian(bytes, bmpInfo.pixelDataOffset + byteNum , 1)
                    val green: Int = readLittleEndian(bytes, bmpInfo.pixelDataOffset + byteNum + 1, 1)
                    val red: Int = readLittleEndian(bytes, bmpInfo.pixelDataOffset + byteNum + 2, 1)
                    val alpha: Int = if(bmpInfo.bitsPerPixel == 32) readLittleEndian(bytes, bmpInfo.pixelDataOffset +  byteNum + 3, 1) else 255
                    pixels[x][y] = Color(red, green, blue, alpha)
                    x++
                    byteNum += bmpInfo.bitsPerPixel / 8
                    if(x % bmpInfo.width == 0) {
                        x = 0
                        y--
                        if(byteNum % rowByteWidth != 0)
                            byteNum += (rowByteWidth - (byteNum % rowByteWidth))
                    }
//                    TODO("Not implemented pixel data parsing with 16 or more bits per pixel.")
                } else {
                    TODO("Not implemented pixel data parsing with 16 or more bits per pixel.")
                }
            }
        } else {
            TODO("Not implemented parsing pixel data with other compression algorithms")
        }
        return pixels
    }

    private fun intEqualsString(bytesToInt: Int, string: String): Boolean {
        var charIndex = 0
        val sum = (string).sumBy { it.toInt().shl(8*charIndex++) }
//    Logger.debugInfo("intEqualsString($bytesToInt, \"$string\") called. Comparing $bytesToInt and $sum")
        return sum == bytesToInt
    }
}