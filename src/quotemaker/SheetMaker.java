/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quotemaker;

/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ====================================================================
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JTable;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

/**
 * Illustrates how to create a simple scatter chart.
 *
 * @author Roman Kashitsyn
 */
public class SheetMaker {
    
    private String cleanString(String s) {
        
        s = s.replace("<html>", "");
        s = s.replace("</html>", "");
        s = s.replace("<b>", "");
        s = s.replace("</b>", "");
        s = s.trim();
        
        return s;
        
    }
    
    public void exportQuote(QuoteManager manager) throws FileNotFoundException, IOException {
        
        XSSFWorkbook wb = new XSSFWorkbook();
        
        CellStyle boldStyle = wb.createCellStyle();
        Font boldFont = wb.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);
        
        CellStyle centerStyle = wb.createCellStyle();
        centerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        
        CellStyle boldCenterStyle = wb.createCellStyle();
        boldCenterStyle.setFont(boldFont);
        boldCenterStyle.setAlignment(CellStyle.ALIGN_CENTER);
        
        CellStyle boldRightStyle = wb.createCellStyle();
        boldRightStyle.setFont(boldFont);
        boldRightStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        
        CellStyle boldLargeCenterStyle = wb.createCellStyle();
        Font boldLargeFont = wb.createFont();
        boldLargeFont.setBold(true);
        boldLargeFont.setFontHeight((short) 256);
        boldLargeCenterStyle.setFont(boldLargeFont);
        boldLargeCenterStyle.setAlignment(CellStyle.ALIGN_CENTER);
        
        XSSFSheet sheet = (XSSFSheet) wb.createSheet();
        JTable jTable = manager.frame.finalQuoteTable;
        
        //Create
        XSSFTable table = sheet.createTable();
        table.setDisplayName("KeytracerQuote");
        CTTable cttable = table.getCTTable();
        
        //Style configurations
        CTTableStyleInfo style = cttable.addNewTableStyleInfo();
        style.setName("TableStyleLight18");
        style.setShowColumnStripes(true);
        style.setShowRowStripes(false);
        
        //Set which area the table should be placed in
        AreaReference reference = new AreaReference(new CellReference("B17"), 
                new CellReference("F" + String.valueOf(17 + jTable.getRowCount() + 1)));
        cttable.setRef(reference.formatAsString());
        cttable.setId(1);
        cttable.setName("KeytracerQuote");
        
        CTTableColumns columns = cttable.addNewTableColumns();
        columns.setCount(5);
        CTTableColumn column;
        XSSFRow row;
        XSSFCell cell;
        for(int i=0; i<5; i++) {
            //Create column
            column = columns.addNewTableColumn();
            column.setName("Column");
            column.setId(i+1);
        }
        
        Row header = sheet.createRow(16);
        for(int i = 0; i < 5; i++) {
            
            Cell temp = header.createCell(i + 1);
            
            switch(i) {

                case 0:
                    temp.setCellValue("Part #");
                    break;
                case 1:
                    temp.setCellValue("Qty.");
                    break;
                case 2:
                    temp.setCellValue("Description");
                    break;
                case 3:
                    temp.setCellValue("Unit Price");
                    break;
                case 4:
                    temp.setCellValue("Ext. Price");
                    break;

            }

            temp.setCellStyle(centerStyle);
            
        }
        
        for(int rowNum = 0; rowNum < jTable.getRowCount(); rowNum++) {
            //Create row
            row = sheet.createRow(rowNum + 16 + 1);
            for(int j=0; j<5; j++) {
                //Create cell
                cell = row.createCell(j + 1);
                String val = String.valueOf(jTable.getValueAt(rowNum, j));
                if(val != null && val.equalsIgnoreCase("null") == false) {
                    
                    //This is to ensure TOTAL QUOTE is not counted, as this will be added afterwards
                    if(((rowNum == (jTable.getRowCount() - 1) && j == 3) == false)) {
                        cell.setCellValue(cleanString(val));
                    }
                    
                    if(rowNum == (jTable.getRowCount() - 1)) {
                        cell.setCellStyle(boldStyle);
                    }

                    if(j == 4) {
                        cell.setCellStyle(boldStyle);
                    }
                    
                    if(j == 0 || j == 1) {
                        cell.setCellStyle(centerStyle);
                    }
                    
                    if(manager.proxSafeCabinets.contains(cleanString(val))) {
                        
                        cell.setCellStyle(boldStyle);
                        
                    }
                    
                } else if (rowNum == (jTable.getRowCount() - 1) && j == 2) {
                    
                    cell.setCellValue("TOTAL QUOTE");
                    cell.setCellStyle(boldRightStyle);
                    
                }
                
            }
            
        }
        
        sheet.autoSizeColumn(0, true);
        sheet.autoSizeColumn(1, true);
        sheet.autoSizeColumn(2, true);
        sheet.autoSizeColumn(3, true);
        sheet.autoSizeColumn(4, true);
        
        sheet.setColumnWidth(3, 23428);
        
        System.out.println(sheet.getColumnWidth(0));
        System.out.println(sheet.getColumnWidth(1));
        System.out.println(sheet.getColumnWidth(2));
        System.out.println(sheet.getColumnWidth(3));
        System.out.println(sheet.getColumnWidth(4));
        
        //Add extras
        
        addPicture(wb, sheet);
        
        Row quoteRow = sheet.createRow(7);
        Row addressRow = sheet.createRow(9);
        Row attnRow = sheet.createRow(10);
        
        Cell quoteCell = quoteRow.createCell(3);
        Cell addressCell = addressRow.createCell(3);
        Cell attnCell = attnRow.createCell(3);
        
        quoteCell.setCellValue("Quotation");
        quoteCell.setCellStyle(boldLargeCenterStyle);
        
        addressCell.setCellValue("Somewhere in Canada");
        addressCell.setCellStyle(boldCenterStyle);
        
        attnCell.setCellValue("Attn: John Smith");
        attnCell.setCellStyle(boldCenterStyle);
        
        FileOutputStream fileOut = new FileOutputStream("quote.xlsx");
        wb.write(fileOut);
        fileOut.close();
    }
    
    public void addPicture(Workbook wb, Sheet sheet) throws FileNotFoundException, IOException {
        
        //add picture data to this workbook.
        InputStream is = new FileInputStream("res" + File.separator + "quoteLogo.png");
        byte[] bytes = IOUtils.toByteArray(is);
        int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        is.close();

        CreationHelper helper = wb.getCreationHelper();

        // Create the drawing patriarch.  This is the top level container for all shapes. 
        Drawing drawing = sheet.createDrawingPatriarch();

        //add a picture shape
        ClientAnchor anchor = helper.createClientAnchor();
        //set top-left corner of the picture,
        //subsequent call of Picture#resize() will operate relative to it
        anchor.setCol1(1);
        anchor.setRow1(1);
        Picture pict = drawing.createPicture(anchor, pictureIdx);

        //auto-size picture relative to its top-left corner
        pict.resize();
        
    }
    
}