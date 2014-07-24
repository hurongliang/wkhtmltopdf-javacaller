package com.hurl.wkhtmltopdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFService {
	private static final Logger log = LoggerFactory.getLogger(PDFService.class);
	
	public static void convert(File htmlfile, File pdffile) throws IOException, InterruptedException{
		log.debug("convert "+htmlfile+" to "+pdffile);
		if(SystemUtils.IS_OS_WINDOWS){
			log.debug("in windows platform");
			PDFService.convertUnderWindowsPlatform(htmlfile, pdffile);
		}else if(SystemUtils.IS_OS_LINUX){
			
		}
	}
	
	private static void convertUnderWindowsPlatform(File htmlfile, File pdffile) throws IOException, InterruptedException{
		File execFile = findExecutableFile();
		log.debug("Executable file "+execFile);
		if(execFile==null || !execFile.exists()){
			log.error("Cannot convert html to pdf: executable file "+execFile+" not exists");
			return;
		}
		
		ProcessBuilder pb = new ProcessBuilder(execFile.getPath(),htmlfile.getAbsolutePath(),pdffile.getAbsolutePath());
		pb.redirectErrorStream(true);
		
		Process p = pb.start();

		InputStreamReader isr = new InputStreamReader(p.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		while((line=br.readLine())!=null){
			log.debug(">> "+line);
		}
		
		int rc = p.waitFor();
		
		log.debug("convert done with exit code "+rc+".");
	}
	
	private static File findExecutableFile(){
		URL exeURL = PDFService.class.getResource("windowsmsvc64bit/wkhtmltopdf.exe");
		File exeFile = null;
		try{
			exeFile = new File(exeURL.toURI());
		}catch(URISyntaxException e){
			exeFile = new File(exeURL.getPath());
		}
		return exeFile;
	}
}
