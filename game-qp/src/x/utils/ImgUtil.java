package x.utils;

import jsa.id.JsaIdGenerator;
import jsa.orm.IDataSource;
import jsa.orm.JsaOrmFactory;
import org.springframework.core.io.ClassPathResource;
import x.vo.UserVo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ImgUtil {
    public static byte[] image2Bytes(String imgSrc) throws Exception {
        FileInputStream fin = new FileInputStream(new File(imgSrc));
        //可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]
        byte[] bytes = new byte[fin.available()];
        //将文件内容写入字节数组，提供测试的case
        fin.read(bytes);
        fin.close();
        return bytes;
    }

    public static FileInputStream getImageByte(String infile) throws FileNotFoundException {
        FileInputStream imageByte = null;
        File file = new File(infile);
        imageByte = new FileInputStream(file);
        return imageByte;
    }

    public static void main(String[] args) throws IOException {

    /*    JsaOrmFactory.initDefaultExecutorAdapter();
        IDataSource ds = JsaOrmFactory.getDataSource();
        List<UserVo> userVos = ds.getBySql(UserVo.class, "SELECT * FROM UserVo", null);
        for (UserVo vo : userVos) {
            ds.deleteById(vo);
        }
        UserVo userVo = new UserVo();
        try {
            byte[] b1 = image2Bytes("E:\\123.png"); //path是绝对路径
            byte[] b2 = Base64.getEncoder().encode(b1);
            userVo.setIdentityCardIMGf(b2);
            ds.save(userVo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (UserVo vo : userVos) {
            byte[] b3 = Base64.getDecoder().decode(vo.getIdentityCardIMGf());
            System.out.println("解码后的字节数组 = " + Arrays.toString(b3));
            try {
                buff2Image(b3, "E:\\test" + vo.getId() + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println();*/

      /*  try {
            InputStream in = getImageByte("E:\\123.png");
            System.out.println("___" + in.available());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String path = "";
        try {
            byte[] b1 = image2Bytes("E:\\123.png"); //path是绝对路径
            System.out.println(new String(b1));
            System.out.println("*图片的字节数组 = " + Arrays.toString(b1));
            byte[] b2 = Base64.getEncoder().encode(b1);


            System.out.println(Base64.getEncoder().encodeToString(b1));
            byte[] textByte = Base64.getEncoder().encodeToString(b1).getBytes("UTF-8");
            System.out.println("AAA = " + Arrays.toString(textByte));
            System.out.println("编码后的字节数组 = " + Arrays.toString(b2));
            System.out.println("编码后的字节数组对应的ascil码值 = " + new String(b2));

            byte[] b3 = Base64.getDecoder().decode(b2);
            System.out.println("解码后的字节数组 = " + Arrays.toString(b3));
            buff2Image(b3, "E:\\test.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        String url = "http://212.64.84.143:8080/gmtool/";

        BufferedImage small = null;
        try {
            small = QRCodeUtil.encode2(URLDecoder.decode(url), null, false);
            String filePath = "E:\\test.jpg";

            BufferedImage finalImage = overlapImage(filePath, small);
            byte[] fimalByte = imageToBytes(finalImage, "jpg");
            buff2Image(fimalByte, "E:\\test11111.jpg");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 转换BufferedImage 数据为byte数组
     *
     * @param format image格式字符串.如"gif","png"
     * @return byte数组
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static BufferedImage overlapImage(String filePath, BufferedImage small) {
        try {
            BufferedImage big = ImageIO.read(new File(filePath));
            Graphics2D g = big.createGraphics();
            int x =150;
            int y = 315;
            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
            g.dispose();
            return big;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void buff2Image(byte[] b, String tagSrc) throws Exception {
        FileOutputStream fout = new FileOutputStream(tagSrc);
        //将字节写入文件
        fout.write(b);
        fout.close();
    }
}
