package image

/**
 * Created by Egor Nemchinov on 04.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class BmpInfo(override var width: Int = 0, override var height: Int = 0): ImageInfo {
    var fileSize: Int = 0
    var pixelDataOffset: Int = 0
        get() = if(field == 0) 14 + headerSize else field
    var headerSize: Int = 40

    var bitsPerPixel: Int = 8
    var compression: CompressionType = CompressionType.BI_RGB
    var pixelDataSize: Int = 0 //default 0 if stored in array[][]
        get() {
            if(field == 0)
                return fileSize - pixelDataOffset
            else
                return field
        }

    //resolution variables
    var xPixelsPerMeter: Int = 0
    var yPixelsPerMeter: Int = 0

    var colorsUsed: Int = 0 //size of colors table in units
    var colorsImportant: Int = 0

    //ver 4(108)
    var redMask: Int = 255
    var greenMask: Int = 255
    var blueMask: Int = 255
    var alphaMask: Int = 255
    var colorSpaceType: ColorSpaceType = ColorSpaceType.LCS_CALIBRATED_RGB
//    var endPoints:
    var gammaRed = 0
    var gammaGreen = 0
    var gammaBlue = 0


    enum class CompressionType {
        BI_RGB, BI_RLE8, BI_RLE4, BI_BITFIELDS,
        BI_JPEG, BI_PNG, BI_ALPHABITFIELDS;
        companion object {
            fun getByValue(value: Int): CompressionType {
                when (value) {
                    0 -> return BI_RGB
                    1 -> return BI_RLE4
                    2 -> return BI_RLE8
                    3 -> return BI_BITFIELDS
                    4 -> return BI_JPEG
                    5 -> return BI_PNG
                    6 -> return BI_ALPHABITFIELDS
                    else -> return BI_RGB
                }
            }
        }
    }

    enum class ColorSpaceType {
        LCS_CALIBRATED_RGB, LCS_sRGB, LCS_WINDOWS_COLOR_SPACE,
        PROFILE_LINKED, PROFILE_EMBEDDED;
        companion object {
            fun getByString(s: String): ColorSpaceType {
                when(s) {
                    "sRGB" -> return LCS_sRGB
                    "Win " -> return LCS_WINDOWS_COLOR_SPACE
                    "LINK" -> return PROFILE_LINKED
                    "MBED" -> return PROFILE_EMBEDDED
                    else -> return LCS_CALIBRATED_RGB
                }
            }

            fun getByValue(value: Int): ColorSpaceType {
                when(value) {
                    0x73524742 -> return LCS_sRGB
                    0x57696E20 -> return LCS_WINDOWS_COLOR_SPACE
                    0x4C494E4B -> return PROFILE_LINKED
                    0x4D424544 -> return PROFILE_EMBEDDED
                    else -> return LCS_CALIBRATED_RGB
                }
            }
        }
    }

    override fun toString(): String {
        return "BmpInfo: width - $width, height - $height, fileSize - $fileSize,\n" +
                " pixelDataOffset - $pixelDataOffset, headerSize - $headerSize, \n" +
                "bitsPerPixel - $bitsPerPixel, compression - $compression, pixelDataSize - $pixelDataSize,\n " +
                "..., colorsUsed - $colorsUsed, colorsImportant - $colorsImportant."
    }
}