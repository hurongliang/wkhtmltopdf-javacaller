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
	 * @param header header information to be added to each page.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean convert(File htmlfile, File pdffile,PageHeader header,PageFooter footer) throws IOException, InterruptedException{
		log.debug("convert "+htmlfile+" to "+pdffile);
		
		if(SystemUtils.IS_OS_WINDOWS){
			return convertUnderWindowsPlatform(htmlfile,pdffile,header,footer);
		}
		
		if(SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX){
			return convertUnderWindowsMacOSPlatform(htmlfile,pdffile,header,footer);
		}
		
		log.error("Unsupported system platform");
		return false;
	}
	
	private static boolean convertUnderWindowsPlatform(File htmlfile, File pdffile,PageHeader header,PageFooter footer) throws IOException, InterruptedException{
		log.debug("convert under windows platform");
		
		File executableFile = findExecutableFileUnderWindowsPlatform();
		
		if(executableFile==null){
			log.error("Cannot convert html to pdf: no suitable command tools");
			return false;
		}
		
		if(!executableFile.exists()){
			log.error("Cannot convert html to pdf: command tools "+executableFile+" not exists");
			return false;
		}

		return WkhtmltopdfService.executeCommand(executableFile, htmlfile, pdffile, header,footer);
	}
	
	private static boolean executeCommand(File executableFile, File htmlfile, File pdffile,PageHeader header,PageFooter footer) throws IOException, InterruptedException{
		List<String> commandList = new ArrayList<String>();
		commandList.add(executableFile.getAbsolutePath());
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
			if(StringUtils.isNotBlank(header.getFontSize())){
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
		if(footer!=null){
			if(StringUtils.isNotBlank(footer.getCenterText())){
				commandList.add("--footer-center");
				commandList.add(footer.getCenterText());
			}
			if(StringUtils.isNotBlank(footer.getLeftText())){
				commandList.add("--footer-left");
				commandList.add(footer.getLeftText());
			}
			if(StringUtils.isNotBlank(footer.getRightText())){
				commandList.add("--footer-right");
				commandList.add(footer.getRightText());
			}
			if(StringUtils.isNotBlank(footer.getFontName())){
				commandList.add("--footer-font-name");
				commandList.add(footer.getFontName());
			}
			if(StringUtils.isNotBlank(footer.getFontName())){
				commandList.add("--footer-font-size");
				commandList.add(String.valueOf(footer.getFontSize()));
			}
			if(footer.isDisplayLine()){
				commandList.add("--footer-line");
			}
			if(footer.getSpace()>0){
				commandList.add("--footer-spacing");
				commandList.add(String.valueOf(footer.getSpace()));
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
		File rootDirectory = findRootDirectoryForTools();
		
		if(rootDirectory==null){
			return null;
		}
		
		File execFile32bit = new File(rootDirectory,"win32"+File.separator+"wkhtmltopdf.exe");
		File execFile64bit = new File(rootDirectory,"win64"+File.separator+"wkhtmltopdf.exe");
		
		boolean is64bit = is64bitArchitecture();
		
		log.debug("is 64-bit arch "+is64bit);
		
		if(is64bit && execFile64bit.exists()){
			log.debug("find executable file under windows os 64 bit: "+execFile64bit);
			return execFile64bit;
		}else{
			if(execFile32bit.exists()){
				log.debug("find executable file under windows os 32 bit: "+execFile32bit);
				return execFile32bit;
			}else{
				log.debug("executable file under windows os not exists");
				return null;
			}
		}
	}
	
	private static boolean is64bitArchitecture(){
		boolean is64bit = false;
		
		String arch = System.getProperty("os.arch");
		String archDataModel = System.getProperty("sun.arch.data.model");
		
		boolean isarch64bit = StringUtils.equalsIgnoreCase(arch, "amd64") || StringUtils.equalsIgnoreCase(arch, "x86_64");
		boolean isdatamodel64bit = StringUtils.equalsIgnoreCase(archDataModel, "64");
		
		if(isarch64bit && isdatamodel64bit){
			is64bit = true;
		}
		
		return is64bit;
	}
	
	private static boolean convertUnderWindowsMacOSPlatform(File htmlfile, File pdffile,PageHeader header,PageFooter footer) throws IOException, InterruptedException{
		log.debug("convert under mac os");
		
		File jnalibrary = findJNALibrary();
		if(jnalibrary==null){
			log.error("Cannot find jna library path");
			return false;
		}
		
		String key = "jna.library.path";
		String jnalibrarypathbefore = System.getProperty(key);
		
		System.setProperty(key, jnalibrary.getAbsolutePath());
		int code;
		
		boolean headerenable = header!=null;
		String headerlefttext = header!=null?header.getLeftText():null;
		String headercentertext = header!=null?header.getCenterText():null;
		String headerrighttext = header!=null?header.getRightText():null;
		String headerfontname = header!=null?header.getFontName():null;
		String headerfontsize = header!=null?header.getFontSize():null;
		
		boolean footerenable = footer!=null;
		String footerlefttext = footer!=null?footer.getLeftText():null;
		String footercentertext = footer!=null?footer.getCenterText():null;
		String footerrighttext = footer!=null?footer.getRightText():null;
		String footerfontname = footer!=null?footer.getFontName():null;
		String footerfontsize = footer!=null?footer.getFontSize():null;
		
		code = WkhtmltopdfcallLibrary.INSTANCE.htmltopdfcallwithheader(htmlfile.getAbsolutePath(), pdffile.getAbsolutePath(), headerenable, headerlefttext, headercentertext, headerrighttext, headerfontname, headerfontsize, footerenable, footerlefttext, footercentertext, footerrighttext, footerfontname, footerfontsize);
		
		log.debug("convert result code "+code);
		
		System.setProperty(key, jnalibrarypathbefore);
		
		return code==0;
	}
	
	private static File findJNALibrary(){
		File rootDirectory = findRootDirectoryForTools();
		
		if(rootDirectory==null){
			return null;
		}
		
		File jnalibrary = new File(rootDirectory,"macos");
		
		if(!jnalibrary.exists()){
			log.debug("executable file "+jnalibrary+" not exists");
			return null;
		}
		
		log.debug("jna library path: "+jnalibrary);
		return jnalibrary;
	}
	
	/** utilities */
	
	private static File findRootDirectoryForTools(){
		File classpath = getClasspath();
		
		File rootDirectory = new File(classpath.getParent(),"tools"+File.separator+"wkhtmltopdf");
		
		if(!rootDirectory.exists()){
			log.warn("tools root directory "+rootDirectory+" not exists");
			return null;
		}
		
		log.debug("root directory for tools "+rootDirectory);
		
		return rootDirectory;
	}
	
	private static File getClasspath(){
		URL classpathURL = WkhtmltopdfService.class.getResource("/");
		
		File classpath = null;
		try{
			classpath = new File(classpathURL.toURI());
		}catch(URISyntaxException e){
			classpath = new File(classpathURL.getPath());
		}
		
		return classpath;
	}
}
