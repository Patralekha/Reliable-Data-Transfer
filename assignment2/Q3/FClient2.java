import java.net.*;
import java.io.*;
import java.util.*;

public class FClient2 {

	public static void main(String[] args) {

	    DatagramSocket cs = null;
		FileOutputStream fos = null;

		try {

		System.out.println("Client is ready ! ");
         System.out.println(" Requesting demoText.html from Server ");

	    	cs = new DatagramSocket();
            cs.setSoTimeout(3000);
        	byte[] rd, sd;
			String reply;
			DatagramPacket sp,rp;
			int count=1;
			boolean end = false;

			// write received data into demoText1.html
			fos = new FileOutputStream("demoText1.html");

			while(!end)
			{
			    String ack = "" + count;

				// send ACK
			    sd=ack.getBytes();
			    sp=new DatagramPacket(sd,sd.length,
									  InetAddress.getByName(args[0]),
  									  Integer.parseInt(args[1]));
				cs.send(sp);

				// get next consignment
				rd=new byte[512];
				rp=new DatagramPacket(rd,rd.length);
			    try
			    {

			    cs.receive(rp);

				// concat consignment
			    reply=new String(rp.getData());

			    System.out.println(reply);
			    if(count<=7)
                {
                System.out.print("\n For the above message ACK with sequence number: "+count+" successfully sent to server !! \n\n");
                }
				fos.write(rp.getData());

				if (reply.trim().equals("END")) // if last consignment then  modify end to stop loop
					end = true;
			    }
			    catch(SocketTimeoutException ex)
			{
				System.out.println("Time out ! Did not receive packet ! Waiting for Server to resend ...");
				if(count==25)
                    end=true;
			}


				count++;
			}
              System.out.println("Protocol ended. All packets succesfully recieved . Client Program terminated . ...");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());

		} finally {

			try {
				if (fos != null)
					fos.close();
				if (cs != null)
					cs.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
}
