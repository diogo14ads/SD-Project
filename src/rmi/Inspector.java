package rmi;

import java.util.ArrayList;

import common.DatabaseRow;

public class Inspector extends Thread{

	private int interval;
	private DatabaseConnection dbCon;
	
	public Inspector(int interval, DatabaseConnection dbCon)
	{
		this.interval = interval;
		this.dbCon = dbCon;
		this.start();
	}
	
	public void run()
	{
		System.out.println("Inspector is looking for expired projects...");
		while(true)
		{
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			patrol();
		}
	}

	private void patrol() {

		dbCon.endExpiredProject();
		
	}
}
