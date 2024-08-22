package listeners;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class MySuiteListener implements ISuiteListener {
	private static final String REPORTS_FOLDER = "reports/";
    private static final String BACKUP_REPORTS_FOLDER = "backupReports/";
    private static final String ZIP_FILE_EXTENSION = ".zip";
    private static final int NUM_REPORTS_TO_KEEP = 1;
    private static final String REPORT_FILE_PATTERN = "^.*\\.html$";
    private static final String ZIP_FILE_PREFIX = "my_report";

    
    @Override
    public void onStart(ISuite suite) {
        // Do nothing on suite start
    }

    @Override
    public void onFinish(ISuite suite) {
        List<File> reportFiles = getReportFiles(REPORTS_FOLDER, REPORT_FILE_PATTERN);
        File latestReportFile = getLatestReportFile(reportFiles);

        if (latestReportFile != null) {
        	String zipFileName = getZipFileName(REPORTS_FOLDER, REPORT_FILE_PATTERN);

            File backupFolder = new File(BACKUP_REPORTS_FOLDER);

            try {
                // Create the backupReports folder if it doesn't exist
                backupFolder.mkdirs();

                // Create the zip file in the backupReports folder
                File zipFile = new File(backupFolder, zipFileName);
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));

                // Add all the report files except the latest report file to the zip file
                for (File reportFile : reportFiles) {
                    if (!reportFile.equals(latestReportFile)) {
                        addFileToZip(reportFile, zipOutputStream);
                    }
                }

                zipOutputStream.close();

                // Delete all the report files except the latest report file
                for (File reportFile : reportFiles) {
                    if (!reportFile.equals(latestReportFile)) {
                        reportFile.delete();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Returns a list of report files that match the given folder and file pattern.
     */
    private List<File> getReportFiles(String folder, String filePattern) {
        List<File> reportFiles = new ArrayList<>();

        File dir = new File(folder);
        if (!dir.exists() || !dir.isDirectory()) {
            return reportFiles;
        }

        Pattern pattern = Pattern.compile(filePattern);
        File[] files = dir.listFiles();
        if (files == null) {
            return reportFiles;
        }

        for (File file : files) {
            String fileName = file.getName();
            if (pattern.matcher(fileName).matches()) {
                reportFiles.add(file);
            }
        }

        // Sort the report files by last modified time, in descending order
        reportFiles.sort(Comparator.comparing(File::lastModified).reversed());

        return reportFiles;
    }

    /**
     * Returns the latest report file in the given list of files, or null if the list is empty or contains only one file.
     */
    private File getLatestReportFile(List<File> reportFiles) {
        if (reportFiles.size() <= NUM_REPORTS_TO_KEEP) {
            // There's no need to skip any report file
            return null;
        }

        // Sort the report files in ascending order of last modified time
        Collections.sort(reportFiles, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.compare(f1.lastModified(), f2.lastModified());
            }
        });

        // Return the oldest report file that should be skipped
        return reportFiles.get(NUM_REPORTS_TO_KEEP);
    }

    /**
     * Returns the name of the zip file to create.
     */
    private String getZipFileName(String folder, String filePattern) {
        // Get the list of report files, sorted by last modified time, in descending order
        List<File> reportFiles = getReportFiles(folder, filePattern);
        if (reportFiles.isEmpty()) {
            return null;
        }

        // Use the timestamp of the second latest report file, if available
        String timestamp;
        if (reportFiles.size() > 1) {
            timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date(reportFiles.get(1).lastModified()));
        } else {
            timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        }

        return ZIP_FILE_PREFIX + timestamp + ZIP_FILE_EXTENSION;
    }




    /**
     * Adds the given file to the given zip output stream.
     */
    private void addFileToZip(File file, ZipOutputStream zipOutputStream) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        ZipEntry zipEntry = new ZipEntry(file.getName());
        zipOutputStream.putNextEntry(zipEntry);

        int bytesRead;
        while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
            zipOutputStream.write(buffer, 0, bytesRead);
        }

        bufferedInputStream.close();
        fileInputStream.close();
        zipOutputStream.closeEntry();
    }

}
