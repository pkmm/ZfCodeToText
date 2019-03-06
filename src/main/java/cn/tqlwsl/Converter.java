package cn.tqlwsl;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Converter {

    private final static String modelName = "model.txt";
    private svm_model model;

    public Converter() throws IOException {
        Reader reader = new StringReader(loadModel(modelName));
        this.model = svm.svm_load_model(new BufferedReader(reader));
    }

    private String loadModel(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResourceAsStream(fileName);
        if (null == in) {
            throw new IOException("can not read file by " + fileName);
        }

        return IOUtils.toString(in, "UTF-8");
    }

    public String convert(BufferedImage image) {
        StringBuffer sb = new StringBuffer();
        svm_node[][] nodes = ImageTools.convertImageToVector(image);

        for (int i = 0; i < 4; i++) {
            double val = svm.svm_predict(model, nodes[i]);
            sb.append((char) (int) val);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        Converter converter = new Converter();
        BufferedImage image = ImageIO.read(new URL("http://zfxk.zjtcm.net/CheckCode.aspx"));
        String ans = converter.convert(image);
        ImageIO.write(image, "png", new File("D:/temp/" + ans + ".png"));
        System.out.println(ans);
    }
}
