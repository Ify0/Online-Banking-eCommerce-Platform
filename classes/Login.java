import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.PortableServer.ForwardRequest;

public class Login extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//The connection for the database 
		Connection connection = null;
		response.setContentType("text/html");
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?serverTime=UTC", "root", "root");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement login = null;
		try {

			login = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Prints out the results from the database
		ResultSet rs = null;
		try {
			rs = login.executeQuery("select * from accounts");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        //Retrieves answer from html page 
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try {
			//Iterates through the database looking for the username and password 
			if (rs.next()) {
				try {
					if (rs.getString(1).equalsIgnoreCase(username) && rs.getString(2).equalsIgnoreCase(password));
					//proceeds to the login page 
					response.sendRedirect("homePage.html");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
                //if the account does not exist
				PrintWriter out = response.getWriter();
				out.print("\"<html><head><title>Error</title></head>"
						+ "<body><h1>Sorry this account does not exist! !</h1>");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
