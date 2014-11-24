package es.collectserv.test.concurrent;


public class PhoneNumberGenerator {
	private int mPhoneNumberCont;
	private final int mInitPhoneVal = 610000000;
	
	public PhoneNumberGenerator(){
		mPhoneNumberCont = mInitPhoneVal;
	}

	public void resetValue(){
		mPhoneNumberCont = mInitPhoneVal;
	}
	
	public String generate_phoneNumber(){
		mPhoneNumberCont++;
		return Integer.toString(mPhoneNumberCont);
	}
	
}
