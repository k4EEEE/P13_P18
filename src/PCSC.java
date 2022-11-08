import java.nio.ByteBuffer;
import java.util.List;
import java.util.Scanner;

import javax.smartcardio.*;
import javax.swing.JComboBox;

//Access restriction: The type 'Application' is not API (restriction on required library rt.jar)
//The solution is to change the access restrictions.
//Go to the properties of your Java project,
//i.e. by selecting "Properties" from the context menu of the project in the "Package Explorer".
//Go to "Java Build Path", tab "Libraries".
//Expand the library entry
//select
//"Access rules",
//"Edit..." and
//"Add..." a "Resolution: Accessible" with a corresponding rule pattern. For me that was "javax/smartcardio/**", for you it might instead be "com/apple/eawt/**".

public class PCSC {

	ResponseAPDU answer; 
	static CardChannel channel;
	
	public static void main(String[] args) {
		
		boolean bracelet = false;
		boolean iphone = false;
		boolean visa = false;
		boolean mc = false;
		boolean mada_only = false;
		
		// TODO Auto-generated method stub
		  try {
			   // Display the list of terminals
			   TerminalFactory factory = TerminalFactory.getDefault();
			   List<CardTerminal> terminals = factory.terminals().list();
			   
			   System.out.println("Terminals Connected " );
			   
			   if(terminals.size() > 0) {
				   		for(int i=0; i< terminals.size();i++)
				   				System.out.println(i+1+"."+"Terminal "+": " + terminals.get(i).toString());
			   }
					
			   // Use the first terminal.
			   CardTerminal terminal = terminals.get(2); //set the value in get() to the proper reader type

			   // Connect with the card
			   Card card = terminal.connect("*");
			   System.out.println("card: " + card);
			   channel = card.getBasicChannel();

			   // 1.ATR  => ANSWER TO RESET
			   ATR atr ;
			   if (terminal.isCardPresent()) {
			            System.out.println("Card present!");
			            atr = card.getATR(); //reset card
			            System.out.println("ATR: "+bytesToHex(atr.getBytes()));
			            
			            if( bytesToHex(atr.getBytes()).substring(0, 17) == "3B8F80018031E06B04") // Bracelt
							   bracelet = true;
			            
		        }
			   
			   
			   
			   // 2.Read Contactless Main directory 2PAY.SYS.DDF01 PPSE
			   byte[] PPSE = {
					   (byte) 0x00 /*Class*/, 
					   (byte) 0xA4 /*Inst A4 Directory File*/, 
					   (byte) 0x04 /* P1 Read File by name*/, 
					   (byte)  0x00 /* P2 00 */, 
					   (byte) 0x0E  /* Lc */,
					   (byte)  0x32,(byte)  0x50,(byte)  0x41, (byte) 0x59, (byte) 0x2E, (byte) 0x53, (byte) 0x59, (byte) 0x53, (byte) 0x2E, (byte) 0x44, (byte) 0x44, (byte) 0x46, (byte) 0x30, (byte) 0x31, //2PAY.SYS.DDF01 PPSE
					   (byte)  0x00
					   };
			   
			   ResponseAPDU answer = channel.transmit(new CommandAPDU(ByteBuffer.wrap(PPSE)));
			   System.out.println("Read PPSE answer: " + bytesToHex(answer.getBytes()));

			   
			   if(bytesToHex(answer.getBytes()).indexOf("A0000000041010") > 0) {
				   mc = true;
				   MCHIP_Flow(answer, channel);
			   } 
			   if(bytesToHex(answer.getBytes()).indexOf("A0000000031010") > 0) {
				   visa = true;
				   VISA_Flow(answer, channel);
			   }
				   
			   if ( !visa && !mc && bytesToHex(answer.getBytes()).indexOf("A0000002281010") > 0) {
				   mada_only = true;
				   MCHIP_Flow(answer, channel);
				   
			   }
			   
 		    

			   // 7. Disconnect the card
			   card.disconnect(false);
	 		   System.out.println("Card Disconnected");
			  } catch(Exception e) {
			   System.out.println("Error: " + e.toString());
			  }
		
	}
	
	public static void VISA_Flow(ResponseAPDU ansr,  CardChannel chanl) {
		
		try {
		   // 3. Read MADA APP
		   byte[] MADAaid =  {
				   (byte) 0x00 /* Class*/, 
				   (byte) 0xA4 /* Inst Read File*/, 
				   (byte) 0x04 /* P1 Read By name */, 
				   (byte) 0x00 /* P2 */, 
				   (byte)0x07 /* Lc */,
				   (byte)0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x28, (byte) 0x10, (byte) 0x10, // MADA App
				   (byte)0x00
		   };
		   ansr = chanl.transmit(new CommandAPDU( ByteBuffer.wrap(MADAaid)));
		   System.out.println("MADA answer: " + bytesToHex(ansr.getBytes()));
		   
		   // 4. Read VISA APP
		   byte[] VISAaid =  {
				   (byte) 0x00 /* Class*/, 
				   (byte) 0xA4 /* Inst Read File*/, 
				   (byte) 0x04 /* P1 Read By name */, 
				   (byte) 0x00 /* P2 */, 
				   (byte)0x07 /* Lc */, 
				   (byte)0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x10, (byte) 0x10, // Visa App
				   (byte)0x00
				   };
		   ansr = channel.transmit(new CommandAPDU( ByteBuffer.wrap(VISAaid)));
		   System.out.println("VISA answer: " + bytesToHex(ansr.getBytes()));

		   // 5. Get Processing options NFC APPLE PAY VISA
			byte[] GPOIPHONEVISA = {
					(byte) 0x80 /* Class */, 
					(byte) 0xA8 /* INST Get Processing Options*/, 
					(byte) 0x00 /* P1 */, 
					(byte) 0x00 /* P2 */,  
					(byte) 0x37  /* Lc excluding Le*/,
					(byte) 0x83, (byte) 0x35, // Data Object List Tag, list of PDOL as requested by the card in the previous response without tag or length
					(byte) 0x36, (byte) 0xA0, (byte) 0x40, (byte) 0x00, //  Terminal Transaction Qualifiers
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00,  // Amount Authorized
                    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,  // Amount Other
					(byte) 0x06, (byte) 0x82 /* Currency Code */, 						                               //term country code
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,  	           //TVR
					(byte) 0x06, (byte) 0x82  /* Country Code */, 						                               //txn Currency
					(byte) 0x22, (byte) 0x11, (byte) 0x06 /* Txn Date YYMMDD */,					                       //TXN date
                    (byte) 0x00   /* Txn Type purchase */,								                                    //TXN Type
					(byte) 0x5B, (byte) 0x2C, (byte) 0x46, (byte) 0x3B /* Unpredictable number */,                            // Unpredictable Number 
					(byte) 0x56, (byte) 0x69, (byte) 0x73, (byte) 0x61, (byte) 0x20, (byte) 0x44, (byte) 0x45, (byte) 0x42, (byte) 0x49, (byte) 0x54, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // merchan name and location
					,(byte) 0x00    /* Le */
					};		   
			
			// IPHONE VISA  GPO
			System.out.println("GET Processing Options VISA");
			ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(GPOIPHONEVISA)));     
			System.out.println("GET Processing Options :"+bytesToHex(ansr.getBytes()));

			// 6. ññRead Record 01 SFI 1
 		    byte[] READRECORD_11 = {
 		    		(byte) 0x00 /* Class 00*/, 
 		    		(byte) 0xB2 /* Read Record */, 
 		    		(byte) 0x01 /*Record # 1*/, 
 		    		(byte) 0x0C /*SFI 3*/ , 
 		    		(byte) 0x00 /* Le */
 		    		};
			ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(READRECORD_11)));     
 		    System.out.println("READRECORD_11 answer: " + bytesToHex(ansr.getBytes()));

 		    
			   // 6. ññRead Record 01 SFI 2
 		    byte[] READRECORD_21 = {
 		    		(byte) 0x00 /* Class 00*/, 
 		    		(byte) 0xB2 /* Read Record */, 
 		    		(byte) 0x01 /*Record # 1*/, 
 		    		(byte) 0x14 /*SFI 3*/ , 
 		    		(byte) 0x00 /* Le */
 		    		};
			ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(READRECORD_21)));     
 		    System.out.println("READRECORD_21 answer: " + bytesToHex(ansr.getBytes()));
 		    
		   // 6. ññRead Record 01 SFI 3
 		    byte[] READRECORD_31 = {
 		    		(byte) 0x00 /* Class 00*/, 
 		    		(byte) 0xB2 /* Read Record */, 
 		    		(byte) 0x01 /*Record # 1*/, 
 		    		(byte) 0x1C /*SFI 3*/ , 
 		    		(byte) 0x00 /* Le */
 		    		};
			ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(READRECORD_31)));     
 		    System.out.println("READRECORD_31 answer: " + bytesToHex(ansr.getBytes()));			
		} catch (CardException e) {
			// TODO Auto-generated catch block
			   System.out.println("Error: " + e.toString());;
		}
	
	}
	
	public static void MCHIP_Flow(ResponseAPDU ansr, CardChannel chanl) {

		   try {
			   // 3. Read MADA APP
			   byte[] MADAaid =  {
					   (byte) 0x00 /* Class*/, 
					   (byte) 0xA4 /* Inst Read File*/, 
					   (byte) 0x04 /* P1 Read By name */, 
					   (byte) 0x00 /* P2 */, 
					   (byte)0x07 /* Lc */,
					   (byte)0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x28, (byte) 0x10, (byte) 0x10, // MADA App
					   (byte)0x00
			   };
			   	ansr = chanl.transmit(new CommandAPDU( ByteBuffer.wrap(MADAaid)));
				System.out.println("MADA answer: " + bytesToHex(ansr.getBytes()));
				
				   // 3. Read MADA GPO
				   byte[] MADAGPO =  {
						   (byte) 0x80 /* Class*/, 
						   (byte) 0xA8 /* Inst Read File*/, 
						   (byte) 0x00 /* P1  */, 
						   (byte) 0x00 /* P2 */, 
						   (byte)0x07 /* Lc */,
						   (byte)0x83, (byte) 0x05, 
						   //(byte) 0x00, (byte) 0x00, 
						   (byte) 0xF0, (byte) 0x00, (byte) 0xF0, (byte) 0xA0, (byte) 0x01,
						   (byte)0x00
				   };
			   	ansr = chanl.transmit(new CommandAPDU( ByteBuffer.wrap(MADAGPO)));
				System.out.println("MADA GPO: " + bytesToHex(ansr.getBytes()));

				byte[] Read_SFI2_REC1 = { (byte) 0x00,	(byte) 0xB2, (byte) 0x01, (byte) 0x14, (byte) 0x00}; //READ SFI 2 Rec 1
				ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(Read_SFI2_REC1 )));     
				System.out.println("READRECORD_21 answer: " + bytesToHex(ansr.getBytes()));

				byte[] Read_SFI3_REC1 = { (byte) 0x00,	(byte) 0xB2, (byte) 0x01, (byte) 0x1C, (byte) 0x00}; //READ SFI 3 Rec 1
				ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(Read_SFI3_REC1 )));     
				System.out.println("READRECORD_31 answer: " + bytesToHex(ansr.getBytes()));

				byte[] Read_SFI3_REC2 = { (byte) 0x00,	(byte) 0xB2, (byte) 0x02, (byte) 0x1C, (byte) 0x00}; //READ SFI 3 Rec 2
				ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(Read_SFI3_REC2 )));     
				System.out.println("READRECORD_32 answer: " + bytesToHex(ansr.getBytes()));

				byte[] Read_SFI4_REC1 = { (byte) 0x00,	(byte) 0xB2, (byte) 0x01, (byte) 0x24, (byte) 0x00}; //READ SFI 4 Rec 1
				ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(Read_SFI4_REC1 )));     
				System.out.println("READRECORD_41 answer: " + bytesToHex(ansr.getBytes()));

				byte[] Read_SFI4_REC2 = { (byte) 0x00,	(byte) 0xB2, (byte) 0x02, (byte) 0x24, (byte) 0x00}; //READ SFI 4 Rec 2
				ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(Read_SFI4_REC2 )));     
				System.out.println("READRECORD_42 answer: " + bytesToHex(ansr.getBytes()));

		 		    //Generate Challenge
	  		 		// byte[] GEN_CHALLENGE = {
	  		 		//		 	(byte) 0x00,									//CLA 
	  		 		//		 	(byte) 0x84,									//INST
	  		 		//		 	(byte) 0x00,									//ARQC
	  		 		//		 	(byte) 0x00,           							//P2
	  		 		//		 	(byte) 0x08									//Lc
	  		 		 //};
	  		 		//ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(GEN_CHALLENGE)));     
			 		//System.out.println("Generate CHallenge answer: " + bytesToHex(ansr.getBytes()));
			 		
 		    //Generate AC
		 		 byte[] GEN_AC = {
		 				 	(byte) 0x80,									//CLA 
		 				 	(byte) 0xAE,									//INST
		 				 	(byte) 0x80,									//ARQC
		 				 	(byte) 0x00,           							//P2
		 				 	(byte) 0x42,									//Lc
		/*9F02 06*/ 		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x00,	//Auth Amt
		/*9F03 06*/ 		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,	//Other Amt
		/*9F1A 02*/			(byte) 0x06, (byte) 0x82, 							//Terminal Country Code
		/*95   05*/			(byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0x80, (byte) 0x00,		//TVR
		/*5F2A 02*/			(byte) 0x06, (byte) 0x82, 							//Transaction Currency Code
		/*9A   03*/			(byte) 0x22, (byte) 0x11, (byte) 0x08,						//Transaction Date
		/*9C   01*/			(byte) 0x00,									//Transaction Type
		/*9F37 *04*/		(byte) 0x33, (byte) 0x57, (byte) 0xA3, (byte) 0x0B,				//Unpredictable Number terminal
		/*9F35 01*/         (byte) 0x22, 									//Terminal type- Attended, offline only. Operated by Merchant//
		/*9F45 02*/			(byte) 0x00, (byte) 0x00, /*};*/							//Data Authentication Code
		//byte[] c = new byte[GEN_AC.length + ansr.getBytes().length - 2];
		//System.arraycopy(GEN_AC, 0, c, 0, GEN_AC.length);
		//System.arraycopy(ansr.getBytes(), 0, c, GEN_AC.length, ansr.getBytes().length-2); 		 
				 
		/*9F4C *08*/		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, //ICC Dynamic Number returned from read record apdu	
		//byte[] Trailer = {
		/*9F34 *03*/		(byte) 0x1F, (byte) 0x03, (byte) 0x02,						//CVM result as viewed by terminal
		/*9F21 03*/			(byte) 0x02, (byte) 0x02, (byte) 0x02,						//Transaction Time
		/*9F7C 14*/		    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, //Customer Exclusive Data
							(byte) 0x00
		 		 };
		
		//byte[] d = new byte[c.length + Trailer.length];
		//System.arraycopy(c, 0, d, 0, c.length);
		//System.arraycopy(Trailer, 0, d, c.length, Trailer.length); 		 

		System.out.println("GEN AC COMMAND: "+ bytesToHex((GEN_AC)));
 		ansr = channel.transmit(new CommandAPDU(ByteBuffer.wrap(GEN_AC)));     
 		System.out.println("Generate AC answer: " + bytesToHex(ansr.getBytes()));
	 	String ARQC = bytesToHex(ansr.getBytes());	
		System.out.println("ARQC : " + ARQC.substring(ARQC.indexOf("9F26"), ARQC.indexOf("9F26")+22));

		   } catch (CardException e) {
			// TODO Auto-generated catch block
			   System.out.println("Error: " + e.toString());
		}
		   
	}


	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static String bytesToHex(byte bytes) {
	    char hexChars ;
	    //for (int j = 0; j < bytes.length; j++) {
	        int v = bytes & 0xFF;
	        hexChars = HEX_ARRAY[v >>> 4];
	        hexChars = HEX_ARRAY[v & 0x0F];
	    //}
	    return new String(""+hexChars);
	}
}

