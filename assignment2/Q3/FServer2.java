import java.net.*;
import java.io.*;
import java.util.*;

public class FServer2
{
private static final double LOSS_RATE = 0.3;
   private static final int AVERAGE_DELAY = 100;  // milliseconds

	public static void main(String[] args) {

		DatagramSocket ss = null;
		FileInputStream fis = null;
		DatagramPacket rp, sp;
		byte[] rd, sd;
         int flag=0;
		InetAddress ip;
		int port;
		int counter=1;
		int extra=1;
		boolean ter=false;
     Random random = new Random();

		try {
			ss = new DatagramSocket(Integer.parseInt(args[0]));
			System.out.println("Server is up....");
			System.out.println("Intiating RDT protocol.Waiting for Client request....");


			// read file into buffer
			fis = new FileInputStream("demoText.html");

			int consignment;
			String strConsignment;
			int result = 0; // number of bytes read

			while(!(ter) && result!=-1){

				rd=new byte[100];
				sd=new byte[512];

				rp = new DatagramPacket(rd,rd.length);

				ss.receive(rp);
               if (random.nextDouble() < LOSS_RATE)
                {
                System.out.println("   Frame: "+counter+" not sent. Trying to resend ...");
                 continue;
                 }

         // Simulate network delay.
              //  Thread.sleep((int) (random.nextDouble() * 2 * AVERAGE_DELAY));

				// get client's consignment request from DatagramPacket
				ip = rp.getAddress();
				port =rp.getPort();
				System.out.println("Client IP Address = " + ip);
				System.out.println("Client port = " + port);

				strConsignment = new String(rp.getData());
				consignment = Integer.parseInt(strConsignment.trim());
				if(extra<=7)
                {
                if(extra==7)
                {

                System.out.println("RDT with Sequence number: "+counter+"with payload :"+consignment+" succesfully sent ! !\n\n");
                ter=true;
                }
                else
                {
                    System.out.println("RDT with Sequence number: "+counter+"with payload :"+consignment+" succesfully sent ! !\n\n");

                }
                }
				// prepare data
				result = fis.read(sd);
				if (result == -1) {
					/*sd = new String("END").getBytes();*/
					consignment = -1;
				}
				sp=new DatagramPacket(sd,sd.length,ip,port);

				ss.send(sp);

				rp=null;
				sp = null;
                }
             counter++;

		}
		 catch (IOException ex) {
			System.out.println(ex.getMessage());

		}

		finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}

System.out.println("Protocol ended. All packets succesfully sent . Server Program terminated . ...");

	}

}

