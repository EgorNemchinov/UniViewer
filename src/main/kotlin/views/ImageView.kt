package views

import image.Image
import java.awt.Graphics
import javax.swing.JFrame
import javax.swing.JPanel

/**
 * Created by Egor Nemchinov on 03.05.17.
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
class ImageView: View {
    override fun drawImage(image: Image) {
        val frame = JFrame()
        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        frame.setSize(image.imageInfo.width, image.imageInfo.height)
        frame.add(ImagePanel(image))
        frame.isVisible = true
    }
}

class ImagePanel(var image: Image, isDoubleBuffered: Boolean = true) : JPanel(isDoubleBuffered) {
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        for (i in 0..image.imageInfo.height-1){
            for (j in 0..image.imageInfo.width-1){
                g.color = image.pixelAt(j, i).color
                g.drawRect(j, i, 1, 1)
            }
        }
    }
}