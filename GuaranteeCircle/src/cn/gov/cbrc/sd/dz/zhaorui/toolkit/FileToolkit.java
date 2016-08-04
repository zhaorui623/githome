package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class FileToolkit {
	public static long forChannel(File f1,File f2) throws Exception{   
	        long time=new Date().getTime();   
	        int length=2097152;   
	         FileInputStream in=new FileInputStream(f1);   
	         FileOutputStream out=new FileOutputStream(f2);   
	         FileChannel inC=in.getChannel();   
	         FileChannel outC=out.getChannel();   
	         ByteBuffer b=null;   
	        while(true){   
	            if(inC.position()==inC.size()){   
	                 inC.close();   
	                 outC.close();   
	                return new Date().getTime()-time;   
	             }   
	            if((inC.size()-inC.position())<length){   
	                 length=(int)(inC.size()-inC.position());   
	             }else  
	                 length=2097152;   
	             b=ByteBuffer.allocateDirect(length);   
	             inC.read(b);   
	             b.flip();   
	             outC.write(b);   
	             outC.force(false);   
	         }   
	     }

	public void fileChannelCopy(File s, File t) {
	
	        FileInputStream fi = null;
	
	        FileOutputStream fo = null;
	
	        FileChannel in = null;
	
	        FileChannel out = null;
	
	        try {
	
	            fi = new FileInputStream(s);
	
	            fo = new FileOutputStream(t);
	
	            in = fi.getChannel();//得到对应的文件通道
	
	            out = fo.getChannel();//得到对应的文件通道
	
	            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
	
	        } catch (IOException e) {
	
	            e.printStackTrace();
	
	        } finally {
	
	            try {
	
	                fi.close();
	
	                in.close();
	
	                fo.close();
	
	                out.close();
	
	            } catch (IOException e) {
	
	                e.printStackTrace();
	
	            }
	
	        }
	
	    }  
}

