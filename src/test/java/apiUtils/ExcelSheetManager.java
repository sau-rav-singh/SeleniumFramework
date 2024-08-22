package apiUtils;

public class ExcelSheetManager {

    private static final ThreadLocal<ExcelSheetReader> excelSheetReader = new ThreadLocal<>();
    private static final ThreadLocal<ExcelSheetWriter> excelSheetWriter = new ThreadLocal<>();

    public static void setExcelSheetReader(ExcelSheetReader reader) {
        excelSheetReader.set(reader);
    }

    public static ExcelSheetReader getExcelSheetReader() {
        return excelSheetReader.get();
    }

    public static void setExcelSheetWriter(ExcelSheetWriter writer) {
        excelSheetWriter.set(writer);
    }

    public static ExcelSheetWriter getExcelSheetWriter() {
        return excelSheetWriter.get();
    }

}
