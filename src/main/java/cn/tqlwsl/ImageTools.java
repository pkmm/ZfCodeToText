package cn.tqlwsl;
import libsvm.svm_node;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageTools {
    private final static int subImageCount = 4;
    private final static int n = 16;
    private final static int m = 21;
    private final static int n_m = n * m;

    /**
     * 把图片转换为向量
     * 大图转化为4个小图片，每个图片是16 * 21的大小
     *
     * @param image
     * @return
     */
    public static svm_node[][] convertImageToVector(BufferedImage image) {

        svm_node[][] vector = new svm_node[subImageCount][n_m];
        List<BufferedImage> images = splitImage(image);
        for (int k = 0; k < subImageCount; k++) {
            BufferedImage currentSubImage = images.get(k);
            int index = 0;
            for (int i = 0; i < currentSubImage.getWidth(); i++) {
                for (int j = 0; j < currentSubImage.getHeight(); j++) {
                    svm_node node = new svm_node();
                    node.index = index + 1;
                    node.value = rgbToDouble(currentSubImage.getRGB(i, j));
                    vector[k][index++] = node;
                }
            }
        }
        return vector;
    }

    private static double rgbToDouble(int rgb) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        return (0.3 * r + 0.59 * g + 0.11 * b) / 255.0;
    }


    private static List<BufferedImage> splitImage(BufferedImage image) {
        List<BufferedImage> images = new ArrayList<BufferedImage>();
        for (int i = 2; i < 50; i += 12) {
            images.add(image.getSubimage(i, 1, n, m));
        }

        return images;
    }
}
