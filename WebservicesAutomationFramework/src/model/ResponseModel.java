package model;

public class ResponseModel {

	/**
	 * @param args
	 */
	
	String xmlString="";
	String jsonString="";
	String htmlString="";
	String plainText="";
	public String getXmlString() {
		return xmlString;
	}
	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	public String getHtmlString() {
		return htmlString;
	}
	public void setHtmlString(String htmlString) {
		this.htmlString = htmlString;
	}
	public String getPlainText() {
		return plainText;
	}
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
	public ResponseModel(String xmlString, String jsonString,
			String htmlString, String plainText) {
		super();
		this.xmlString = xmlString;
		this.jsonString = jsonString;
		this.htmlString = htmlString;
		this.plainText = plainText;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
