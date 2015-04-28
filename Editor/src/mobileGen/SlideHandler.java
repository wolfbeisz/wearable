package mobileGen;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class SlideHandler {

	public Slide[] insertNewSlide(Slide[] ar, int slideNr, int slideType){
		if(ar!=null && slideNr<ar.length){
			int slidesBeforeInsert = 0;

			for(int i = 0; i<ar.length; i++){
				if(ar[i]!=null){
					slidesBeforeInsert++;
				}
			}


			for(int i = slidesBeforeInsert; i>slideNr; i--){
				ar[i] = ar[i-1];
				ar[i].setSlideNr(ar[i].getSlideNr()+1);
			}

			ar[slideNr] = new Slide(slideNr, slideType);
		}
		return ar;

	}

	public Slide getSlideforSlideNr(Slide[] ar, int slideNr){
		Slide s = null;
		if(ar!=null){
			for(int i = 0; i<ar.length; i++){
				if(ar[i]!=null){
					if(ar[i].getSlideNr() == slideNr)
						s = ar[i];
				}
			}
		}
		return s;
	}

	public Slide[] removeSlide(Slide[] ar, int slideNr){
		if(ar!=null && slideNr<ar.length){
			int slidesBeforeRemove = 0;

			for(int i = 0; i<ar.length; i++){
				if(ar[i]!=null){
					slidesBeforeRemove++;
				}
			}
			ar[slidesBeforeRemove] = null;
			for(int i = slideNr; i<slidesBeforeRemove; i++){
				ar[i] = ar[i+1];
				if(ar[i]!=null){
					ar[i].setSlideNr(ar[i].getSlideNr()-1);
				}	
			}
		}
		return ar;
	}

	public boolean checkEdgesForConsistency(Slide[] ar){
		boolean ret = true;
		if(ar!=null){
			for(int i = 0; i<ar.length; i++){
				if(ar[i]!=null){
					if(ar[i].getAnswer1Successor()>-1 && getSlideforSlideNr(ar,ar[i].getAnswer1Successor())==null){
						ret = false;
					}
					if(ar[i].getAnswer2Successor()>-1 && getSlideforSlideNr(ar,ar[i].getAnswer2Successor())==null){
						ret = false;
					}
					if(ar[i].getAnswer3Successor()>-1 && getSlideforSlideNr(ar,ar[i].getAnswer3Successor())==null){
						ret = false;
					}
					if(ar[i].getAnswer4Successor()>-1 && getSlideforSlideNr(ar,ar[i].getAnswer4Successor())==null){
						ret = false;
					}
				}
			}
		}
		return ret;
	}

	public Slide[] setCompanyImage(Slide[] ar, Image img){
		if(ar!=null){
			for(int i = 0; i<ar.length; i++){
				if(ar[i]!=null){
					ar[i].setCompanyImage((BufferedImage)img);
				}	
			}
		}
		return ar;
	}

	public Slide getSlideForNumber(Slide[] ar, int slideNr){
		Slide ret = null;
		if(ar!=null && slideNr<ar.length){
			for(int i = 0; i<ar.length; i++){
				if(ar[i]!=null && ar[i].getSlideNr()==slideNr){
					ret = ar[i];
				}
			}
		}
		return ret;
	}

	public int getSlideCount(Slide[] ar){
		int count = 0;
		if(ar!=null){
			for(int i=0; i<ar.length; i++){
				if(ar[i]!=null){
					count++;
				}
			}
		}
		return count;
	}
}
