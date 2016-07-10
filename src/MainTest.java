//Restaurant Bill Calculator Application
//Java Project: Part 2
//Author: Aimee Tyrrell
//Author email address: aimee.tyrrell@gmail.com

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainTest {

	// main method   
	public static void main( String[] args )    
	{   
		final String USER = "root";
		final String PASS = "root";

		// check command-line arguments   
		if ( args.length == 2 )   
		{   
			// get command-line arguments 
			String databaseDriver = args[ 0 ];  
			String databaseURL = args[ 1 ];

			// create new RestaurantBillCalculator   
			RestaurantBillCalculator application = new RestaurantBillCalculator(  databaseDriver, databaseURL, USER, PASS );

			//Exits the object on close
			application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}   

		else{
			//create new warning alert for users if number of arguments haven't been provided
			JOptionPane warning = new JOptionPane();
			//add warning message for user trying to connect with incorrect credentials
			warning.showMessageDialog(null, "Correct numbers of database information has not been provided. Hint: you need both driver and database URL");
		}
	} // end method main   
}

