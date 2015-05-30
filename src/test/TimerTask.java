package test;


import java.util.Timer;

public class TimerTask {
	Timer timer;
	Timer timer2;
	 public void setUp(){
	    	timer = new Timer();
	        timer.schedule(new CrawlTask(),
	               0,        //initial delay
	               5000); 
	    }
	 
	 public void setUp2(){
	    	timer2 = new Timer();
	        timer2.schedule(new CrawlTask2(),
	               0,        //initial delay
	               1000); 
	    }
	 
	 class CrawlTask extends java.util.TimerTask {
	    	    		 		
	    	    public void run() {
	            	System.out.println(111);            	
	            }
	    	}
	  
	 class CrawlTask2 extends java.util.TimerTask {
	 		
 	    public void run() {
         	System.out.println(222);            	
         }
 	}
	    	   
	    public static void main(String []args){
	    	TimerTask t = new TimerTask();
	    	t.setUp();
	    	t.setUp2();
	    }
}
