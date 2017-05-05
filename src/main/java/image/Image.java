package image;

/**
 * Created by Egor Nemchinov on 03.05.17.
 *
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
public class Image {
    private Pixel[][] pixels;
    private ImageInfo imageInfo;

    public Image(ImageInfo imageInfo, Pixel[][] pixels) {
        this(imageInfo);
        this.pixels = pixels; //mb reverse since it's coded upside-down?
    }

    public Image(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
        pixels = new Pixel[imageInfo.getWidth()][imageInfo.getHeight()];
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                pixels[i][j] = new Pixel(0,0, 30, 1f);
            }
        }
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public Pixel pixelAt(int x, int y) {
        return pixels[x][y];
    }
}
