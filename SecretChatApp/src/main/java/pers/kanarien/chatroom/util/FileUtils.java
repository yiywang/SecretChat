package pers.kanarien.chatroom.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * <p>
 * Description: Tools related to file information and operations
 * 1. Judge the file type by the file header
 * 2. Get the formatted file size
 * 3. Efficiently convert files into byte arrays
 * </p>
 * @author Kanarien
 * @version 1.0
 * @date November 15, 2017 9:05:10 PM
 */
public class FileUtils {

    private static final HashMap<String,String> fileTypes = new HashMap<String,String>(); 
    static { // BOM（Byte Order Mark）文件头字节
        fileTypes.put("494433", "mp3");
        fileTypes.put("524946", "wav");
        fileTypes.put("ffd8ff", "jpg");
        fileTypes.put("FFD8FF", "jpg");
        fileTypes.put("89504E", "png");
        fileTypes.put("89504e", "png");
        fileTypes.put("474946", "gif");
    }
    private static final String B_UNIT = "B";
    private static final String KB_UNIT = "KB";
    private static final String MB_UNIT = "MB";
    private static final String GB_UNIT = "GB";
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.0");



    /**
     * <p>
     * Description: through the file header containing BOM (Byte Order Mark)
     * The first 3 bytes determine the file type
     * </p>
     * @param filePath file path
     * @return
     */
    public static String getFileType(String filePath) {
        return fileTypes.get(getFileHeader3(filePath));
    }

    /**
     * <p>
     * Description: Get the first 3 bytes of the file header
     * </p>
     * @param filePath file path
     * @return
     */
    private static String getFileHeader3(String filePath) {
        
      File file=new File(filePath);
        if(!file.exists() || file.length()<4){
            return "null";
        }
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(file);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return value;
    }
    
    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * <p>
     * Description: Get the formatted file size
     * The format is with a unit and one decimal place
     * </p>
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        String fileSizeString = "";
        if (size < 1024) {
            fileSizeString = decimalFormat.format(size) + B_UNIT;
        } else if (size < 1048576) {
            fileSizeString = decimalFormat.format(size / 1024) + KB_UNIT;
        } else if (size < 1073741824) {
            fileSizeString = decimalFormat.format(size / 1048576) + MB_UNIT;
        } else {
            fileSizeString = decimalFormat.format(size / 1073741824) + GB_UNIT;
        }
        return fileSizeString;
    }
    
    public static String getFormatSize(long size) {
        return getFormatSize((double)size);
    }

    /**
     * <p>
     * Description: Convert files into byte arrays efficiently
     * </p>
     * @param filename
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static byte[] toByteArray(String filePath) throws IOException {  
        
        FileChannel fc = null;  
        try {  
            fc = new RandomAccessFile(filePath, "r").getChannel();  
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,  
                    fc.size()).load();  
            System.out.println(byteBuffer.isLoaded());  
            byte[] result = new byte[(int) fc.size()];  
            if (byteBuffer.remaining() > 0) {  
                byteBuffer.get(result, 0, byteBuffer.remaining());  
            }  
            return result;  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        } finally {  
            try {  
                fc.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    public static void main(String args[]){

    }


}
