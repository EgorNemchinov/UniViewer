package image

import java.awt.Color

/**
 * Created by Egor Nemchinov on 05.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
data class Pixel(var r: Int = 0, var g: Int = 0, var b: Int = 0, var a: Float = 1f) {
    constructor(color: Color) : this(color.red, color.green, color.blue, color.alpha / 255f)

    val color: Color
        get() = Color(r, g, b)
}
