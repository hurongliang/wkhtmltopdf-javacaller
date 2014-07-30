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

/**
 * A service to convert html file to pdf file.
 * @author Administrator
 *
 */
public class WkhtmltopdfService {
	private static final Logger log = LoggerFactory.getLogger(WkhtmltopdfService.class);
	
	/**
	 * Convert html file to pdf file
	 * @param htmlfile the html file to be converted.
	 * @param pdffile the pdf file will be generated.
	 * @return true if converting is successfully, or false if converting is failed.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean convert(File htmlfile, File pdffile) throws IOException, InterruptedException{
		return convert(htmlfile,pdffile,null);
	}
	
	/**
	 * Convert html file to pdf file
	 * @param htmlfile the html file to be converted.
	 * @param pdffile the pdf file will be generated.
	 * @return true if converting is successfully, or false if converting is failed.
	 * @param header header information to be added to each page.
	 * @throws IOException
	 * @throws InterruptedException
	 */
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
		File executableFile = findExecutableFileUnderWindowsPlatform();
		log.debug("Executable file "+executableFile);
		if(executableFile==null || !executableFile.exists()){
			log.error("Cannot convert html to pdf: executable file "+executableFile+" not exists");
			return false;
		}
		List<String> commandList = new ArrayList<String>();
		commandList.add(executableFile.getPath());
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
	
	private static File findExecutableFileUnderWindowsPlatform(){
		boolean is64bit = false;
		String arch = System.getProperty("os.arch");
		String archDataModel = System.getProperty("sun.arch.data.model");
		if(StringUtils.equalsIgnoreCase(arch, "amd64") && StringUtils.equalsIgnoreCase(archDataModel, "64")){
			is64bit = true;
		}
		
		String pathFor32bit = "tools/wkhtmltopdf/win32/wkhtmltopdf.exe";
		String pathFor64bit = "tools/wkhtmltopdf/win64/wkhtmltopdf.exe";
		
		log.debug("arch "+arch);
		log.debug("arch data model "+archDataModel);
		log.debug("is 64-bit "+is64bit);
		log.debug("path for 32-bit "+pathFor64bit);
		log.debug("path for 64-bit "+pathFor64bit);
		
		File classpath = getClasspath();
		log.debug("classpath "+classpath);
		
		File exeFile = null;
		if(is64bit){
			exeFile = new File(classpath,pathFor64bit);
			if(!exeFile.exists()){//64-bit version not exists, get 32-bit version.
				log.debug("wkhtmltopdf.exe 64-bit not exists, try to find 32-bit version");
				exeFile = new File(classpath,pathFor32bit);
			}
		}else{
			exeFile = new File(classpath,pathFor32bit);
		}
		
		if(!exeFile.exists()){
			log.warn("file "+exeFile+" not exists");
			return null;
		}
		
		return exeFile;
	}
	
	private static File getClasspath(){
		URL classpathURL = WkhtmltopdfService.class.getProtectionDomain().getCodeSource().getLocation();
		
		File classpath = null;
		try{
			classpath = new File(classpathURL.toURI());
		}catch(URISyntaxException e){
			classpath = new File(classpathURL.getPath());
		}
		
		return classpath;
	}
}
