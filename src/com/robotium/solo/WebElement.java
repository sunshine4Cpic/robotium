package com.robotium.solo;

import java.util.Hashtable;

/**
 * Represents an element shown in a WebView.  
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

public class WebElement {
	
	private int locationX = 0;
	private int locationY = 0;
	private String id;
	private String text;
	private String name;
	private String className;
	private String tagName;
	private Hashtable<String, String> attributes;
	
	//孙建平添加
	private int upperLeftCornerX=0;
	private int upperLeftCornerY=0;
	
	private int lowerRightCornerX=0;
	private int lowerRightCornerY=0;
	
	public void setUpperLeftCornerX(int upperLeftCornerX){
		this.upperLeftCornerX = upperLeftCornerX;
	}
	
	public int getUpperLeftCornerX(){
		return this.upperLeftCornerX;
	}
	
	public void setUpperLeftCornerY(int upperLeftCornerY){
		this.upperLeftCornerY = upperLeftCornerY;
	}
	
	public int getUpperLeftCornerY(){
		return this.upperLeftCornerY;
	}
	
	public void setLowerRightCornerX(int lowerRightCornerX){
		this.lowerRightCornerX = lowerRightCornerX;
	}
	
	public int getLowerRightCornerX(){
		return this.lowerRightCornerX;
	}
	
	public void setLowerRightCornerY(int lowerRightCornerY){
		this.lowerRightCornerY = lowerRightCornerY;
	}
	
	public int getLowerRightCornerY(){
		return this.lowerRightCornerY;
	}
	
	/**
	 * Constructs this object. 
	 * 
	 * @param webId the given web id
	 * @param textContent the given text to be set
	 * @param name the given name to be set
	 * @param className the given class name to set
	 * @param tagName the given tag name to be set
	 * @param attributes the attributes to set
	 */

	public WebElement(String webId, String textContent, String name, String className, String tagName, Hashtable<String, String> attributes) {

		this.setId(webId);
		this.setTextContent(textContent);
		this.setName(name);
		this.setClassName(className);
		this.setTagName(tagName);
		this.setAttributes(attributes);
	}

	/**
	 * Returns the WebElements location on screen.
	 */

	public void getLocationOnScreen(int[] location) {

		location[0] = locationX;
		location[1] = locationY;
	}

	/**
	 * Sets the X location.
	 * 
	 * @param locationX the X location of the {@code WebElement}
	 */

	public void setLocationX(int locationX){
		this.locationX = locationX;
	}

	/**
	 * Sets the Y location.
	 * 
	 * @param locationY the Y location of the {@code WebElement}
	 */

	public void setLocationY(int locationY){
		this.locationY = locationY;
	}

	/**
	 * Returns the X location.
	 * 
	 * @return the X location
	 */

	public int getLocationX(){
		return this.locationX;
	}

	/**
	 * Returns the Y location.
	 * 
	 * @return the Y location
	 */

	public int getLocationY(){
		return this.locationY;
	}

	/**
	 * Returns the id.
	 * 
	 * @return the id
	 */

	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the id to set
	 */

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the class name.
	 * 
	 * @return the class name
	 */

	public String getClassName() {
		return className;
	}

	/**
	 * Sets the class name.
	 * 
	 * @param className the class name to set
	 */

	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Returns the tag name.
	 * 
	 * @return the tag name
	 */

	public String getTagName() {
		return tagName;
	}

	/**
	 * Sets the tag name.
	 * 
	 * @param tagName the tag name to set
	 */

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * Returns the text content.
	 * 
	 * @return the text content
	 */

	public String getText() {
		return text;
	}

	/**
	 * Sets the text content.
	 * 
	 * @param textContent the text content to set
	 */
	
	public void setTextContent(String textContent) {
		this.text = textContent;
	}

	/**
	 * Returns the value for the specified attribute.
	 * 
	 * @return the value for the specified attribute
	 */

	public String getAttribute(String attributeName) {
		if (attributeName != null){
			return this.attributes.get(attributeName);
		}
		
		return null;
	}

	/**
	 * Sets the attributes.
	 * 
	 * @param attributes the attributes to set
	 */
	
	public void setAttributes(Hashtable<String,String> attributes) {
		this.attributes = attributes;
	}

}
