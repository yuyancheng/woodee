package com.sf.kh.util;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @Auther: 01378178
 * @Date: 2018/7/3 19:59
 * @Description:
 */
public class ImageUtil {

    private ImageUtil(){}

    public static void base64ToImg(String base64Code, String path, String name){

        if(StringUtils.isBlank(base64Code)){
            return;
        }

        BASE64Decoder decoder = new BASE64Decoder();

        try{
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }
        }catch(Exception e){
            throw new RuntimeException("base64ToImg, create dir error", e);
        }

        try(
                FileOutputStream out =  new FileOutputStream(new File(path + File.separator + name));
                BufferedOutputStream bos = new BufferedOutputStream(out)
        ){

            byte[] b = decoder.decodeBuffer(base64Code);
            bos.write(b);
            bos.flush();

        }catch(Exception e){
            throw new RuntimeException("base64ToImg error", e);
        }
    }


    public static String imgToBase64(String path){
        if(StringUtils.isBlank(path)){
            return path;
        }

        BASE64Encoder encoder = new BASE64Encoder();
        byte[] data = null;
        try(FileInputStream in = new FileInputStream(path); BufferedInputStream bs = new BufferedInputStream(in)) {
            data = new byte[in.available()];
            bs.read(data);
        }catch(Exception e){
            throw new RuntimeException("imgToBase64 error",e);
        }

        return data==null ?  null : encoder.encode(data);
    }
}
