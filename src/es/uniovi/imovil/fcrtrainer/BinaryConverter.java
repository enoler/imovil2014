package es.uniovi.imovil.fcrtrainer;

import java.util.Random;

public class BinaryConverter {
	private static final int MAX_RANDOM_NUMBER = 128;
	private static final int MIN_RANDOM_NUMBER = 1;
	
	
	
	public String deleteStartingZeroesFromBinaryInput(String binaryText){
		String c = "" + binaryText.charAt(0); 
		int i=0;
		int lastPosition = 0;
		while(c.equals("0")){
			c = "" + binaryText.charAt(i); 
			if(c.equals("1")){
				lastPosition = i;
			}
			i++;
		}
		//System.out.println("substring: " + binaryText.substring(lastPosition));
		//Now substring and return	
		return binaryText.substring(lastPosition);
	}
	// Binary Modul
	public int createRandomNumber() {
		Random r = new Random();
		return r.nextInt(MAX_RANDOM_NUMBER - MIN_RANDOM_NUMBER) + MIN_RANDOM_NUMBER;
	}

	public String convertDecimalToBinary(int decimalNumber) {
		return Integer.toBinaryString(decimalNumber);
	}
	
	public String convertDecimalToBinary(String decimalNumber) {
		int number = Integer.parseInt(decimalNumber);
		return convertDecimalToBinary(number);
	}
	
	public int convertBinaryToInteger(String binary){
		return Integer.parseInt(binary, 2);
	}
}
