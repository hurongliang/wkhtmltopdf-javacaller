package com.hurl.wkhtmltopdf;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

public class PdfServiceTests {
	
//	@Test
	public void testConvertWithMultiThread() throws IOException, InterruptedException{
		String htmlfile = "C:/Users/Administrator/Downloads/fcomb0001.html";
		for(int i=0;i<1;i++){
			String pdffile = "C:/Users/Administrator/Downloads/fcomb0001_"+i+".pdf";
			Thread t = new Thread(new ExeRunnable(htmlfile,pdffile));
			t.start();
		}
		
		while(true){
			Thread.sleep(5000);
		}
	}

//	@Test
	public void testConvert() throws IOException, InterruptedException{
		String htmlfile = "C:/Users/Administrator/Downloads/fcomb0001.html";
		String pdffile = "C:/Users/Administrator/Downloads/fcomb0001.pdf";
		WkhtmltopdfService.convert(new File(htmlfile), new File(pdffile));
	}
	
	@Test
	public void testConvertWithHeader() throws IOException, InterruptedException{
		PageHeader header = new PageHeader();
		header.setCenterText("This is a header");
		header.setLeftText("这是一句中文");
		header.setRightText("This is a right header");
		header.setFontSize(8);
		header.setFontName("YaHei Consolas Hybrid");
		
		String htmlfile = "C:/Users/Administrator/Downloads/fcomb0001.html";
		String pdffile = "C:/Users/Administrator/Downloads/fcomb0001.pdf";
		
		WkhtmltopdfService.convert(new File(htmlfile), new File(pdffile),header);
	}
	
//	@Test
	public void printAllAvailableSystemFonts(){
	    GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    Font[] fonts = e.getAllFonts(); // Get the fonts
	    for (Font f : fonts) {
	      System.out.println(f.getFontName());
	    }
	}
}

class ExeRunnable implements Runnable{
	private String htmlfile;
	private String pdffile;
	public ExeRunnable(String htmlfile,String pdffile){
		this.htmlfile = htmlfile;
		this.pdffile = pdffile;
	}
	public void run() {
		try {
			WkhtmltopdfService.convert(new File(htmlfile), new File(pdffile));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}