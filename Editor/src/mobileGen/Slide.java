package mobileGen;
import java.awt.Image;  
import java.awt.image.BufferedImage;


public class Slide {
	private int slideNr;
	private int slideType;
	private String caption;
	private String imageDescription;
	private String answer1;
	private int answer1Successor;
	private String answer2;
	private int answer2Successor;
	private String answer3;
	private int answer3Successor;
	private String answer4;
	private int answer4Successor;
	private String previous;
	private String next;
	private BufferedImage companyImage = null;
	private BufferedImage img = null;
	
	public Slide(int slideNr, int slideType){
		this.slideNr = slideNr;
		this.slideType = slideType;
		this.caption = "Caption";
		this.imageDescription = "";
		this.answer1 = "";
		this.answer1Successor = -1;
		this.answer2 = "";
		this.answer2Successor = -1;
		this.answer3 = "";
		this.answer3Successor = -1;
		this.answer4 = "";
		this.answer4Successor = -1;
		this.previous = "Back";
		this.next = "Next";
	}
	public String getImageDescription() {
		return imageDescription;
	}
	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}
	public int getSlideNr() {
		return slideNr;
	}
	public void setSlideNr(int slideNr) {
		this.slideNr = slideNr;
	}
	public int getSlideType() {
		return slideType;
	}
	public void setSlideType(int slideType) {
		this.slideType = slideType;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getAnswer1() {
		return answer1;
	}
	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}
	public int getAnswer1Successor() {
		return answer1Successor;
	}
	public void setAnswer1Successor(int answer1Successor) {
		this.answer1Successor = answer1Successor;
	}
	public String getAnswer2() {
		return answer2;
	}
	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}
	public int getAnswer2Successor() {
		return answer2Successor;
	}
	public void setAnswer2Successor(int answer2Successor) {
		this.answer2Successor = answer2Successor;
	}
	public String getAnswer3() {
		return answer3;
	}
	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}
	public int getAnswer3Successor() {
		return answer3Successor;
	}
	public void setAnswer3Successor(int answer3Successor) {
		this.answer3Successor = answer3Successor;
	}
	public String getAnswer4() {
		return answer4;
	}
	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}
	public int getAnswer4Successor() {
		return answer4Successor;
	}
	public void setAnswer4Successor(int answer4Successor) {
		this.answer4Successor = answer4Successor;
	}
	public String getPrevious() {
		return previous;
	}
	public void setPrevious(String previous) {
		this.previous = previous;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public BufferedImage getImg() {
		return img;
	}
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	public BufferedImage getCompanyImage() {
		return companyImage;
	}
	public void setCompanyImage(BufferedImage companyImage) {
		this.companyImage = companyImage;
	}

}
