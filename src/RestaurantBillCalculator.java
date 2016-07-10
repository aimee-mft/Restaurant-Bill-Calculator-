//Restaurant Bill Calculator Application
//Java Project: Part 2
//Author: Aimee Tyrrell
//Author email address: aimee.tyrrell@gmail.com

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RestaurantBillCalculator extends JFrame {

	// JLabel for Restaurant
	private JLabel restaurantJLabel;

	// JPanel to display waiter information
	private JPanel waiterJPanel;

	// JLabel and JTextField for table number
	private JLabel tableNumberJLabel;
	private JComboBox tableNumberJComboBox;

	// JLabel and JTextField for waiter name
	private JLabel waiterNameJLabel;
	private JTextField waiterNameJTextField;

	// JPanel to display menu items
	private JPanel menuItemsJPanel;

	// JLabel and JComboBox for beverage
	private JLabel beverageJLabel;
	private JComboBox beverageJComboBox;

	// JLabel and JComboBox for appetizer
	private JLabel appetizerJLabel;
	private JComboBox appetizerJComboBox;

	// JLabel and JComboBox for main course
	private JLabel mainCourseJLabel;
	private JComboBox mainCourseJComboBox;

	// JLabel and JComboBox for dessert
	private JLabel dessertJLabel;
	private JComboBox dessertJComboBox;

	// JButton for calculate bill
	private JButton calculateBillJButton;
	
	// JButton for pay bill
	private JButton payBillJButton;

	// JButton for save table
	private JButton saveTableJButton;

	// JLabel and JTextField for subtotal
	private JLabel subtotalJLabel;
	private JTextField subtotalJTextField;

	// JLabel and JTextField for tax
	private JLabel taxJLabel;
	private JTextField taxJTextField;

	// JLabel and JTextField for total
	private JLabel totalJLabel;
	private JTextField totalJTextField;

	// constant for tax rate
	private final static double TAX_RATE = 0.05;

	// created new JFrame
	JFrame contentPane;

	// declare instance variables for database processing
	private Connection myConnection;
	private Statement myStatement;
	private ResultSet myResultSet;
	
	// declare instance variable ArrayList to hold bill items
	private ArrayList billItems = new ArrayList();
	private double subtotal;
	private boolean connectedToDatabase = false;

	// constructor
	public RestaurantBillCalculator(String databaseDriver, String databaseURL, String USER, String PASS) {
		// make database connection
		try {

			// load driver
			Class.forName(databaseDriver);
			// connect to database
			myConnection = DriverManager.getConnection(databaseURL, USER, PASS);
			// create statement
			myStatement = myConnection.createStatement();
			// set connected boolean to true (for checks later)
			connectedToDatabase = true;

		} catch (SQLException exception) {
			// add code here to MS access database

		} catch (ClassNotFoundException exception) {
			 exception.printStackTrace();
		}

		if (!connectedToDatabase) {
			createErrorInterface();
		} else {
			createUserInterface();
		}

	} // end constructor

	// create error alert if the database connection could not be found
	private void createErrorInterface() {
		
		//create new JOPtion Pane if no database found
		JOptionPane error = new JOptionPane();
		// add message to Joption Pane
		error.showMessageDialog(null, "Coould not connect to the database, please check the database credentials");

	}

	private void createUserInterface() {

		// get content pane for attaching GUI components
		Container contentPane = getContentPane();

		// enable explicit positioning of GUI components
		contentPane.setLayout(null);

		// set up createWaiterJPanel & menuItemsJPanel
		createWaiterJPanel();
		createMenuItemsJPanel();

		// add createWaiterJPanel & menuItemsJPanel to the content pane
		contentPane.add(waiterJPanel);
		contentPane.add(menuItemsJPanel);

		// restaurantJLabel
		restaurantJLabel = new JLabel("Restaurant");
		restaurantJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		restaurantJLabel.setBounds(140, 10, 125, 25);
		contentPane.add(restaurantJLabel);

		// set up waiterJPanel
		waiterJPanel = new JPanel();
		waiterJPanel.setLayout(null);
		waiterJPanel.setBounds(10, 55, 300, 100);
		waiterJPanel.setBorder(BorderFactory.createTitledBorder("Waiter Information"));
		contentPane.add(waiterJPanel);

		// set up subtotalJLabel
		subtotalJLabel = new JLabel("Subtotal: ");
		subtotalJLabel.setBounds(15, 340, 100, 25);
		subtotalJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		contentPane.add(subtotalJLabel);

		// set up subtotalJTextField
		subtotalJTextField = new JTextField();
		subtotalJTextField.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		subtotalJTextField.setBackground(null);
		subtotalJTextField.setBounds(100, 340, 100, 25);

		subtotalJTextField.setEditable(false);
		subtotalJTextField.setHorizontalAlignment(JTextField.RIGHT);
		subtotalJTextField.setBackground(null);
		subtotalJTextField.setBorder(BorderFactory.createLoweredBevelBorder());
		subtotalJTextField.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		contentPane.add(subtotalJTextField);

		// taxJLabel
		taxJLabel = new JLabel("Tax: ");
		taxJLabel.setBounds(15, 370, 100, 25);
		taxJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		contentPane.add(taxJLabel);

		// set up taxJTextField
		taxJTextField = new JTextField(5);
		taxJTextField.setBounds(100, 370, 100, 25);
		taxJTextField.setHorizontalAlignment(JTextField.RIGHT);
		taxJTextField.setBackground(null);
		taxJTextField.setEditable(false);
		subtotalJTextField.setBackground(null);
		taxJTextField.setBorder(BorderFactory.createLoweredBevelBorder());
		taxJTextField.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		contentPane.add(taxJTextField);

		// set up totalJLabel
		totalJLabel = new JLabel("Total: ");
		totalJLabel.setBounds(15, 400, 100, 25);
		totalJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		contentPane.add(totalJLabel);

		// set up totalJTextField
		totalJTextField = new JTextField();
		totalJTextField.setBounds(100, 400, 100, 25);
		totalJTextField.setEditable(false);
		totalJTextField.setHorizontalAlignment(JTextField.RIGHT);
		totalJTextField.setBackground(null);
		totalJTextField.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		totalJTextField.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		contentPane.add(totalJTextField);
		
		//Save Table JButton
		saveTableJButton = new JButton("Save Table");
		saveTableJButton.setEnabled(false);
		saveTableJButton.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		saveTableJButton.setBorder(BorderFactory.createRaisedBevelBorder());
		saveTableJButton.setBounds(210, 340, 100, 25);
		add(saveTableJButton);
		
		// Pay Bill Button
		payBillJButton = new JButton("Pay Bill");
		payBillJButton.setEnabled(false);
		payBillJButton.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		payBillJButton.setBorder(BorderFactory.createRaisedBevelBorder());
		payBillJButton.setBounds(210, 400, 100, 25);
		add(payBillJButton);
		
		// Calculate Bill Button
		calculateBillJButton = new JButton("Calculate Bill");
		calculateBillJButton.setEnabled(false);
		calculateBillJButton.setBounds(210, 370, 100, 25);
		calculateBillJButton.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		calculateBillJButton.setBorder(BorderFactory.createRaisedBevelBorder());
		add(calculateBillJButton);
		calculateBillJButton.addActionListener(new ActionListener() // anonymous
		// inner
		// class
		{
			// event handler called when calculateBillJButton is clicked
			public void actionPerformed(ActionEvent event) {
				calculateBillJButtonActionPerformed(event);
			}

		} // end anonymous inner class

		); // end addActionListener

		// set properties of application's window
		setTitle("Restaurant Bill Calculator"); // set window title
		setSize(320, 600); // set window size
		setVisible(true); // display window

	} // end method createUserInterface

	private void createWaiterJPanel() {

		// set up waiterJPanel
		waiterJPanel = new JPanel();
		waiterJPanel.setLayout(null);
		waiterJPanel.setBounds(10, 55, 300, 100);
		waiterJPanel.setBorder(BorderFactory.createTitledBorder("Waiter Information"));
		add(waiterJPanel);

		// set up tableNumberJLabel
		tableNumberJLabel = new JLabel("Table number: ");
		tableNumberJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		tableNumberJLabel.setBounds(5, 30, 100, 25);
		waiterJPanel.add(tableNumberJLabel);

		// set up tableNumberJComboBox
		tableNumberJComboBox = new JComboBox();
		tableNumberJComboBox.setBounds(150, 30, 110, 25);
		tableNumberJComboBox.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		waiterJPanel.add(tableNumberJComboBox);

		// set up waiterNameJLabel
		waiterNameJLabel = new JLabel("Waiter name: ");
		waiterNameJLabel.setBounds(5, 60, 100, 25);
		waiterNameJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		waiterJPanel.add(waiterNameJLabel);

		// set up waiterNameJTextField
		waiterNameJTextField = new JTextField();
		waiterNameJTextField.setBounds(150, 60, 100, 25);
		waiterNameJTextField.setEditable(false);
		waiterNameJTextField.setBorder(BorderFactory.createEtchedBorder());
		waiterNameJTextField.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		waiterJPanel.add(waiterNameJTextField);
	}

	private void createMenuItemsJPanel() {

		// set up MenuItemsJpanel
		menuItemsJPanel = new JPanel();
		menuItemsJPanel.setBounds(10, 170, 300, 150);
		menuItemsJPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Menu Items"));
		menuItemsJPanel.setLayout(null);

		// set up beverageJLabel
		beverageJLabel = new JLabel();
		beverageJLabel.setBounds(10, 25, 80, 24);
		beverageJLabel.setText("Beverage:");
		beverageJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		menuItemsJPanel.add(beverageJLabel);

		// set up beverageJComboBox
		beverageJComboBox = new JComboBox();
		beverageJComboBox.setBounds(90, 25, 170, 25);
		beverageJComboBox.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		menuItemsJPanel.add(beverageJComboBox);
		beverageJComboBox.addItemListener(new ItemListener() // anonymous inner
																// class
		{
			// event handler called when item in beverageJComboBox is selected
			@Override
			public void itemStateChanged(ItemEvent event) {
				beverageJComboBoxItemStateChanged(event);
			}
		} // end anonymous inner class
		); // end addItemListener

		// add items to beverageJComboBox
		beverageJComboBox.addItem("");
		loadCategory("Beverage", beverageJComboBox);

		// set up appetizerJLabel
		appetizerJLabel = new JLabel("Appetizer: ");
		appetizerJLabel.setBounds(10, 60, 100, 25);
		appetizerJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		menuItemsJPanel.add(appetizerJLabel);

		// set up appetizerJComboBox
		appetizerJComboBox = new JComboBox();
		appetizerJComboBox.setBounds(88, 56, 170, 25);
		appetizerJComboBox.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		menuItemsJPanel.add(appetizerJComboBox);
		appetizerJComboBox.addItemListener(new ItemListener() // anonymous inner
																// class
		{
			// event handler called when item in appetizerJComboBox is selected
			@Override
			public void itemStateChanged(ItemEvent event) {
				appetizerJComboBoxItemStateChanged(event);
			}
		} // end anonymous inner class
		); // end

		// add items to appetizerJComboBox
		appetizerJComboBox.addItem("");
		loadCategory("Appetizer", appetizerJComboBox);

		// set up mainCourseJLabel
		mainCourseJLabel = new JLabel("Main: ");
		mainCourseJLabel.setBounds(10, 90, 100, 25);
		mainCourseJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		menuItemsJPanel.add(mainCourseJLabel);

		// set up mainCourseJComboBox
		mainCourseJComboBox = new JComboBox();
		mainCourseJComboBox.setBounds(88, 88, 170, 25);
		mainCourseJComboBox.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		menuItemsJPanel.add(mainCourseJComboBox);

		// add action listener to mainCourseJComboBox
		mainCourseJComboBox.addItemListener(new ItemListener() // anonymous
																// inner class
		{
			// event handler called when item in mainCourseJComboBox is selected
			@Override
			public void itemStateChanged(ItemEvent event) {
				mainCourseJComboBoxItemStateChanged(event);
			}

		} // end anonymous inner class

		); // end addItemListener

		// add items to mainCourseJComboBox
		mainCourseJComboBox.addItem("");
		loadCategory("Main Course", mainCourseJComboBox);

		// set up dessertJLabel
		dessertJLabel = new JLabel();
		dessertJLabel.setBounds(10, 120, 80, 24);
		dessertJLabel.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		dessertJLabel.setText("Dessert:");
		menuItemsJPanel.add(dessertJLabel);

		// set up dessertJComboBox
		dessertJComboBox = new JComboBox();
		dessertJComboBox.setBounds(88, 120, 170, 25);
		dessertJComboBox.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		menuItemsJPanel.add(dessertJComboBox);

		// add action listener to dessertJComboBox
		dessertJComboBox.addItemListener(new ItemListener() // anonymous inner
															// class
		{
			// event handler called when item in dessertJComboBox
			// is selected
			@Override
			public void itemStateChanged(ItemEvent event) {
				dessertJComboBoxItemStateChanged(event);
			}

		} // end anonymous inner class

		); // end addItemListener

		// add items to dessertJComboBox
		dessertJComboBox.addItem("");
		loadCategory("Dessert", dessertJComboBox);

	} // end method createMenuItemsJPanel
	
	
	// reset JFrame
		private void resetJFrame() {
			
			// reset instance variable
			billItems = new ArrayList();

			// reset and disable menuItemsJPanel
			menuItemsJPanel.setEnabled(false);
			beverageJComboBox.setSelectedIndex(0);
			appetizerJComboBox.setSelectedIndex(0);
			mainCourseJComboBox.setSelectedIndex(0);
			dessertJComboBox.setSelectedIndex(0);
			beverageJComboBox.setEnabled(false);
			appetizerJComboBox.setEnabled(false);
			mainCourseJComboBox.setEnabled(false);
			dessertJComboBox.setEnabled(false);

			// reset and enable waiterJPanel
			waiterJPanel.setEnabled(true);
			tableNumberJComboBox.setEnabled(true);
			tableNumberJComboBox.setSelectedIndex(0);
			waiterNameJTextField.setText("");

			// clear JTextFields
			subtotalJTextField.setText("");
			taxJTextField.setText("");
			totalJTextField.setText("");

			// disable JButtons
			saveTableJButton.setEnabled(false);
			calculateBillJButton.setEnabled(false);
			payBillJButton.setEnabled(false);

		} // end method resetJFrame


	// add items to JComboBox
	private void loadCategory(String category, JComboBox categoryJComboBox) {

		try {

			// SQL query to select from database and order Ascending
			myResultSet = myStatement.executeQuery("SELECT `name` FROM `menu` WHERE `category` = '" + category
					+ "' AND  name <> '<<NONE>>' ORDER BY name ASC");

			// add items to JComboBox
			while (myResultSet.next() == true) {
				categoryJComboBox.addItem(myResultSet.getString("name"));
			}
			// close myResultSet
			myResultSet.close();

		} // end try

		// catch SQLException
		catch (SQLException exception) {
			exception.printStackTrace();
		}

	} // end method loadCategory

	// user select beverage
	private void beverageJComboBoxItemStateChanged(ItemEvent event) {
		// select an item
		if (event.getStateChange() == ItemEvent.SELECTED) {
			billItems.add((String) beverageJComboBox.getSelectedItem());
		}

	} // end method beverageJComboBoxItemStateChanged

	// user select appetizer
	private void appetizerJComboBoxItemStateChanged(ItemEvent event) {
		// select an item
		if (event.getStateChange() == ItemEvent.SELECTED) {
			billItems.add((String) appetizerJComboBox.getSelectedItem());
		}

	} // end method appetizerJComboBoxItemStateChanged

	// user select main course
	private void mainCourseJComboBoxItemStateChanged(ItemEvent event) {
		// select an item
		if (event.getStateChange() == ItemEvent.SELECTED) {
			// add string representation of the selected Combobox item to the
			// Array List Bill Items
			billItems.add((String) mainCourseJComboBox.getSelectedItem());
		}

	} // end method mainCourseJComboBoxItemStateChanged

	// user select dessert
	private void dessertJComboBoxItemStateChanged(ItemEvent event) {
		// select an item
		if (event.getStateChange() == ItemEvent.SELECTED) {
			// add string representation of the selected Combobox item to the
			// Array List Bill Items
			billItems.add((String) dessertJComboBox.getSelectedItem());
		}

	} // end method dessertJComboBoxItemStateChanged

	// user clicks CalculateBillJButton
	private void calculateBillJButtonActionPerformed(ActionEvent event) {

		if (tableNumberJComboBox.getSelectedItem().equals(0) || waiterNameJTextField.getText().equals("")) {
			// if table number and waiter name are empty once user clicks calculate bill 
			// fire JOption Pane warning
			JOptionPane pane = new JOptionPane();
			// add message to Joption Pane
			pane.showMessageDialog(null, "Table Number and waiter name must contain information");

		} else {

			double total = calculateSubtotal();
			// display subtotal, tax and total
			displayTotal(total);
		}

	} // end method calculateBillJButtonActionPerformed

	// calculate sub total
	private double calculateSubtotal() {

		double total = subtotal;
		Object[] items = billItems.toArray();

		// get data from database
		try {
			// get price for each item in items array
			for (int i = 0; i < items.length; i++) {

				// execute query to get price
				myResultSet = myStatement.executeQuery("SELECT price " + "FROM menu WHERE name = '" + (String) items[i] + "'");

				// myResultSet not empty
				if (myResultSet.next() == true) {
					total += myResultSet.getDouble("price");
				}
				// close myResultSet
				myResultSet.close();
			} // end for
		} // end try

		// catch SQLException
		catch (SQLException exception) {

			exception.printStackTrace();
		}

		return total;

	} // end method calculateSubtotal

	private void displayTotal(double total) {
		// define display format
		DecimalFormat dollars = new DecimalFormat("$0.00");

		// display subtotal
		subtotalJTextField.setText(dollars.format(total));

		// calculate and display tax
		double tax = total * TAX_RATE;
		taxJTextField.setText(dollars.format(tax));

		// display total
		totalJTextField.setText(dollars.format(total + tax));
	} // end method displayTotal

	// user close window
	private void frameWindowClosing(WindowEvent event) {
		// close myStatement and database connection
		try {
			myStatement.close();
			myConnection.close();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} finally {
			System.exit(0);
		}

	} // end method frameWindowClosing

} // end class RestaurantBillCalculator
