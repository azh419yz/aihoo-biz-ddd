package com.aihoo.domain.sys.excel;

import com.aihoo.exception.BizException;
import com.aihoo.util.CalculateUtil;
import com.aihoo.util.CharUtil;
import com.aihoo.util.DateUtil;
import com.aihoo.util.StringHandler;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Excel 工具类（基于 Apache POI）。
 *
 * <p>从旧 admin 的 com.aihoo.admin.common.excel.ExcelUtils 迁入。
 * 包路径和内部 import 已统一到 shared-kernel（com.aihoo.*）。</p>
 */
public class ExcelUtils {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";

    public static <T> List<T> readExcel(String path, Class<T> cls, MultipartFile file) {

        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new BizException("未上传文件");
        }
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确！当前文件名是:{}", fileName);
            throw new BizException("上传文件格式不正确！当前文件名是" + fileName);
        }
        List<T> dataList = new ArrayList<>();
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2007)) {
                workbook = new XSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2003)) {
                workbook = new HSSFWorkbook(is);
            }
            if (workbook != null) {
                Map<String, List<Field>> classMap = new HashMap<>();
                List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                fields.forEach(
                        field -> {
                            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                            if (annotation != null) {
                                String value = annotation.value();
                                if (StringUtils.isBlank(value)) {
                                    return;
                                }
                                if (!classMap.containsKey(value)) {
                                    classMap.put(value, new ArrayList<>());
                                }
                                field.setAccessible(true);
                                classMap.get(value).add(field);
                            }
                        }
                );
                Map<Integer, List<Field>> reflectionMap = new HashMap<>(16);
                Sheet sheet = workbook.getSheetAt(0);

                boolean firstRow = true;
                for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (firstRow) {
                        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            if (classMap.containsKey(cellValue)) {
                                reflectionMap.put(j, classMap.get(cellValue));
                            }
                        }
                        firstRow = false;
                    } else {
                        if (row == null) {
                            continue;
                        }
                        try {
                            T t = cls.newInstance();
                            boolean allBlank = true;
                            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                                if (reflectionMap.containsKey(j)) {
                                    Cell cell = row.getCell(j);
                                    String cellValue = getCellValue(cell);
                                    if (StringUtils.isNotBlank(cellValue)) {
                                        allBlank = false;
                                    }
                                    List<Field> fieldList = reflectionMap.get(j);
                                    fieldList.forEach(
                                            x -> {
                                                try {
                                                    handleField(t, cellValue, x);
                                                } catch (Exception e) {
                                                    log.error("reflect field:{} value:{} exception!", x.getName(), cellValue, e);
                                                }
                                            }
                                    );
                                }
                            }
                            if (!allBlank) {
                                dataList.add(t);
                            } else {
                                log.warn("row:{} is blank ignore!", i);
                            }
                        } catch (Exception e) {
                            log.error("parse row:{} exception!", i, e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("parse excel exception!", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error("parse excel exception!", e);
                }
            }
        }
        return dataList;
    }

    /**
     * 读取excel
     */
    public static <T> List<T> getExcelRead(String fileName, InputStream is, boolean isTitle, Map<Integer, String> param, Class<T> cls) throws Exception {
        try {
            Workbook workbook = getWorkbook(fileName, is);
            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;
            List<T> list = new ArrayList<T>();
            for (Row row : sheet) {
                if (count == 0 && isTitle) {
                    count++;
                    continue;
                }
                Map<String, String> obj_val = new HashMap<String, String>();

                for (Integer index : param.keySet()) {
                    Cell cell = row.getCell(index);
                    String value = cell == null ? "" : getValue(cell);
                    if (StringHandler.isEmpty(value) && obj_val.keySet().size() == 0) {
                        continue;
                    } else {
                        obj_val.put(param.get(index), value.trim());
                    }
                }
                if (obj_val.keySet().size() > 0) {
                    String json = JSON.toJSONString(obj_val);
                    T t = JSON.parseObject(json, cls);
                    list.add(t);
                    obj_val.clear();
                }
            }
            return list;
        } catch (Exception e) {
            throw e;
        }
    }


    public static Workbook getWorkbook(String fileName, InputStream is) throws Exception {
        Workbook workbook = null;
        try {
            boolean isExcel2003 = true;
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }

            if (isExcel2003) {
                workbook = new XSSFWorkbook(is);
            } else {
                workbook = new HSSFWorkbook(is);
            }
        } catch (Exception e) {
            throw e;
        }
        return workbook;
    }


    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }


    public static String getValue(Cell cell) {
        String cellValue = null;
        if (cell.getCellType() == CellType.BOOLEAN) {
            cellValue = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue());
                cellValue = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_6).format(date);
            } else {
                double value = cell.getNumericCellValue();
                cellValue = CalculateUtil.add(value, 0d, CalculateUtil.two, CalculateUtil.ROUND_HALF_UP).toString();
                if (cellValue.indexOf(".00") > -1) {
                    Long s = CalculateUtil.add(cellValue, "0", CalculateUtil.two, CalculateUtil.ROUND_HALF_UP).longValue();
                    cellValue = s.toString();
                }
            }
        } else if (cell.getCellType() == CellType.STRING) {
            cellValue = String.valueOf(cell.getStringCellValue());
        } else {
            cellValue = String.valueOf(cell.getStringCellValue());
        }
        return CharUtil.trimString(cellValue);
    }

    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == null || type == void.class || StringUtils.isBlank(value)) {
            return;
        }
        if (type == String.class) {
            field.set(t, value);
        } else if (type == Object.class) {
            field.set(t, value);
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            field.set(t, value);
        } else if (type == LocalDateTime.class) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dt = LocalDateTime.parse(value, df);
            field.set(t, dt);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                return org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else {
                return BigDecimal.valueOf(cell.getNumericCellValue()).toString();
            }
        } else if (cell.getCellType() == CellType.FORMULA) {
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }

    }

    public static <T> void writeExcel(HttpServletRequest request, HttpServletResponse response, List<T> dataList, Class<T> cls, String fileName) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                })).collect(Collectors.toList());

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);

                Font font = wb.createFont();
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
            });
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            dataList.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    if (value != null) {
                        if (type == Date.class) {
                            cell.setCellValue(value.toString());
                        } else if (type == LocalDateTime.class) {
                            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            cell.setCellValue(df.format((LocalDateTime) value));
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                });
            });
        }
        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        buildExcelDocument(fileName, wb, response);
    }

    /**
     * 浏览器下载excel
     */
    private static void buildExcelDocument(String fileName, Workbook wb, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.flushBuffer();
            ServletOutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成excel文件
     */
    private static void buildExcelFile(String path, Workbook wb) {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            wb.write(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void Excel(HttpServletRequest request, HttpServletResponse response, List<T> dataList, Class<T> cls, String fileName) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                })).collect(Collectors.toList());

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            for (Field field : fieldList) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                Cell cell = row.createCell(aj.getAndIncrement());
                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                Font font = wb.createFont();
                font.setColor(IndexedColors.BLACK.index);
                font.setBold(true);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);

            }
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            CellStyle cellStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setColor(IndexedColors.GREY_80_PERCENT.index);
            cellStyle.setFont(font);
            dataList.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    if (value != null) {
                        if (type == Date.class) {
                            cell.setCellValue(value.toString());
                        } else if (type == LocalDateTime.class) {
                            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            cell.setCellValue(df.format((LocalDateTime) value));
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                });
            });
        }
        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        buildExcelDocument(fileName, wb, response);
    }

}
