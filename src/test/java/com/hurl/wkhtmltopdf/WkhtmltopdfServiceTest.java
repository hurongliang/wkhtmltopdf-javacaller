package com.hurl.wkhtmltopdf;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WkhtmltopdfServiceTest {
	
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

	@Test
	public void testConvert() throws IOException, InterruptedException{
		String htmlfile = "C:/Users/Administrator/Downloads/fcomb0001.html";
		String pdffile = "C:/Users/Administrator/Downloads/fcomb0001.pdf";
		WkhtmltopdfService.convert(new File(htmlfile), new File(pdffile));
	}
	
//	@Test
	public void testClasspath(){
		ProtectionDomain pd = WkhtmltopdfServiceTest.class.getProtectionDomain();
		CodeSource cs = pd.getCodeSource();
		URL location = cs.getLocation();
		String path = location.getPath();
		System.out.println(path);
	}
	
//	@Test
	public void testConvertWithHeader() throws IOException, InterruptedException{
		PageHeader header = new PageHeader();
		header.setCenterText("This is a header");
		header.setLeftText("这是一句中文");
		header.setRightText("This is a right header");
		header.setFontSize(12);
		header.setFontName("YaHei Consolas Hybrid");
		header.setDisplayLine(true);
		header.setSpace(8);
		
		String htmlfile = "C:/Users/Administrator/Downloads/fcomb0001.html";
		String pdffile = "C:/Users/Administrator/Downloads/fcomb0001.pdf";
		
		boolean success = WkhtmltopdfService.convert(new File(htmlfile), new File(pdffile),header);
		Assert.assertEquals(success, true);
	}
	
//	@Test
	public void printAllAvailableSystemFonts(){
	    GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    Font[] fonts = e.getAllFonts(); // Get the fonts
	    for (Font f : fonts) {
	      System.out.println(f.getFontName());
	    }
	}
	
//	@Test
	public void printSystemProperties(){
		System.getProperties().list(System.out);
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