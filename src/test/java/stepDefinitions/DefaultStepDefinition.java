package stepDefinitions;

import apiUtils.ExcelSheetManager;
import apiUtils.ExcelSheetReader;
import io.cucumber.java.en.Given;

public class DefaultStepDefinition {
    private String workbookName;
    private String sheetName;

    @Given("A Workbook named {string} and sheetname as{string} is read")
    public void a_workbook_with_name_and_sub_sheet_name_is_read(String workbookName, String sheetName)
            throws Exception {
        this.workbookName = workbookName;
        this.sheetName = sheetName;
        ExcelSheetReader reader = new ExcelSheetReader(workbookName, sheetName, 0);
        ExcelSheetManager.setExcelSheetReader(reader);
    }

    @Given("Row number as{int} is read")
    public void row_number_is_read(int rowNumber) throws Exception {
        ExcelSheetReader reader = new ExcelSheetReader(workbookName, sheetName, rowNumber);
        ExcelSheetManager.setExcelSheetReader(reader);
    }
}
