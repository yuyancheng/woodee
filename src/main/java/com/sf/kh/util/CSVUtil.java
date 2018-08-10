package com.sf.kh.util;

import code.ponfee.commons.model.ResultCode;
import com.google.common.base.Charsets;
import com.sf.kh.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Auther: 01378178
 * @Date: 2018/7/5 10:54
 * @Refer https://www.cnblogs.com/mingforyou/p/4103132.html
 * @Description:
 */
public class CSVUtil {

    private CSVUtil(){}

    private static final byte[] commonCsvHead = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
    /**
     * CSV文件生成方法
     * @param head
     * @param dataList
     * @param outPutPath
     * @param filename
     * @return
     */
    public static File createCSVFile(List<Object> head, List<List<Object>> dataList,
                                     String outPutPath, String filename) {

        File csvFile = null;

        try{

            csvFile = new File(outPutPath + File.separator + filename + ".csv");
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if(!csvFile.createNewFile()){
                throw new RuntimeException("failed to create csvFile");
            }

        }catch(Exception e){
            throw new RuntimeException("createCSVFile error", e);
        }

        try(    OutputStream outStream = new FileOutputStream(csvFile);
                OutputStreamWriter ow =new OutputStreamWriter(outStream, "GB2312");
                BufferedWriter csvWriter = new BufferedWriter(ow, 1024)
        ) {

            // GB2312使正确读取分隔符","
            // 写入文件头部
            writeRow(head, csvWriter);

            // 写入文件内容
            for (List<Object> row : dataList) {
                writeRow(row, csvWriter);
            }

            csvWriter.flush();

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return csvFile;
    }

    /**
     * 写一行数据方法
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        // 写入文件头部
        for (Object data : row) {
            StringBuilder sb = new StringBuilder();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }

    public static String decorateStr(String str){
        return "\"" +  (StringUtils.isBlank(str) ? "" : (str.indexOf('\"') > -1 ? str.replaceAll("\"", "'"): str))+"\"";
    }



    public static void saveAndExport(String fullPath, HttpServletResponse response, StringBuilder sb, String exportFileName){

        File file = new File(fullPath);
        String content = new String(commonCsvHead)+sb.toString();

        try(
                OutputStream os = new FileOutputStream(file);
                OutputStreamWriter ow = new OutputStreamWriter(os,  Charsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(ow)
        ){

            bw.write(content);
            bw.flush();

        }catch(Exception e){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "导出异常");
        }

        try
        {
            byte[] arr = content.getBytes(Charsets.UTF_8);

            response.setContentType("application/csv");
            response.setHeader("Content-Disposition","attachment; filename=" + URLEncoder.encode(exportFileName, "UTF-8"));
            response.setHeader("Content-Length", arr.length+"");
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write( arr);

        }catch(Exception e){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "导出异常");
        }
    }
}
