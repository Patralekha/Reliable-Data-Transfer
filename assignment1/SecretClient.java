import java.net.*;
import java.io.*;
import java.util.*;
 
public class SecretClient {
 
	public static void main(String[] args) {
	 
	    DatagramSocket cs = null;

		try {
			cs = new DatagramSocket();

			byte[] rd, sd;
			String GREETING = "REQUEST HELLO\n";
			String reply,endsignal="";
            int nextconsignment=0;
			DatagramPacket sp,rp;
			boolean end = false;

			while(!end)
			{   	  
				// send Greeting      
			    sd=GREETING.getBytes();	 
			    sp=new DatagramPacket(sd,sd.length, 
									InetAddress.getByName(args[0]),
  									Integer.parseInt(args[1]));	 
				cs.send(sp);	
				System.out.println("sent greeting/acknowledgement");

				// get next consignment
				rd=new byte[512];
				rp=new DatagramPacket(rd,rd.length); 
			    cs.receive(rp);	

				// print SECRET
				reply=new String(rp.getData());	 
                if(reply.indexOf("END")!=-1)
                   { 
                     endsignal=new String("END");
                }
                else{
                    String g=reply+" ";
                    String parts[]=g.split(" ");
                   nextconsignment=Integer.parseInt(parts[parts.length-2])+1;
                GREETING="ACK "+nextconsignment;
               }
				System.out.println(reply);
                
                
                  if (endsignal.equals("END")) // last consignment
					end = true;

			}
		 
			cs.close();

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
 
}
