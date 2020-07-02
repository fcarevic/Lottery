package pdfcreator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.stream.Stream;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.clipper.Paths;

public class PdfWriter {
	public static void createPDF(String filename, LinkedList<Integer[]> allcombinations, int transactionID ) {
		Document document = new Document();
		try {
			com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filename));
	
		document.open();
		 
		PdfPTable table = new PdfPTable(7);
		addTableHeader(table,transactionID);
		addRows(table,allcombinations);
		 
		document.add(table);
		document.close();
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		
		
		
	}
	private static void  addRows(PdfPTable table, LinkedList<Integer[]> allcombinations) {
	   for(Integer[] comb : allcombinations) {
		  for(Integer num:comb) {
		
			  table.addCell(num+"");
						  
			  
		  }
	   }
	}
	
	private static void addTableHeader(PdfPTable table, int transactionID) {
	    Stream.of("Drzavna ","lutirija  ", "Srbije "," " , ""," ", "ID: " + transactionID)
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(0);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}
	
	
	public static void main(String [] args) {
		
	//	createPDF(filename, allcombinations, transactionID);
		
	}
	
}
