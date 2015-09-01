package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

public class IPToolkit {

	public static boolean isInSameNet(String ip1, String mask1, String ip2,
			String mask2) {
		if(mask1.equals(mask2)==false)
			return false;
		int net1=getValue(ip1)&getValue(mask1);
		int net2=getValue(ip2)&getValue(mask2);
//		System.out.println(Integer.toBinaryString(getValue(ip1)));
//		System.out.println(Integer.toBinaryString(getValue(mask1)));	
//		System.out.println(Integer.toBinaryString(getValue(ip1)&getValue(mask1)));
//		System.out.println();
//		
//		System.out.println(net1);
//		System.out.println(Integer.toBinaryString(net1));
//		System.out.println();
//		
//		System.out.println(Integer.toBinaryString(getValue(ip2)));
//		System.out.println(Integer.toBinaryString(getValue(mask2)));	
//		System.out.println(Integer.toBinaryString(getValue(ip1)&getValue(mask2)));
//		System.out.println();
//		
//		System.out.println(net2);
//		System.out.println(Integer.toBinaryString(net2));
		
		if(net1==net2)
			return true;
		else 
			return false;
		
	}
	
	public static int getValue(String ip){
		String a[]=ip.split("\\.");
		int a0=Integer.parseInt(a[0])<<24;
		int a1=Integer.parseInt(a[1])<<16;
		int a2=Integer.parseInt(a[2])<<8;
		int a3=Integer.parseInt(a[3]);
		return a0+a1+a2+a3;
	}
	
	public static void main(String[] args) {
		boolean bool=isInSameNet("192.168.0.1","255.255.255.0","192.168.0.2","255.255.254.0");
		System.out.println(bool);
	}


}
