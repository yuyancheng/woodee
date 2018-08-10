package com.sf.kh.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Auther: 01378178
 * @Date: 2018/7/2 10:31
 * @Description:
 */
public class ExcelUtil {

    /**
     * 实例化导出类
     *
     * @param title    导出表格的表名，最好是英文，中文可能出现乱码
     * @param rowName  导出表格的列名数组
     * @param dataList 对象数组的List集合
     * @param response
     */
    private  ExcelUtil() {
    }

    // 导出数据
    public static void exportData(String title, String[] rowName, List<String[]> dataList, HttpServletRequest request, HttpServletResponse response){

        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建一个excel对象
        HSSFSheet sheet = workbook.createSheet(title); // 创建表格


        int columnNum = rowName.length;                                             // 表格列的长度
        HSSFRow rowRowName = sheet.createRow(0);                           // 在第二行创建行
        HSSFCellStyle cells = workbook.createCellStyle();
        cells.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        rowRowName.setRowStyle(cells);

        // 循环 将列名放进去
        for (int i = 0; i < columnNum; i++) {
            HSSFCell cellRowName = rowRowName.createCell(i);
            cellRowName.setCellType(CellType.STRING);                       // 单元格类型
            HSSFRichTextString text = new HSSFRichTextString(rowName[i]);   // 得到列的值
            cellRowName.setCellValue(text);                                  // 设置列的值
            cellRowName.setCellStyle(getHeaderStyle(workbook, 10));  // 样式
        }

        // 将查询到的数据设置到对应的单元格中
        for (int i = 0; i < dataList.size(); i++) {
            Object[] obj = dataList.get(i);//遍历每个对象
            HSSFRow row = sheet.createRow(i + 1);//创建所需的行数
            for (int j = 0; j < obj.length; j++) {
                HSSFCell cell = row.createCell(j, CellType.STRING);
                if (!"".equals(obj[j]) && obj[j] != null) {
                    cell.setCellValue(obj[j].toString());  //设置单元格的值
                } else {
                    cell.setCellValue("");
                }
//                cell.setCellStyle(columnStyle); // 样式
            }
        }


        if (workbook != null) {
            try( OutputStream out = response.getOutputStream()){
                // excel 表文件名
                String fileName = title+ ".xls";
                String fileName11 = "";
                String userAgent = request.getHeader("USER-AGENT");
                if (StringUtils.contains(userAgent, "Firefox") || StringUtils.contains(userAgent, "firefox")) {//火狐浏览器
                    fileName11 = new String(fileName.getBytes(), "ISO8859-1");
                } else {
                    fileName11 = URLEncoder.encode(fileName, "UTF-8");//其他浏览器
                }
                String headStr = "attachment; filename=\"" + fileName11 + "\"";
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", headStr);
                workbook.write(out);
                out.flush();
                workbook.close();
            } catch (Exception e) {
                throw new RuntimeException("export data error",  e);
            }
        }
    }

    public static  HSSFCellStyle getColumnStyle(HSSFWorkbook workbook, int fontSize) {
        return defaultStype(workbook, fontSize);
    }

    public static HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook, int fontSize) {
        HSSFFont font = defaultFont(workbook, fontSize);
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public static HSSFCellStyle defaultStype(HSSFWorkbook workbook, int fontSize){
        HSSFFont font = defaultFont(workbook, fontSize);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public static HSSFFont defaultFont(HSSFWorkbook workbook, int fontSize){
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) fontSize);
//        font.setFontName("宋体");
        return font;
    }

}
