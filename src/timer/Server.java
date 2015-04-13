package timer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import util.XMLHelper;
import metadao.MetaDaoController;
import metadao.MySQLController;
import analysis.AnalysisModule;
import analysis.BasicAnalysis;
import crawler.CrawlModule;
import crawler.DataSource;
import dao.DaoController;

public class Server {
    Timer timer;
    int task_period;
    
    public Server(){
    	super();
    }
    
    public void setUp(){
    	task_period = selectPeriod();
    	timer = new Timer();
        timer.schedule(new CrawlTask(),
               0,        //initial delay
               task_period); 
    }
    
    public int selectPeriod(){
    	XMLHelper xmlHelper = new XMLHelper();
		ArrayList<Integer> periods = xmlHelper.PeriodInputXML("configure.xml");		
    	return periods.get(0);
    }
    
    class CrawlTask extends TimerTask {
    	
    	public void crawlAndAnalysis(String projectName,String owner,String filepath){
    			//get all metadata to server
    			DataSource dataSource = new CrawlModule(projectName,owner,filepath);
    			dataSource.getData();
        		
        		MetaDaoController metadaoController = new MySQLController(dataSource);
        		metadaoController.IntoDataBase();
        		System.out.println("succeed saving metadata...");
        		
        		
        		AnalysisModule analysisModule = new BasicAnalysis(metadaoController);
        		analysisModule.analyzeAll();
        		System.out.println("succeed analysising metadata...");
        		
        		
        		DaoController daoController = new DaoController(analysisModule);
        		daoController.IntoDataBase();
        		System.out.println("succeed saving useful data...");       		
        		        		
        		//now some data already in database
        		analysisModule.invokeEvolveAnalysis();
        	
        		//finally evolve chart 
        		daoController.EvolveIntoDB(analysisModule.getEvolve_echarts());   		
    			
        		System.out.println(owner + "'s " + projectName + " project all finished!");
    		}
    		 		
    	    public void run() {
            	String projectName,owner,filepath;
            	//nikolaypavlov,codahale,spinfo,bytedeco,pagseguro
            	//MLPNeuralNet,bcrypt-ruby,java,javacv,java
            	projectName = "java";
            	owner = "spinfo";
            	filepath = "Downloads/" + owner +"/";
            	System.out.println(filepath);
            	crawlAndAnalysis(projectName,owner,filepath);
            }
    	}
    	
    	
    
    public static void main(String []args){
    	Server server = new Server();
    	server.setUp();
    	
    }
}
