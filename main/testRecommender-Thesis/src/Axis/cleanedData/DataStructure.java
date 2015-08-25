package Axis.cleanedData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class DataStructure {

	public static void main(String[] args) throws IOException {
		DataStructure ds = new DataStructure();
		ds.readExcel();
	}

	// change the output file .txt to .csv then choose figure formate
	public void readExcel() throws IOException {
		// InputStream ExcelFileToRead = new FileInputStream(
		// "./src/Axis/cleanedData/packages_100_Converted.xls");
		InputStream ExcelFileToRead = new FileInputStream(
				"./src/Axis/cleanedData/date_result.xls");

		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		HSSFCell cell;

		Iterator<Row> rows = sheet.rowIterator();
		// write to txt file
		File statText = new File("statsTest.txt");
		FileOutputStream is = new FileOutputStream(statText);
		OutputStreamWriter osw = new OutputStreamWriter(is);
		Writer w = new BufferedWriter(osw);
		List<Integer> list = new ArrayList<Integer>();

		while (rows.hasNext()) {
			row = (HSSFRow) rows.next();
			Iterator<Cell> cells = row.cellIterator();
			int c = 0, nc = 0, ig = 0;
			while (cells.hasNext()) {
				cell = (HSSFCell) cells.next();
				if (cell.getStringCellValue().equals("f")) { // c
//					 System.out.print(row.getRowNum()+cell.getStringCellValue()+" ");
//					 w.write("c: "+row.getRowNum()+cell.getStringCellValue()+" \n");
					
					c++;
				} else if (cell.getStringCellValue().equals("s")) {// nc
//					 System.out.println(row.getRowNum()+cell.getStringCellValue()+" ");
					// w.write(row.getRowNum()+cell.getStringCellValue()+" \n");
					nc++;
				} else if (cell.getStringCellValue().equals("n")) {// ig
//					 w.write(row.getRowNum()+cell.getStringCellValue()+" \n");
					ig++;
				}
			}
			// System.out.println(row.getCell(0));//c,nc,ig
			// w.write(row.getCell(0) + ";" + nc + ";" + c
			// + ";" + ig + "\n");
			w.write(c + ";");
			list.add(c);
		}

		Set<Integer> uniqueSet = new HashSet<Integer>(list);
		 for (int temp : uniqueSet) {  
	            System.out.println(temp + ";" + Collections.frequency(list, temp));  
	        }
		w.close();

	}
}
