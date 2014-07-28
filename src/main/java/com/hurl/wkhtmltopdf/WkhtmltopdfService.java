package com.hurl.wkhtmltopdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WkhtmltopdfService {
	private static final Logger log = LoggerFactory.getLogger(WkhtmltopdfService.class);
	
	public static boolean convert(File htmlfile, File pdffile) throws IOException, InterruptedException{
		return convert(htmlfile,pdffile,null);
	}
	
	public static boolean convert(File htmlfile, File pdffile,PageHeader header) throws IOException, InterruptedException{
		log.debug("convert "+htmlfile+" to "+pdffile);
		
		if(SystemUtils.IS_OS_WINDOWS){
			log.debug("in windows platform");
			return WkhtmltopdfService.convertUnderWindowsPlatform(htmlfile, pdffile,header);
		}else if(SystemUtils.IS_OS_LINUX){
			
		}
		
		log.debug("no suitable command tools");
		return false;
	}
	
	private static boolean convertUnderWindowsPlatform(File htmlfile, File pdffile,PageHeader header) throws IOException, InterruptedException{
		File execFile = findExecutableFile();
		log.debug("Executable file "+execFile);
		if(execFile==null || !execFile.exists()){
			log.error("Cannot convert html to pdf: executable file "+execFile+" not exists");
			return false;
		}
		List<String> commandList = new ArrayList<String>();
		commandList.add(execFile.getPath());
		if(header!=null){
			if(StringUtils.isNotBlank(header.getCenterText())){
				commandList.add("--header-center");
				commandList.add(header.getCenterText());
			}
			if(StringUtils.isNotBlank(header.getLeftText())){
				commandList.add("--header-left");
				commandList.add(header.getLeftText());
			}
			if(StringUtils.isNotBlank(header.getRightText())){
				commandList.add("--header-right");
				commandList.add(header.getRightText());
			}
			if(StringUtils.isNotBlank(header.getFontName())){
				commandList.add("--header-font-name");
				commandList.add(header.getFontName());
			}
			if(header.getFontSize()>0){
				commandList.add("--header-font-size");
				commandList.add(String.valueOf(header.getFontSize()));
			}
			if(header.isDisplayLine()){
				commandList.add("--header-line");
			}
			if(header.getSpace()>0){
				commandList.add("--header-spacing");
				commandList.add(String.valueOf(header.getSpace()));
			}
		}
		
		commandList.add(htmlfile.getAbsolutePath());
		commandList.add(pdffile.getAbsolutePath());
		
		ProcessBuilder pb = new ProcessBuilder(commandList);
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
		return rc==0;
	}
	
	private static File findExecutableFile(){
		URL exeURL = null;
		if(SystemUtils.IS_OS_WINDOWS){
			if(SystemUtils.IS_OS_WINDOWS_VISTA || SystemUtils.IS_OS_WINDOWS_7){
				exeURL = WkhtmltopdfService.class.getResource("windowsmsvc64bit/wkhtmltopdf.exe");
			}else{
				exeURL = WkhtmltopdfService.class.getResource("windowswingw32bit/wkhtmltopdf.exe");
			}
		}
		File exeFile = null;
		try{
			exeFile = new File(exeURL.toURI());
		}catch(URISyntaxException e){
			exeFile = new File(exeURL.getPath());
		}
		return exeFile;
	}
}
