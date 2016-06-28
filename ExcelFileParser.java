package com.company;
/*
A java program to get rid of time-consuming copy-paste operations among MS Excel files.
Author: Özgür Öney
Republic of Turkey Ministry of Forest & Water Management, Jun 2016.

IMPORTANT NOTE:
.xls -> File extension for versions of MS EXCEL released before 2007.
.xlsx -> File Extension for versions of MS EXCEL released after 2007.

Main difference between manipulating .xls and .xlsx is external libraries used. While .xlsx uses Apache POI XSSF, .xls uses APACHE POI HSSF.
 */

import java.io.*;
import java.lang.*;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.*;

public class ExcelFileParser {

    //for logging activities
    private static Logger logger = Logger.getLogger(ExcelFileParser.class.getPackage().getName());

    // For xlsx type
    private static XSSFWorkbook workBookXlsx;
    private XSSFSheet workBookSheetXlsx;
    private static String[][] bigpic = new String[375][3];

    // For xls type
    private HSSFWorkbook workBookXls;
    private HSSFSheet workBookSheetXls;

    /*
        Constructors.
        -ExcelFileParser(String,String,boolean) used for MS Excel documents contains "default naming for sheets"
        -ExcelFileParser(String,String,String,boolean) used for MS Excel documents containts modified naming for sheets.
     */
    public ExcelFileParser(String readpath, String writepath, boolean isXlsx) throws Exception {
        for (int i = 0; i < 375; i++)
            for (int j = 0; j < 3; j++)
                bigpic[i][j] = "X";
        if (isXlsx) {
            String[] files = listFilesForFolder(readpath);
            for (int i = 0; i < files.length; i++) {
                bigpic[i][0] = parseExcelFileIntoArr(readpath + "/" + files[i], true)[0];
                bigpic[i][1] = parseExcelFileIntoArr(readpath + "/" + files[i], true)[1];
                bigpic[i][2] = parseExcelFileIntoArr(readpath + "/" + files[i], true)[2];
            }
            String[][] a= getvalue(writepath);
            combineArrays(bigpic,a);
            printMatrix(a);

        } else {
            String[] files = listFilesForFolder(readpath);
            for (int i = 0; i < files.length; i++) {
                bigpic[i] = parseExcelFileIntoArr(readpath + files[i], false);
            }
            //writeIntoExcelFile(combineArrays(bigpic, getvalue()), writepath);
        }
    }
    public ExcelFileParser(String readpath, String writepath, String sheetName, boolean isXlsx) throws Exception {
        if(isXlsx) {
            String[] files = listFilesForFolder(readpath);
            for (int i = 0; i < files.length; i++) {
                bigpic[i][0] = parseExcelFileIntoArr(readpath + "/" + files[i], true)[0];
                bigpic[i][1] = parseExcelFileIntoArr(readpath + "/" + files[i], true)[1];
                bigpic[i][2] = parseExcelFileIntoArr(readpath + "/" + files[i], true)[2];
            }

        }else {
                String[] files = listFilesForFolder(readpath);
                for (int i = 0; i < files.length; i++) {
                    bigpic[i] = parseExcelFileIntoArr(readpath + files[i], false);
                }
        }
    }

    /*
        Assistant methods.
         -listFilesForFolder() is used to check xlsx files in a specific location and returns the location of files in a String array.
         -timeConverter() is used to solve problem of transition from Date format to String format.
         -howManyDocs() is used to count number of daily work reports for a single person. (e.g. determine whether some specific person uploaded his/her all reports for a week or not.)
         -combineArrays() is used to combine two 2D arrays into one single array, therefore it will be pretty easy to print out our "final" array into excel file.
         -printMatrix() as can be understood from its name, a helper to easily print 2D arrays.
         -cellToString() is used to change cell type from any type to String. Therefore, possible faults due to different cell type in cell manipulations is prevented.
     */
    public String[] listFilesForFolder(String location) {
        //Open new location
        final File folder = new File(location);

        //Initialize variables.
        int counter = 0;
        String extensioncheck = "";

        //For each file in determined location, do
        for (final File fileEntry : folder.listFiles()) {

            //since extension will be .xls and therefore filename is at least 4 characters long
            if (fileEntry.getName().length() > 4)//Check extension of file
                extensioncheck = fileEntry.getName().substring(fileEntry.getName().length() - 5, fileEntry.getName().length());
            else
                extensioncheck = "";

            //if its an xlsx file
            if (extensioncheck.equals(".xlsx") && !fileEntry.getName().equals("writetest.xlsx")) {
                if (fileEntry.getName().charAt(0) == '~')// "~" sign indicates an open file, we dont want to read both file's itself and it's open form.
                    ;
                else
                    counter++;
            }
        }
        String[] gecici = new String[counter];
        counter = 0;

        //For all files in the folder, whose extension is "xlsx", keep their name in an array so that they'll be available to manipulate later on.
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().length() > 4)
                extensioncheck = fileEntry.getName().substring(fileEntry.getName().length() - 5, fileEntry.getName().length());
            else
                extensioncheck = "";
            if (extensioncheck.equals(".xlsx") && !fileEntry.getName().equals("writetest.xlsx")) {
                gecici[counter] = fileEntry.getName();
                counter++;
            }
        }
        return gecici;
    }

    private static String timeConverter(double currentTime) {
        currentTime = currentTime * 24;
        int time = (int) currentTime;
        currentTime = currentTime - time;
        double temporary = currentTime * 60;
        temporary = Math.round(temporary);
        int minutes = (int) temporary;
        String something = "";
        //Some cosmetic operations to hand XX:XX:XX format in
        if (minutes == 60) {
            if (time < 10) {
                time++;
                return "0" + time + ":" + "00" + ":00";
            } else {
                time++;
                return time + ":" + "00" + ":00";
            }
        } else {
            if (minutes < 10)
                something = "0" + minutes;
            else
                something = minutes + "";
            if (time < 10)
                return "0" + time + ":" + something + ":00";
            else
                return time + ":" + something + ":00";
        }
    }

    private static int howManyDocs(String[][]arr, int index){
        int counter=1;
        boolean check=true;
        while(arr!=null && check)
        {
            if(arr[index][1].toLowerCase().equals(arr[index][1].toLowerCase()))
                counter++;
        }
        return counter;
    }

    public static void combineArrays(String[][] allInputs, String[][] calismaform) throws Exception{

        /*
        Imagine to 2D arrays, F and S. While variable i scans F from head to toe, variable j does it for array S. We just read a single column on S, bu we will
        write 0 to 5 columns in a single row of S. therefore, an extra variable k will be defined to walk through columns of S.
         */
        for(int i=0, j=0, k=1; i<allInputs.length; i++){
            try{
                System.out.println(allInputs[i][1]);
                System.out.println(calismaform[j][0]);
                System.out.println(allInputs[i][1].toLowerCase().equals(calismaform[j][0].toLowerCase()));
                //Use toLowerCase() and replaceAll() to prevent faults sourced from upper-lowerCase conflict or because of an extra space character.
                if(allInputs[i][1].toLowerCase().replaceAll(" ", "").equals(calismaform[j][0].toLowerCase().replaceAll(" ", ""))){
                    calismaform[j][k]=allInputs[i][2];
                    k++;
                }
                else{
                    j++;
                    i--;
                    k=1;
                }
            }catch(NullPointerException e){
                System.out.println(allInputs[i][1] + "..." + calismaform[j][0]);
            }
        }

    }

    static void printMatrix(String[][] grid) {
        for(int r=0; r<grid.length; r++) {
            for(int c=0; c<grid[r].length; c++)
                System.out.print(grid[r][c] + " ");
            System.out.println();
        }
    }

    public static String cellToString(XSSFCell cell) {
        BaseXSSFFormulaEvaluator evaluator = workBookXlsx.getCreationHelper().createFormulaEvaluator();
        int type = cell.getCellType();
        Object result;
        switch (type) {
            case XSSFCell.CELL_TYPE_NUMERIC: //0
                result = cell.getNumericCellValue();
                break;
            case XSSFCell.CELL_TYPE_STRING: //1
                result = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_FORMULA: //2
                //Evaluate MS Excel cell's formula
                evaluator.evaluateFormulaCell(cell);
                result=(timeConverter(cell.getNumericCellValue())).toString();
            case XSSFCell.CELL_TYPE_BLANK: //3
                result = "_";
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN: //4
                result = cell.getBooleanCellValue();
                break;
            case XSSFCell.CELL_TYPE_ERROR: //5
                throw new RuntimeException("Hücreleri metne çevirmede bir hata oluştu, lütfen hücreleri kontrol ediniz.");
            default:
                throw new RuntimeException("Bilinmeyen hücre tipi: " + type);
        }
        return result.toString();
    }

    /*
        Methods for reading from MS Excel files.
        -parseExcelFileIntoArr parses excel file into an array, returns an 2D array that contains Date, Name and work-time variables.
        -parseExcelFile parses excel file into a List, so that some manipulations may be easier in further usage. Since array manipulation (walking around array and having limited memory) could be trouble for some situations, this method was written to be used
            in such scenarios by considering special request of supervisor.
        -getValue simply parses all excel file into an array, no manipulations like parseExcelFileIntoArr.
     */

    protected String[] parseExcelFileIntoArr(String path, boolean isXlsx) throws Exception {
        workBookXlsx = new XSSFWorkbook(new FileInputStream(path));
        workBookSheetXlsx = workBookXlsx.getSheetAt(0);
        //Create a trivial array to store all information taken from .xlsx doc
        String[][] arr = new String[workBookSheetXlsx.getPhysicalNumberOfRows() + 10][workBookSheetXlsx.getRow(0).getPhysicalNumberOfCells() + 10];
        //Create an array to keep data to return
        String[] returnalread;
        //Define formula evaluator, which will be used in case we see a MS Excel formulated cell.
        BaseXSSFFormulaEvaluator evaluator = workBookXlsx.getCreationHelper().createFormulaEvaluator();
        //Check if the document is .xlsx
        if (isXlsx) {
            //Loop will scan line by line
            for (int i = 0, numberOfRows = workBookSheetXlsx.getPhysicalNumberOfRows(); i < numberOfRows + 1; i++) {
                XSSFRow row = workBookSheetXlsx.getRow(i);
                //If row is null (user did not even click on the row or did not define a known format for row)
                if (row != null) {
                    //Start going right and look for respective cells.
                    for (int j = 0, numberOfColumns = row.getLastCellNum(); j < numberOfColumns; j++) {
                        XSSFCell cell = row.getCell(j);
                        //Though row is not NULL, cells still can. Check whether or not.
                        if (cell != null) {
                            //To guarantee to read cell, exception is defined.
                            try {
                                if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    arr[i][j] = cell.getStringCellValue();
                                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                                    arr[i][j] = "";
                                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
                                    arr[i][j] = String.valueOf(cell.getBooleanCellValue());
                                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    arr[i][j] = String.valueOf(cell.getNumericCellValue());
                                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
                                    //Initialize variables
                                    int totaltimespent = 0;
                                    //Evaluate MS Excel cell's formula
                                    evaluator.evaluateFormulaCell(cell);
                                    //Now we have time in manner of DAY, so convert it into hour:minute:second manner.
                                    arr[i][j] = timeConverter(cell.getNumericCellValue());
                                } else {
                                    arr[i][j] = cell.getStringCellValue();
                                }
                            }//Thing to do whenever we cannot read a cell due to format problems.
                            catch (Exception e) {
                                logger.fatal("Oops! Can't read cell (row = " + i + ", column = " + j + ") in the excel file! Change cell format to 'Text', please!");
                            }
                        }//If row is NULL; fill it up with "" to guarantee it's string form.
                        else {
                            try {
                                arr[i][j] = "";
                            } catch (ArrayIndexOutOfBoundsException a) {
                                System.out.println("Sorunlarla karşılaşıldı. Lütfen belge içeriğini kontrol ediniz.");
                                break;
                            }


                        }
                    }
                }
            }
        } else {
            //For MS Excel version's before than 2007, .xls
            workBookXls = new HSSFWorkbook(new FileInputStream(path));
            workBookSheetXls = workBookXls.getSheetAt(0);
            return null;
        }
        //We got all data in our trivial array. Now we have to select useful data among any other in array.
        returnalread = new String[30];
        String temporary = String.valueOf(arr[0][11]);

        //Divide "Çalışma tarihi: xx.xx.xx" format into just date
        try {
            String[] dummy = temporary.split(":");
            returnalread[0] = dummy[1];
            returnalread[1] = arr[5][5];
            returnalread[2] = arr[10][5];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(arr[5][5] + " dosyası okunamadı!");
            returnalread[0] = "";
            returnalread[1] = "";
            returnalread[2] = "";
        }
        arr = null;
        System.gc();
        return returnalread;
    }

    protected List<List<String>> parseExcelFile(boolean isXlsx) throws Exception {
        //List of lists, makes it possible to have a structure like ragged arrays.
        List<List<String>> parsedExcelFile = new ArrayList<List<String>>();
        //same procedure above, if file is .xlsx, branch here.
        if (isXlsx) {
            for (int i = 0, numberOfRows = workBookSheetXlsx.getPhysicalNumberOfRows(); i < numberOfRows + 1; i++) {
                XSSFRow row = workBookSheetXlsx.getRow(i);
                if (row != null) {
                    List<String> parsedExcelRow = new ArrayList<String>();
                    for (int j = 0, numberOfColumns = row.getLastCellNum(); j < numberOfColumns; j++) {
                        XSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            try {
                                if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    parsedExcelRow.add(cell.getStringCellValue());
                                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                                    parsedExcelRow.add("");
                                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
                                    parsedExcelRow.add(String.valueOf(cell.getBooleanCellValue()));
                                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    parsedExcelRow.add(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
                                    parsedExcelRow.add("");
                                } else {
                                    parsedExcelRow.add(cell.getStringCellValue());
                                }
                            } catch (Exception e) {
                                logger.fatal("Oops! Can't read cell (row = " + i + ", column = " + j + ") in the excel file! Change cell format to 'Text', please!");
                                return null;
                            }
                        } else {
                            parsedExcelRow.add("");
                        }
                    }
                    parsedExcelFile.add(parsedExcelRow);
                }
            }
            //If file is an .xls document, we will branch here.
        } else {
            for (int i = 0, numberOfRows = workBookSheetXls.getPhysicalNumberOfRows(); i < numberOfRows + 1; i++) {
                HSSFRow row = workBookSheetXls.getRow(i);
                if (row != null) {
                    List<String> parsedExcelRow = new ArrayList<String>();
                    for (int j = 0, numberOfColumns = row.getLastCellNum(); j < numberOfColumns; j++) {
                        HSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            try {
                                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                    parsedExcelRow.add(cell.getStringCellValue());
                                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                                    parsedExcelRow.add("");
                                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
                                    parsedExcelRow.add(String.valueOf(cell.getBooleanCellValue()));
                                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                    parsedExcelRow.add(String.valueOf(cell.getNumericCellValue()));
                                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
                                    parsedExcelRow.add(String.valueOf(""));
                                } else {
                                    parsedExcelRow.add(cell.getStringCellValue());
                                }
                            } catch (Exception e) {
                                logger.fatal("Oops! Can't read cell (row = " + i + ", column = " + j + ") in the excel file! Change cell format to 'Text', please!");
                                return null;
                            }
                        } else {
                            parsedExcelRow.add("");
                        }
                    }
                    parsedExcelFile.add(parsedExcelRow);
                }
            }
        }

        return parsedExcelFile;
    }

    public static String[][] getvalue(String path) {
        XSSFRow row;
        XSSFCell cell;
        String[][] value =null;
        double[][] nums = null;

        try {
            FileInputStream inputStream = new FileInputStream(path);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // get sheet number
            int sheetCn = workbook.getNumberOfSheets();

            for (int cn = 0; cn < sheetCn; cn++) {

                // get 0th sheet data
                XSSFSheet sheet = workbook.getSheetAt(cn);

                // get number of rows from sheet
                int rows = sheet.getPhysicalNumberOfRows();

                // get number of cell from row
                int cells = sheet.getRow(cn).getPhysicalNumberOfCells();

                value = new String[rows][cells];

                for (int r = 0; r < rows; r++) {
                    row = sheet.getRow(r); // bring row
                    if (row != null) {
                        for (int c = 0; c < cells; c++) {
                            cell = row.getCell(c);
                            nums = new double[rows][cells];

                            if (cell != null) {

                                switch (cell.getCellType()) {

                                    case XSSFCell.CELL_TYPE_FORMULA:
                                        value[r][c] = cell.getCellFormula();
                                        break;

                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        value[r][c] = ""
                                                + cell.getNumericCellValue();
                                        break;

                                    case XSSFCell.CELL_TYPE_STRING:
                                        value[r][c] = ""
                                                + cell.getStringCellValue();
                                        break;

                                    case XSSFCell.CELL_TYPE_BLANK:
                                        value[r][c] = "[BLANK]";
                                        break;

                                    case XSSFCell.CELL_TYPE_ERROR:
                                        value[r][c] = ""+cell.getErrorCellValue();
                                        break;
                                    default:
                                }
                                //System.out.print(value[r][c]);

                            } else {
                                //System.out.print("[null]\t");
                            }
                        } // for(c)
                        //System.out.print("\n");
                    }
                } // for(r)
            }
        } catch (NullPointerException e) {
            return value;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.deepToString(value));
        return  value;
    }


    /*
        Methods for writing into MS Excel files.
        -writeIntoExcelFile() is used to print out given an array into pre-determined output file.
     */
    protected void writeIntoExcelFile(String[][] arr, String path) {
        //Protection against FileNotFoundException
        try {
            FileInputStream inp = new FileInputStream(path);
            XSSFWorkbook wb = null;
            //Protection against invalid format, a .xls file cannot be accepted.
            try {
                wb = (XSSFWorkbook) WorkbookFactory.create(inp);
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }
            final int counterforcells = 2;

            int arraysize = 0;
            int counter = 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i][0].length() == 11)
                    arraysize++;
            }

            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row, rowdate;

            Cell cell, namecell, datecell;
            rowdate = sheet.getRow(1);
            for (int i = 0; i < workBookSheetXlsx.getPhysicalNumberOfRows(); i++) {
                for (int j = 0; j < workBookSheetXlsx.getRow(0).getPhysicalNumberOfCells() - 1; j++) {

                    row = sheet.getRow(i + 2);
                    cell = row.getCell(j + 1);
                    namecell = row.getCell(0);
                    datecell = rowdate.getCell(j);
                    cell = row.createCell(j + 1);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    System.out.println("XLSX'den alınan veri : " + namecell.getStringCellValue());
                    if (namecell.getStringCellValue().toLowerCase().equals(arr[counter][1].toLowerCase())) {
                        System.out.println("Yazdirilmasi gereken deger..." + String.valueOf(arr[counter][counterforcells]) + "..." + arr[counter][1]);
                        cell.setCellValue(String.valueOf(arr[counter][counterforcells]));
                        counter++;
                    } else if (!namecell.getStringCellValue().toLowerCase().equals(arr[counter][1].toLowerCase())) {
                        i++;
                        System.out.println(arraysize + "..." + i);
                    } else if (!datecell.getStringCellValue().equals(arr[counter][0])) {
                        j++;
                    }

                }
            }
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ****Method to write to XL****
    public void writeXL (String sPath, String iSheet, String[][] xData) throws Exception {
        File outFile = new File(sPath);
        HSSFWorkbook wb = new HSSFWorkbook();                // The workbook
        HSSFSheet osheet = wb.createSheet(iSheet);           // The worksheet
        int xR_TS = xData.length;                            // The length or number of rows of the 2 dimensional array
        int xC_TS = xData[0].length;                         // The length or number of columns of the first row
        for (int myrow = 0; myrow < xR_TS; myrow++) {        // Go through each row
            HSSFRow row = osheet.createRow(myrow);           // Create a new row
            for (int mycol = 0; mycol < xC_TS; mycol++) {    // Go through each column
                HSSFCell cell = row.createCell(mycol);       // Create a new column
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(xData[myrow][mycol]);
            }
            FileOutputStream fOut = new FileOutputStream(outFile);
            wb.write(fOut);
            fOut.flush();
            fOut.close();
        }
    }

}
