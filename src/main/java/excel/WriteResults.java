package excel;

import db.ReadDB;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WriteResults {

    FileOutputStream file;
    XSSFWorkbook workbook;
    XSSFSheet sheetCompany;
    XSSFSheet sheetCountry;
    XSSFSheet sheetRegion;

    List<String[]> consumptionCompany;
    List<String[]> consumptionCountry;
    List<String[]> consumptionRegion;
    String[] headers;

    public WriteResults(ReadDB readDB) {
        try {
            file = new FileOutputStream("ConsumptionResults.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.consumptionCompany = readDB.getDataCompany();
        this.consumptionCountry = readDB.getDataCountry();
        this.consumptionRegion = readDB.getDataRegion();
        workbook = new XSSFWorkbook();
        sheetCompany = workbook.createSheet("ConsumptionCompany");
        sheetCountry = workbook.createSheet("ConsumptionCountry");
        sheetRegion = workbook.createSheet("ConsumptionRegion");
        writeToExcel();
    }

    public void writeToExcel() {
        writeSheet(sheetCompany, consumptionCompany);
        writeSheet(sheetCountry, consumptionCountry);
        writeSheet(sheetRegion, consumptionRegion);

        autoSizeColumns(sheetCompany, 3);
        autoSizeColumns(sheetCountry, 3);
        autoSizeColumns(sheetRegion, 3);
        try {
            workbook.write(file);
            file.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSheet(XSSFSheet sheet, List<String[]> data) {
        switch (sheet.getSheetName()) {
            case "ConsumptionCompany":
                headers = new String[]{"Company", "Consumption", "Year"};
                break;
            case "ConsumptionCountry":
                headers = new String[]{"Country", "Consumption", "Year"};
                break;
            case "ConsumptionRegion":
                headers = new String[]{"Region", "Consumption", "Year"};
                break;
            default:
                throw new IllegalArgumentException("Неизвестное имя файла: " + sheet.getSheetName());
        }

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.length; i++) {
                row.createCell(i).setCellValue(rowData[i]);
            }
        }
    }

    private void autoSizeColumns(XSSFSheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

}