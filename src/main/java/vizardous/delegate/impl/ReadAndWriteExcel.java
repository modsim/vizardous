/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */

public class ReadAndWriteExcel {

  private WritableCellFormat times;
  private File inputFile;
  private double [] timeValues = null;
  private String [] cellsNameList = null;
  private double [] charactValues = null;
  
 
  public void setOutputFile(File inputFile) {
    this.inputFile = inputFile;
  }
  public void setExcelData(double [] values, String [] cellsNameList) {
    this.timeValues         = values;  
    this.cellsNameList  = cellsNameList;
  }

  public void setExcelData(double [] timeValues, String [] cellsNameList, double [] charactValues) {
    this.timeValues         = timeValues;  
    this.cellsNameList      = cellsNameList;
    this.charactValues      = charactValues; 
  }
  public void readAndWrite() throws IOException, WriteException {
//    File file = new File(inputFile);
    WorkbookSettings wbSettings = new WorkbookSettings();
    wbSettings.setLocale(new Locale("en", "EN"));
    WritableWorkbook workbook = Workbook.createWorkbook(inputFile, wbSettings);
    workbook.createSheet("Division time", 0);
    WritableSheet excelSheet = workbook.getSheet(0);
    createLabel2(excelSheet);
    createContent(excelSheet);

    workbook.write();
    workbook.close();
  }

  private void createLabel(WritableSheet sheet)
      throws WriteException {
    // Lets create a times font
    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
    // Define the cell format
    times = new WritableCellFormat(times10pt);
    // Lets automatically wrap the cells
    times.setWrap(true);

    CellView cv = new CellView();
    cv.setFormat(times);
    cv.setAutosize(true);

    // Write a few headers
    addCaption(sheet, 0, 0, "cell-Id");
    addCaption(sheet, 1, 0, "cell division time");
  }

  private void createLabel2(WritableSheet sheet)
      throws WriteException {
    // Lets create a times font
    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
    // Define the cell format
    times = new WritableCellFormat(times10pt);
    // Lets automatically wrap the cells
    times.setWrap(true);

    CellView cv = new CellView();
//    cv.setFormat(times);
    cv.setAutosize(true);

    // Write a few headers
    addCaption(sheet, 0, 0, "cell division time");
    addCaption(sheet, 1, 0, "cell-Id");
    addCaption(sheet, 2, 0, "generation");
  }
  
  private void createContent(WritableSheet sheet) throws WriteException,
      RowsExceededException {
      
    // Write a few number
    for (int i = 1; i < timeValues.length+1; i++) {
//      // First column
//      addNumber(sheet, 0, i, timeValues [i][0]);
      // Second column
      addNumber(sheet, 0, i, timeValues [i-1]);
    }
    
    for (int j = 1; j < cellsNameList.length+1; j++) {
         // Second column
      addLabel(sheet, 1, j, cellsNameList [j-1]);
    }
    
    for (int k = 1; k < charactValues.length+1; k++) {
         // third column
      addNumber(sheet, 2, k, charactValues [k-1]);
    }
  }

  private void addCaption(WritableSheet sheet, int column, int row, String s)
      throws RowsExceededException, WriteException {
    Label label;
    label = new Label(column, row, s);
    sheet.addCell(label);
  }

  private void addNumber(WritableSheet sheet, int column, int row,
      double integer) throws WriteException, RowsExceededException {
    Number number;
    number = new Number(column, row, integer, times);
    sheet.addCell(number);
  }

  private void addLabel(WritableSheet sheet, int column, int row, String s)
      throws WriteException, RowsExceededException {
    Label label;
    label = new Label(column, row, s, times);
    sheet.addCell(label);
  }
}