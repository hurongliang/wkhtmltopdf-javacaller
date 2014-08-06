package com.hurl.wkhtmltopdf;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface WkhtmltopdfcallLibrary extends Library{
	WkhtmltopdfcallLibrary INSTANCE = (WkhtmltopdfcallLibrary)Native.loadLibrary("wkhtmltopdfcall",WkhtmltopdfcallLibrary.class);
	
	int htmltopdfcall(String html, String pdf);

	int htmltopdfcallwithheader(String html, String pdf, 
			boolean headerenable,
			String headerlefttext, String headercentertext,String headerrighttext, 
			String headerfontname, String headerfontsize, 
			boolean footerenable,
			String footerlefttext, String footercentertext,String footerrighttext, 
			String footerfontname, String footerfontsize);
}
