package com.hurl.wkhtmltopdf;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

public class PdfServiceTests {
	
	@Test
	public void testConvert() throws IOException, InterruptedException{
		String htmlfile = "D:\\tmp\\om0205.html";
		String pdffile = "D:\\tmp\\om0205.pdf";
		PDFService.convert(new File(htmlfile), new File(pdffile));
	}
}
