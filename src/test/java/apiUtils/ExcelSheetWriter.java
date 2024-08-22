package apiUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelSheetWriter {
    private final XSSFWorkbook workbook;
	private final XSSFSheet sheet;
	private final int rowNumber;
	private final FileOutputStream fos;
	private static final Object lock = new Object();

	public ExcelSheetWriter(String workbookName, String sheetName, int rowNumber) throws IOException {
		Objects.requireNonNull(workbookName, "Workbook name cannot be null.");
		Objects.requireNonNull(sheetName, "Sheet name cannot be null.");
		if (rowNumber < 0) {
			throw new IllegalArgumentException("Row number cannot be negative.");
		}

		this.rowNumber = rowNumber;
        String filePath = TestConstants.DATA_FILE_PATH + workbookName + ".xlsx";
		FileInputStream file = new FileInputStream(filePath);
		workbook = new XSSFWorkbook(file);
		sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			throw new IllegalArgumentException("Sheet " + sheetName + " does not exist in the workbook.");
		}
		fos = new FileOutputStream(filePath);
	}

	public void writeCell(String columnName, String value) throws IOException {
		Objects.requireNonNull(columnName, "Column name cannot be null.");
		Objects.requireNonNull(value, "Value cannot be null.");
		if (columnName.isBlank()) {
			throw new IllegalArgumentException("Column name cannot be empty or blank.");
		}

		XSSFRow row = sheet.getRow(0);
		int columnCount = row.getLastCellNum();

		int columnIndex = -1;
		for (int i = 0; i < columnCount; i++) {
			String cellValue = row.getCell(i).getStringCellValue();
			if (cellValue.equalsIgnoreCase(columnName)) {
				columnIndex = i;
				break;
			}
		}

		if (columnIndex == -1) {
			throw new IllegalArgumentException("Column " + columnName + " does not exist in the sheet.");
		}

		XSSFRow dataRow = sheet.getRow(rowNumber);
		if (dataRow == null) {
			dataRow = sheet.createRow(rowNumber);
		}

		XSSFCell cell = dataRow.getCell(columnIndex);
		if (cell == null) {
			cell = dataRow.createCell(columnIndex);
		}

		cell.setCellValue(value);
		saveFile();
	}

	public void deleteRecords(String columnName) throws IOException {
		Objects.requireNonNull(columnName, "Column name cannot be null.");
		if (columnName.isBlank()) {
			throw new IllegalArgumentException("Column name cannot be empty or blank.");
		}

		XSSFRow headerRow = sheet.getRow(0);
		int columnCount = headerRow.getLastCellNum();
		int columnIndex = -1;
		for (int i = 0; i < columnCount; i++) {
			String cellValue = headerRow.getCell(i).getStringCellValue();
			if (cellValue.equalsIgnoreCase(columnName)) {
				columnIndex = i;
				break;
			}
		}
		if (columnIndex == -1) {
			throw new IllegalArgumentException("Column " + columnName + " does not exist in the sheet.");
		}

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			if (row != null) {
				XSSFCell cell = row.getCell(columnIndex);
				if (cell != null) {
					row.removeCell(cell);
				}
			}
		}
		saveFile();
	}

	public void saveFile() throws IOException {
		synchronized (lock) {
			workbook.write(fos);
		}
	}

	public void closeFile() {
		try {
			if (fos != null) {
				fos.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to close file output stream.", e);
		}
		try {
			if (workbook != null) {
				workbook.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to close workbook.", e);
		}
	}
}
