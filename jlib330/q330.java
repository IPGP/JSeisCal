package jlib330;

import java.security.NoSuchAlgorithmException;

public class q330 {
	
    private String serial;
    private String address;
    private String dataport;
    private String authcode;
    
	Data buffers;

	public q330(Data buf) {
		buffers = buf;
	}
	
	public void ParsePacket(byte[] packet) {
		int datalength = Util.Bytes2ToInt(packet, 6);
		int channel;
		int freq;
		int size;
		int previous;
		int key;
		int[] samples = new int[1024];
		byte[] blockette = null;
		
		int indexStamp = Util.DWordToInt(packet, 12);

		for (int i = 16; i < datalength + 12;) {
			if (((packet[i] & 0x80) >> 5) == 0) {
				if ((packet[i] & 0xE0) >> 5 == 0 || (packet[i] & 0xE0) >> 5 == 1) {
					i = i + 4;
				} else if ((packet[i] & 0xE0) >> 5 == 2) {
					i = i + 8;
				} else if ((packet[i] & 0xE0) >> 5 == 3) {
					i = i + 12;
				}
			} else {
				switch (packet[i] & 0xF8) {
				case 0xE8: // 0b11101xxx -> data	
					channel = packet[i] & 0x07;
					freq = packet[i + 1];
					size = Util.Bytes2ToInt(packet, i + 2);
					previous = Util.DWordToInt(packet, i + 4);
					//System.out.println("channel = " + channel + "  -  Freq = " + freq + "  - Size = " + size);
					blockette = new byte[size];
					System.arraycopy(packet, i, blockette, 0, size);
					samples = extractSamples(blockette);
					buffers.addData(indexStamp, channel, freq, samples);
					if (size > 0) {
						i = i + size;
					}
					else {
						System.out.println("Bad Size = "+Integer.toString(size));
						i = datalength+12; // end loop
					}
					break;
					
				case 0xF0:
					channel = packet[i] & 0x07;
					size = Util.Bytes2ToInt(packet, i + 2) & 0x3FF;
					blockette = new byte[size];
					System.arraycopy(packet, i, blockette, 0, size);
					buffers.fillRing(indexStamp, blockette);

					if (size > 0) {
						i = i + size;
					}
					else {
						System.out.println("Bad Size = "+Integer.toString(size));
						i = datalength+12; // end loop
					}
					break;
						
				case 0xF8:
					int type = packet[i] & 0x07;
					switch (type) {
					case 0:
						i += 28;
						break;
					case 1:
						i += 8;
						break;
					case 2:
						i += 12;
						break;
					case 3:
						i += 8;
						break;
						
					}
					
					break;
					

				case 0x88:
				case 0x80:
				case 0xA0:
				case 0xC0:
				case 0xC8:
				case 0xA8:
					i = i + 4;
					break;

				case 0x90:
				case 0xB0:
				case 0xD0:
					i = i + 8;
					break;
				case 0xE0:
					i = i + 8;
					break;

				case 0xB8:
					i = i + 12;
					break;

				case 0x98:
					
					long tempTimeStamp = 0;
					indexStamp = Util.DWordToInt(packet, 12);
					tempTimeStamp = (indexStamp + Util.DWordToInt(packet, i + 4)); // in s
					tempTimeStamp += 946684800; // epoch 01-01-2000
					tempTimeStamp = (tempTimeStamp * 1000000 + Util.DWordToInt(packet, i + 8)) / 1000;
					buffers.setTimeStamp(indexStamp, tempTimeStamp);
					i += 12;
					break;

				default:
					System.out.println("Default case  !!" + (packet[i]));
					i += 10000; //exit
					break;
				}
			}
		}
	}

	public byte[] SetDataAckPacket(byte arg1, byte arg2) {
		// Set Data Aknowledge Packet using last data frame number
		byte[] dtack = new byte[36];
		int index_dtack = 0;
		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;

		dtack[index_dtack++] = 0x0a;
		dtack[index_dtack++] = 0x02;
		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x18;

		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = arg1;
		dtack[index_dtack++] = arg2;

		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;

		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x01;

		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;

		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;

		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;
		dtack[index_dtack++] = (byte) 0x00;

		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;
		dtack[index_dtack++] = 0x00;

		int crc = new CRC32().crc32(dtack, 4, index_dtack - 4, 0);

		byte[] temp = Util.intToDWord(crc);
		dtack[0] = temp[3];
		dtack[1] = temp[2];
		dtack[2] = temp[1];
		dtack[3] = temp[0];

		return dtack;
	}

	public byte[] SetDeregPacket(String serial) {
		byte[] dereg = new byte[20];
		int index_dereg = 0;
		
		byte[] serialb = new byte[serial.length() / 2];
		for (int i = 0; i < serialb.length; i++) {
			serialb[i] = (byte) Integer.parseInt(serial.substring(2 * i, 2 * i + 2), 16);
		}
		
		
		dereg[index_dereg++] = 0x00;
		dereg[index_dereg++] = 0x00;
		dereg[index_dereg++] = 0x00;
		dereg[index_dereg++] = 0x00;

		dereg[index_dereg++] = 0x12;
		dereg[index_dereg++] = 0x02;
		dereg[index_dereg++] = 0x00;
		dereg[index_dereg++] = 0x08;

		dereg[index_dereg++] = 0x00;
		dereg[index_dereg++] = 0x03;
		dereg[index_dereg++] = 0x00;
		dereg[index_dereg++] = 0x00;

		dereg[index_dereg++] = serialb[0];
		dereg[index_dereg++] = serialb[1];
		dereg[index_dereg++] = serialb[2];
		dereg[index_dereg++] = serialb[3];

		dereg[index_dereg++] = serialb[4];
		dereg[index_dereg++] = serialb[5];
		dereg[index_dereg++] = serialb[6];
		dereg[index_dereg++] = serialb[7];

		int crc = new CRC32().crc32(dereg, 4, index_dereg - 4, 0);

		byte[] temp = Util.intToDWord(crc);
		dereg[0] = temp[3];
		dereg[1] = temp[2];
		dereg[2] = temp[1];
		dereg[3] = temp[0];

		return dereg;

	}

	public byte[] SetServerRequestPacket(String serial) {
		byte[] rqsrv = new byte[20];
		
		byte[] serialb = new byte[serial.length() / 2];
		for (int i = 0; i < serialb.length; i++) {
			serialb[i] = (byte) Integer.parseInt(serial.substring(2 * i, 2 * i + 2), 16);
		}
		
		rqsrv[0] = 0x00;
		rqsrv[1] = 0x00;
		rqsrv[2] = 0x00;
		rqsrv[3] = 0x00;

		rqsrv[4] = 0x10;
		rqsrv[5] = 0x02;
		rqsrv[6] = 0x00;
		rqsrv[7] = 0x08;

		rqsrv[8] = 0x00;
		rqsrv[9] = 0x00;
		rqsrv[10] = 0x00;
		rqsrv[11] = 0x00;

		rqsrv[12] = serialb[0];
		rqsrv[13] = serialb[1];
		rqsrv[14] = serialb[2];
		rqsrv[15] = serialb[3];
		rqsrv[16] = serialb[4];
		rqsrv[17] = serialb[5];
		rqsrv[18] = serialb[6];
		rqsrv[19] = serialb[7];

		byte[] temp = Util.intToDWord(new CRC32().crc32(rqsrv, 4, 16, 0));
		rqsrv[0] = temp[3];
		rqsrv[1] = temp[2];
		rqsrv[2] = temp[1];
		rqsrv[3] = temp[0];

		return rqsrv;

	}

	public byte[] SetServerResponsePacket(byte[] rqsrv, byte[] result,
			String authcode) throws NoSuchAlgorithmException {

		byte[] srvrsp = new byte[60];
		srvrsp[0] = 0x00;
		srvrsp[1] = 0x00;
		srvrsp[2] = 0x00;
		srvrsp[3] = 0x00;
		srvrsp[4] = 0x11;
		srvrsp[5] = 0x02;
		srvrsp[6] = 0x00;
		srvrsp[7] = 0x30;
		srvrsp[8] = 0x00;
		srvrsp[9] = 0x01;
		srvrsp[10] = 0x00;
		srvrsp[11] = 0x00;
		// 010000113246FA5B

		// Serial
		System.arraycopy(rqsrv, 12, srvrsp, 12, 8);
		// result from srvch
		System.arraycopy(result, 12, srvrsp, 20, 16);

		int j = 36;
		srvrsp[j++] = (byte) 0x47;
		srvrsp[j++] = (byte) 0xf4;
		srvrsp[j++] = (byte) 0xe7;
		srvrsp[j++] = (byte) 0x65;
		srvrsp[j++] = (byte) 0x0E;
		srvrsp[j++] = (byte) 0xB0;
		srvrsp[j++] = (byte) 0xF5;
		srvrsp[j++] = (byte) 0xE3;

		// challenge + ip + udp
		String str = "";

		for (j = 0; j < 16; j++) {
			int value = srvrsp[20 + j];
			if (value == 0) {
				str = str.concat("00");
			} else {
				if (value < 0)
					value += 256;
				if (value < 16)
					str = str.concat("0");
				str = str.concat(Integer.toHexString(value));
			}
		}

		// auth code
		if (authcode.equals("0")) {
			str = str.concat("0000000000000000");	
		}
		else {
			str = str.concat(authcode);
		}
		
		
		// serial
		for (j = 0; j < 8; j++) {
			int value = srvrsp[12 + j];
			if (value == 0) {
				str = str.concat("00");
			} else {
				if (value < 0)
					value += 256;
				if (value < 16)
					str = str.concat("0");
				str = str.concat(Integer.toHexString(value));
			}
		}

		// random
		for (j = 0; j < 8; j++) {
			int value = srvrsp[36 + j];
			if (value == 0) {
				str = str.concat("00");
			} else {
				if (value < 0)
					value += 256;
				if (value < 16)
					str = str.concat("0");
				str = str.concat(Integer.toHexString(value));
			}
		}

		// MD5 calculation
		java.security.MessageDigest msgDigest = java.security.MessageDigest
				.getInstance("MD5");
		msgDigest.update(str.getBytes());
		byte[] digest = msgDigest.digest();
		
		// Copy challenge response
		System.arraycopy(digest, 0, srvrsp, 44, 16);
		
		// CRC
		byte[] temp = Util.intToDWord(new CRC32().crc32(srvrsp, 4, 56, 0));
		srvrsp[0] = temp[3];
		srvrsp[1] = temp[2];
		srvrsp[2] = temp[1];
		srvrsp[3] = temp[0];

		return srvrsp;
	}

	public byte[] SetRequestMemoryPacket(int offset, int dataport) {
		byte[] rqmem = new byte[20];
		int index_rqmem = 0;

		rqmem[index_rqmem++] = 0x00;
		rqmem[index_rqmem++] = 0x00;
		rqmem[index_rqmem++] = 0x00;
		rqmem[index_rqmem++] = 0x00;

		rqmem[index_rqmem++] = 0x41;
		rqmem[index_rqmem++] = 0x02;
		rqmem[index_rqmem++] = 0x00;
		rqmem[index_rqmem++] = 0x08;

		rqmem[index_rqmem++] = 0x00;
		rqmem[index_rqmem++] = 0x02;
		rqmem[index_rqmem++] = 0x00;
		rqmem[index_rqmem++] = 0x00;

		rqmem[index_rqmem++] = (byte) 0x00;
		rqmem[index_rqmem++] = (byte) 0x00;
		rqmem[index_rqmem++] = (byte) (offset >>> 8);
		rqmem[index_rqmem++] = (byte) (offset & 0x00ff);

		rqmem[index_rqmem++] = (byte) 0x00;
		rqmem[index_rqmem++] = (byte) 0x00;
		rqmem[index_rqmem++] = (byte) 0x00;
		rqmem[index_rqmem++] = (byte) dataport;

		// CRC
		byte[] temp = Util.intToDWord(new CRC32().crc32(rqmem, 4,
				index_rqmem - 4, 0));
		rqmem[0] = temp[3];
		rqmem[1] = temp[2];
		rqmem[2] = temp[1];
		rqmem[3] = temp[0];

		return rqmem;

	}

	public byte[] SetRequestLogPacket(int dataport) {
		byte[] rqlog = new byte[16];
		int index_rqlog = 0;

		rqlog[index_rqlog++] = 0x00;
		rqlog[index_rqlog++] = 0x00;
		rqlog[index_rqlog++] = 0x00;
		rqlog[index_rqlog++] = 0x00;

		rqlog[index_rqlog++] = 0x18;
		rqlog[index_rqlog++] = 0x02;
		rqlog[index_rqlog++] = 0x00;
		rqlog[index_rqlog++] = 0x02;

		rqlog[index_rqlog++] = 0x00;
		rqlog[index_rqlog++] = 0x03;
		rqlog[index_rqlog++] = 0x00;
		rqlog[index_rqlog++] = 0x00;

		rqlog[index_rqlog++] = 0x00;
		rqlog[index_rqlog++] = (byte) (dataport - 1);

		// CRC
		byte[] temp = Util.intToDWord(new CRC32().crc32(rqlog, 4,
				index_rqlog - 4, 0));
		rqlog[0] = temp[3];
		rqlog[1] = temp[2];
		rqlog[2] = temp[1];
		rqlog[3] = temp[0];

		return rqlog;

	}

	public byte[] SetFlushDpPacket(byte[] result) {
		byte[] flushDP = new byte[64];
		int index_flushDP = 0;
		flushDP[index_flushDP++] = 0x00;
		flushDP[index_flushDP++] = 0x00;
		flushDP[index_flushDP++] = 0x00;
		flushDP[index_flushDP++] = 0x00;

		flushDP[index_flushDP++] = 0x17;
		flushDP[index_flushDP++] = 0x02;
		flushDP[index_flushDP++] = 0x00;
		flushDP[index_flushDP++] = 52;

		flushDP[index_flushDP++] = 0x00;
		flushDP[index_flushDP++] = 0x00;
		flushDP[index_flushDP++] = 0x00;
		flushDP[index_flushDP++] = 0x00;

		// Recopy Log contents
		for (index_flushDP = 12; index_flushDP < result.length; index_flushDP++) {
			flushDP[index_flushDP] = result[index_flushDP];
		}

		// set flush bit
		flushDP[15] |= 0x02;
		
		// set time to 0
		flushDP[48] = 0x00; 
		flushDP[49] = 0x00;
		flushDP[50] = 0x00;
		flushDP[51] = 0x00;
		

		
		// CRC
		byte[] temp = Util.intToDWord(new CRC32().crc32(flushDP, 4,
				index_flushDP - 4, 0));
		flushDP[0] = temp[3];
		flushDP[1] = temp[2];
		flushDP[2] = temp[1];
		flushDP[3] = temp[0];

		return flushDP;
	}

	public byte[] SetDataOpenPacket() {
		byte[] dtopen = new byte[12];
		int index_dtopen = 0;

		dtopen[index_dtopen++] = 0x00;
		dtopen[index_dtopen++] = 0x00;
		dtopen[index_dtopen++] = 0x00;
		dtopen[index_dtopen++] = 0x00;

		dtopen[index_dtopen++] = 0x0B;
		dtopen[index_dtopen++] = 0x02;
		dtopen[index_dtopen++] = 0x00;
		dtopen[index_dtopen++] = 0x00;

		dtopen[index_dtopen++] = 0x00;
		dtopen[index_dtopen++] = 0x00;
		dtopen[index_dtopen++] = 0x00;
		dtopen[index_dtopen++] = 0x00;

		// CRC
		byte[] temp = Util.intToDWord(new CRC32().crc32(dtopen, 4,
				index_dtopen - 4, 0));
		dtopen[0] = temp[3];
		dtopen[1] = temp[2];
		dtopen[2] = temp[1];
		dtopen[3] = temp[0];

		return dtopen;

	}

	// return next memory address - 0 end
	public int ParseMemory(byte[] memory) {
		int tokenlength = 0;
		System.out.println("memory length " + memory.length);

		for (int i = 0; i < memory.length;) {
			int temp = 0;
			temp |= memory[i] & 0xFF;
			switch (temp) {
			case 0: // 0b11101xxx -> data
				i++;
				break;
			case 1:
				i += 2;
				break;
			case 2:
				i += 8;
				break;
			case 3:
				i += 3;
				break;
			case 4:
				i += 41;
				break;
			case 5:
				i += 3;
				break;
			case 6:
				i += 17;
				break;
			case 7:
				i += 11;
				break;
			case 8:
				i += 9;
				break;
			case 9:
				i += 3;
				break;
			case 192:
			case 193:
				tokenlength = Util.Bytes2ToInt(memory, i + 1);
				i = i + tokenlength + 1;
				break;
			case 129:
			case 130:
			case 131:
			case 132:
			case 133:
				tokenlength = 0;
				tokenlength |= memory[i + 1] & 0xff;
				i = i + tokenlength + 1;
				break;
			case 128: // 0x80
				tokenlength = 0;
				tokenlength |= memory[i + 1] & 0xff;
				i = i + tokenlength + 1;
				// code to verify channels
				break;
			default:
				i = i + 4;
				break;
			}
		}
		return 1;
	}

	public int[] extractSamples(byte[] bloc) {
		/* get nibbles */
		int offset = Util.Bytes2ToInt(bloc, 8);
		int size = Util.Bytes2ToInt(bloc, 2);
		int nibblesoffset = 10;
		int nibblesbytes = offset - nibblesoffset;
		int nibbles = (size - offset) / 4;
		int n = 0;
		int currNibble = 0;
		int dnib = 0;
		int[] values = new int[1024];
		int valuesoffset = offset;
		int tempInt;
		int tempval = 0;
		int currNum = 0;
		boolean end = false;
		
		int previous = Util.DWordToInt(bloc, 4);
		values[currNum++] = previous;

		for (int i = nibblesoffset; (i < (nibblesoffset + nibblesbytes))
				&& (n < nibbles); i++) {
			for (int j = 0; (j < 4) && (n < nibbles); j++) {
				currNibble = (bloc[i] >> (6 - j * 2)) & 0x03;
				switch (currNibble) {
				case 0:
					end = true;
					break;
				case 1:
					n++;
					tempval = bloc[valuesoffset++];
					if (tempval > 127)
						tempval -= 256;
					values[currNum] = values[currNum - 1] + tempval;
					currNum++;
					tempval = bloc[valuesoffset++];
					if (tempval > 127)
						tempval -= 256;
					values[currNum] = values[currNum - 1] + tempval;
					currNum++;
					tempval = bloc[valuesoffset++];
					if (tempval > 127)
						tempval -= 256;
					values[currNum] = values[currNum - 1] + tempval;
					currNum++;
					tempval = bloc[valuesoffset++];
					if (tempval > 127)
						tempval -= 256;
					values[currNum] = values[currNum - 1] + tempval;
					currNum++;
					break;
				case 2:
					n++;
					tempInt = Util.DWordToInt(bloc, valuesoffset);
					dnib = (tempInt >> 30) & 0x03;
					switch (dnib) {
					case 1:
						tempval = tempInt & 0x3FFFFFFF;
						//tempval = ((tempInt << 2) >>> 2);
						if (tempval >  0x1fffffff)
							tempval -= 0x40000000;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						break;
					case 2:
						tempval = ((tempInt << 2) >>> 17);
						if (tempval > 16383)
							tempval -= 32768;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 17) >>> 17);
						if (tempval > 16383)
							tempval -= 32768;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						break;
					case 3:
						tempval = ((tempInt << 2) >>> 22) & 0x03ff;
						if (tempval > 511)
							tempval -= 1024;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 12) >>> 22) & 0x03ff;
						if (tempval > 511)
							tempval -= 1024;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 22) >>> 22) & 0x03ff;
						if (tempval > 511)
							tempval -= 1024;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						break;
					default:
						break;
					}
					break;
				case 3:
					n++;
					tempInt = Util.DWordToInt(bloc, valuesoffset);
					dnib = (tempInt >> 30) & 0x03;
					switch (dnib) {
					case 0:
						tempval = ((tempInt << 2) >>> 26) & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 8) >>> 26) & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 14) >>> 26) & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 20) >>> 26) & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 26) >>> 26) & 0x3f;
						if (tempval > 31)
							tempval -= 64;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						break;
					case 1:
						tempval = ((tempInt << 2) >>> 27) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 7) >>> 27) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 12) >>> 27) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 17) >>> 27) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 22) >>> 27) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 27) >>> 27) & 0x1f;
						if (tempval > 15)
							tempval -= 32;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						break;
					case 2:
						tempval = ((tempInt << 4) >>> 28) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 8) >>> 28) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 12) >>> 28) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 16) >>> 28) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 20) >>> 28) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 24) >>> 28) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						tempval = ((tempInt << 28) >>> 28) & 0x0f;
						if (tempval > 7)
							tempval -= 16;
						values[currNum] = values[currNum - 1] + tempval;
						currNum++;
						valuesoffset += 4;
						break;
					default:
						break;
					}
				}
			}
		}
		return values;
	}
	
	
	

	
}
