package jlib330;

public class Util {
   public static byte[] intToDWord(int i) {
		byte[] dword = new byte[4];
		dword[0] = (byte) (i & 0x00FF);
		dword[1] = (byte) ((i >> 8) & 0x000000FF);
		dword[2] = (byte) ((i >> 16) & 0x000000FF);
		dword[3] = (byte) ((i >> 24) & 0x000000FF);
		return dword;
	}
  
   public static int DWordToInt(byte[]buf,int ind) {
		int result = 0;
		result |= buf[ind] & 0xFF;
		result <<= 8;
		result |= buf[ind+1] & 0xFF;
		result <<= 8;
		result |= buf[ind+2] & 0xFF;
		result <<= 8;
		result |= buf[ind+3] & 0xFF;
		
		return result;
	}
  
   public static int Bytes2ToInt(byte[]buf,int ind) {
		int result = 0;
		result |= buf[ind] & 0xFF;
		result <<= 8;
		result |= buf[ind+1] & 0xFF;		
		return result;
	}  


}
