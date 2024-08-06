package apiUtils;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelSheetReader {
    private final Sheet sheet;
    private final Row row;
    private static final String FILE_EXTENSION = ".xlsx";

    public ExcelSheetReader(String workbookName, String sheetName, int rowNumber) {
        try (FileInputStream fileInputStream = new FileInputStream(
                TestConstants.DATA_FILE_PATH + workbookName + FILE_EXTENSION)) {
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            sheet = workbook.getSheet(sheetName);
            row = sheet.getRow(rowNumber);
            if (row == null) {
                throw new IllegalArgumentException("Row number " + rowNumber + " does not exist in the sheet.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel workbook.", e);
        }
    }

    public String readCell(int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return "";
        }
        return cell.toString();
    }

    private int getCellIndex(String columnName) {
        Row firstRow = sheet.getRow(0);
        if (firstRow == null) {
            throw new IllegalStateException("The first row of the sheet is empty.");
        }
        for (Cell cell : firstRow) {
            if (cell == null) {
                continue;
            }
            String value = cell.toString();
            if (value.equals(columnName)) {
                return cell.getColumnIndex();
            }
        }
        throw new IllegalArgumentException("Column " + columnName + " does not exist in the sheet.");
    }

    public String readCell(String columnName) {
        int columnIndex = getCellIndex(columnName);
        return readCell(columnIndex);
    }
}
