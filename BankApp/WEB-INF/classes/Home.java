import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Home extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //The connection for the database 
		Connection connection = null;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?serverTime=UTC", "root", "root");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Retrieving the answer from the radio button
		String choice = request.getParameter("choice");
		//Converts the string answer to int 
		int pin = Integer.parseInt(request.getParameter("pin"));
		int val = Integer.parseInt(request.getParameter("amount"));
	    //Withdrawal 
		if (choice.equals("with")) {
			//int value user stores the balance
			int bal = viewBalance(pin);

			PreparedStatement with;
			 // Validation for lodging money out , to ensure the user does not exceed their total amount 
			if (bal > val) {
				try {
					//SQL Statement that will update the balance 
					with = connection.prepareStatement("UPDATE accounts SET balance = balance -? WHERE pin= ?");
					with.setInt(1, val);
					with.setInt(2, pin);
					int rowsUpdated = with.executeUpdate();
					with.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 // Returns the users details of the transaction 
				out.print("\"<html><head><title>Bank Balance</title></head>" + "<body><h1>Successful !</h1>"
						+ "<p> Your  transfer details : Account PIN : " + request.getParameter("pin") + " Amount: "
						+ request.getParameter("amount") + "</p></body>");
			} else {
				//If the user exceeds their total amount
				out.print("\"<html><head><title>404</title></head>"
						+ "<body><h1>Sorry you are exceeding your total balance amount! </h1>");
			}
		}
       //Lodgement 
		if (choice.equals("lodge")) {
			PreparedStatement with;
			
				try {
					//SQL Statement that will update the balance 
					with = connection.prepareStatement("UPDATE accounts SET balance = balance +? WHERE pin= ?");
					with.setInt(1, val);
					with.setInt(2, pin);
					int rowsUpdated = with.executeUpdate();
					with.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                // Returns the users details of the transaction 
				out.print("\"<html><head><title>Bank Balance</title></head>" + "<body><h1>Successful !</h1>"
						+ "<p> Your transfer details : Account PIN " + request.getParameter("pin") + "  Amount: "
						+ request.getParameter("amount") + "</p></body>");
			 
		}
	
		}

	
 // view balance function: Using the pin , it is passed as a parameter and searched in the database using the result set , if the pin exists the balance will be returned
	public int viewBalance(int pin) {
		//this int val is used to ensure the balance doesn't fall below 0
		int val = -1;
		//The connection 
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?serverTime=UTC", "root",
				"root")) {
         //SQL statement used to retrieve the balance 
			PreparedStatement ps = con.prepareStatement("SELECT balance FROM accounts WHERE pin =?");
			ps.setInt(1, pin);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				//Retrieves the balance
				val = rs.getInt("balance");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //Returns the value
		return val;

	}
}
