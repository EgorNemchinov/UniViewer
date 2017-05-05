package image

/**
 * Created by Egor Nemchinov on 05.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class Image(val imageInfo: ImageInfo, var pixels: Array<Array<Pixel>>) {
    fun pixelAt(x: Int, y: Int): Pixel {
        return pixels[x][y]
    }
}