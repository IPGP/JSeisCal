package jlib330;
// CRC32 calculation.
// This software is in the public domain.
//

import java.math.*;

/**
 * <P> Calculates the CRC32 - 32 bit Cyclical Redundancy Check
 * <P> This check is used in numerous systems to verify the integrity
 * of information.  It's also used as a hashing function.  Unlike a regular
 * checksum, it's sensitive to the order of the characters.
 * It produces a 32 bit (Java <CODE>int</CODE>.
 * <P>
 * This Java programme was translated from a C version I had written.
 * <P> This software is in the public domain.
 *
 * <P>
 * When calculating the CRC32 over a number of strings or byte arrays
 * the previously calculated CRC is passed to the next call.  In this
 * way the CRC is built up over a number of items, including a mix of
 * strings and byte arrays.
 * <P>
 * <PRE>
 * CRC32 crc = new CRC32();
 * int crcCalc = crc.crc32("Hello World");
 * crcCalc = crc.crc32("How are you?", crcCalc);
 * crcCalc = crc.crc32("I'm feeling really good, how about you?", crcCalc);
 * </PRE>
 * The line <CODE>int crcCalc = crc.crc32("Hello World");</CODE> is equivalent
 * to <CODE>int crcCalc = crc.crc32("Hello World", -1);</CODE>.
 * When starting a new CRC calculation the "previous crc" is set to
 * 0xFFFFFFFF (or -1).
 * <P>
 * The table only needs to be built once.  You may use it to generate
 * many CRC's.
 * <CODE>
 *
 * @author Michael Lecuyer
 *
 * @version 1.1 August 11, 1998
 *
 */
public class CRC32
{
   int CRCTable[];   // CRC Lookup table

   /**
   * Tests CRC32.
   * <BR>Eg: <SAMP> java CRC32 "Howdy, I'm A Cowboy"
   *
   * @param args the string used to calculate the CRC32
   */
   public static void main(String args[])
   {
      if (args.length == 0)
      {
         System.out.println("Usage CRC32 [string to calculate CRC32]");
         System.out.println("Example : 0 0 0 0 3C 02 00 08 00 00 00 00 00 02 00 00 00 00 00 02");
         System.out.println("Leave the 4 first bytes at 0");
         System.exit(1);
      }

      byte[] packet = new byte[args.length];
      
      for (int i=0;i<args.length;i++) {
    	  packet[i] = (byte)(Integer.parseInt(args[i],16)&0xff);
    	  //System.out.println(packet[i]);
      }
      int l = args.length - 4;
      int crc;
      crc = new CRC32().crc32(packet,4,l,0);
      byte[] temp = Util.intToDWord(crc);
      
      
      System.out.println("CRC is " + (Integer.toHexString(temp[3]&0xff))+" "+Integer.toHexString(temp[2]&0xff)+" "+Integer.toHexString(temp[1]&0xff)+" "+Integer.toHexString(temp[0]&0xff) );
   }

   private int crc;  // currently calculated crc (used in conversion to byte array)

   /**
   * Constructor constructs the lookup table.
   * 
   */
   CRC32()
   {
      buildCRCTable();     
   }

   /**
    * Just build a plain old fashioned table based on good, old fashioned
    * values like the CRC32_POLYNOMIAL.  The table is of a fixed size.
    */
   private void buildCRCTable()
   {
      //final int CRC32_POLYNOMIAL = 0xEDB88320;
	  final int CRC32_POLYNOMIAL = 0x56070368; //1443300200
      

      short count, bits;
      int tdata, accum;

      CRCTable = new int[256];

      for (count = 0; count <= 255; count++)
      {
         tdata = (int)count << 24;
         accum = 0;
         for (bits = 1; bits <= 8; bits++) {
        	if ((tdata ^ accum) < 0)
               accum = (accum << 1) ^ CRC32_POLYNOMIAL;
            else
               accum = (accum << 1);
         tdata = tdata << 1;
         }
         
         CRCTable[count] = accum;
      }
      
      
     /* 
        integer count, bits ;
  		longint tdata, accum ;
  		
        for (count = 0 ; count <= 255 ; count++)
    	    begin
    	      tdata = (longint)count shl 24 ;
    	      accum = 0 ;
    	      for (bits = 1 ; bits <= 8 ; bits++)
    	        begin
    	          if ((tdata xor accum) < 0)
    	            then
    	              accum = (accum shl 1) xor CRC_POLYNOMIAL ;
    	            else
    	              accum = (accum shl 1) ;
    	          tdata = tdata shl 1 ;
    	        end
    	      (*crctable)[count] = accum ;
    	    end
      */
      
      
   }

   /**
    * Convenience mithod for generating a CRC from a single <CODE>String</CODE>.
    *
    * @param buffer string to generate the CRC32 
    *
    * @return 32 bit CRC
    */
   public int crc32(String buffer)
   {
      return crc32(buffer, 0xFFFFFFFF);
   }
   
   /**
    * Convenience method for generating a CRC from a <CODE>byte</CODE> array.
    *
    * @param buffer byte array to generate the CRC32 
    *
    * @return 32 bit CRC
    */
   public int crc32(byte buffer[])
   {
      return crc32(buffer, 0xFFFFFFFF);
   }
   
   /**
    * Convenience method for generating a CRC from a series of <CODE>String</CODE>'s.
    *
    * @param buffer string to generate the CRC32 
    * @param crc previously generated CRC32.
    *
    * @return 32 bit CRC
    */
   public int crc32(String buffer, int crc)
   {
      return crc32(buffer.getBytes(), crc);
   }

   /**
    * Convenience method for generating a CRC from a series of <CODE>byte</CODE> arrays.
    *
    * @param buffer byte array to generate the CRC32 
    * @param crc previously generated CRC32.
    *
    * @return 32 bit CRC
    */
   public int crc32(byte buffer[], int crc)
   {
      return crc32(buffer, 0, buffer.length, crc);
   }

   /**
    * General CRC generation function.
    *
    * @param buffer byte array to generate the CRC32 
    * @param start byte start position 
    * @param count number of byte's to include in CRC calculation 
    * @param crc previously generated CRC32.
    *
    * @return 32 bit CRC
    */
   public int crc32(byte buffer[], int start, int count, int lastcrc)
   {
      long temp1=0;
      int i = start;

      crc = lastcrc;
      

      while (count-- != 0)
      {
         temp1 = (int)((crc >>> 24) ^ buffer[i++] & 0xff);
         //System.out.println(temp1);
         crc = (crc << 8) ^ CRCTable[(int)temp1];
      }
      
      
      /*longint gcrccalc (crc_table_type *crctable, pbyte p, longint len)
      begin
        longint crc ;
        integer temp ;

        crc = 0 ;
        while (len > 0)
          begin
            temp = ((crc shr 24) xor *p++) and 255 ;
            crc = (crc shl 8) xor (*crctable)[temp] ;
            dec(len) ;
          end
        return crc ;
      end*/

      
      return crc;
   }
}
