package com.hurl.wkhtmltopdf;

public class PageFooter {
	private String centerText;
	private String leftText;
	private String rightText;
	private String fontName;
	private String fontSize;
	private int space;
	private boolean displayLine;
	public String getCenterText() {
		return centerText;
	}
	
	/**
	 * Set text to be displayed at the center of header.
	 * @param centerText
	 */
	public void setCenterText(String centerText) {
		this.centerText = centerText;
	}
	public String getLeftText() {
		return leftText;
	}
	/**
	 * Set test to be displayed at the left of header.
	 * @return
	 */
	public void setLeftText(String leftText) {
		this.leftText = leftText;
	}
	public String getRightText() {
		return rightText;
	}

	/**
	 * Set test to be displayed at the right of header.
	 * @return
	 */
	public void setRightText(String rightText) {
		this.rightText = rightText;
	}
	public String getFontName() {
		return fontName;
	}
	/**
	 * Set font name of the header text.
	 * @param fontName
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public String getFontSize() {
		return fontSize;
	}
	/**
	 * Set font size of the header text.
	 * @param fontSize
	 */
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	
	public int getSpace() {
		return space;
	}
	/**
	 * Set space between header and content.
	 * @param space
	 */
	public void setSpace(int space) {
		this.space = space;
	}
	public boolean isDisplayLine() {
		return displayLine;
	}
	/**
	 * Set whether display a line under the header.
	 * @param displayLine display line under header if true, or not display line under header if false.
	 */
	public void setDisplayLine(boolean displayLine) {
		this.displayLine = displayLine;
	}
}
