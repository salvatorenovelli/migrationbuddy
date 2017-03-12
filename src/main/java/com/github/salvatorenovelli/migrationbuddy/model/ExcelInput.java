package com.github.salvatorenovelli.migrationbuddy.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class ExcelInput {


    private static final boolean NON_PARALLEL = false;
    private final Workbook wb;
    private final Sheet sheet;
    private final Map<String, UrlStats> urlStats;
    private final String filename;


    public ExcelInput(String filename, Map<String, UrlStats> urlStats) throws IOException, InvalidFormatException {
        this.filename = filename;
        this.wb = WorkbookFactory.create(new FileInputStream(this.filename));
        this.urlStats = urlStats;
        this.sheet = getFirstVisibleSheet();
    }

    public void populateSessionInformation() {
        StreamSupport.stream(sheet.spliterator(), NON_PARALLEL)
                .forEach(this::populate);
    }

    public void save() throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename);
        wb.write(fileOut);
        fileOut.close();
    }

    private void populate(Row cells) {
        UrlStats urlStats = this.urlStats.get(extractCellStringValue(cells, 0));
        if (urlStats != null) {
            cells.createCell(2).setCellValue(urlStats.getSessions());
        }else{
            cells.createCell(2).setCellValue("not found");
        }
    }

    private Sheet getFirstVisibleSheet() {
        final Optional<Sheet> first = StreamSupport.stream(wb.spliterator(), NON_PARALLEL)
                .filter(sheet -> !(wb.isSheetHidden(wb.getSheetIndex(sheet)) || wb.isSheetVeryHidden(wb.getSheetIndex(sheet))))
                .findFirst();
        //A workbook without a visible sheet is impossible to create with Microsoft Excel or via APIs (but I'll leave the check there just in case I'm missing something)
        return first.orElseThrow(() -> new RuntimeException("The workbook looks empty!"));
    }

    private String extractCellStringValue(Row row, int index) {
        Cell cell = extractCell(row, index);
        return cell.getStringCellValue();
    }

    private Cell extractCell(Row row, int i) {
        Cell cell = row.getCell(i);
        if (cell == null || cell.getStringCellValue().length() == 0) {
            throw new IllegalArgumentException("'" + "???" + "' parameter is invalid or missing.");
        }
        return cell;
    }

}
