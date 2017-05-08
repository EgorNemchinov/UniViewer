package image

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by Egor Nemchinov on 05.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class Image(val imageInfo: ImageInfo = BmpInfo(), var pixels: Array<Array<Color>>) {
    fun colorAt(x: Int, y: Int): Color {
        return pixels[x][y]
    }

    constructor(file: File) : this(pixels = Array(0){Array<Color>(0){Color.BLACK}}) {
        var image: BufferedImage? = ImageIO.read(file) ?: return
        pixels = Array(image!!.width) {Array<Color>(image.height) {Color.YELLOW}}
        for (i in 0..image.width - 1)
            for (j in 0..image.height - 1)
                pixels[i][j] = Color(image.getRGB(i, j))
    }

    override fun equals(other: Any?): Boolean {
        if(other == null)
            return false
        if(other.javaClass != this.javaClass)
            return false

        other as Image

        if(other.pixels.size != this.pixels.size ||
                other.pixels[0].size != this.pixels[0].size)
            return false
        for(x in 0..pixels.size-1) {
            for(y in 0..pixels[0].size-1) {
                if(this.colorAt(x, y) != other.colorAt(x, y))
                    return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = imageInfo?.hashCode() ?: 0
        result = 31 * result + Arrays.hashCode(pixels)
        return result
    }
}