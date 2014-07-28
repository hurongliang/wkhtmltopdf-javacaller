package com.hurl.wkhtmltopdf;

public class PageHeader {
	private String centerText;
	private String leftText;
	private String rightText;
	private String fontName;
	private int fontSize;
	private int space;
	private boolean displayLine;
	public String getCenterText() {
		return centerText;
	}
	public void setCenterText(String centerText) {
		this.centerText = centerText;
	}
	public String getLeftText() {
		return leftText;
	}
	public void setLeftText(String leftText) {
		this.leftText = leftText;
	}
	public String getRightText() {
		return rightText;
	}
	public void setRightText(String rightText) {
		this.rightText = rightText;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public int getSpace() {
		return space;
	}
	public void setSpace(int space) {
		this.space = space;
	}
	public boolean isDisplayLine() {
		return displayLine;
	}
	public void setDisplayLine(boolean displayLine) {
		this.displayLine = displayLine;
	}
	
	
}
