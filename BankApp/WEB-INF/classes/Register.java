import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Register extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//The connection for the database 
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?serverTime=UTC", "root", "root");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//Retrieving the answer from the radio button
			String aPassword = request.getParameter("aPassword");
			String bPassword = request.getParameter("bPassword");
			//Converts the string answer to int
			int pin = Integer.parseInt(request.getParameter("pin"));
			// Validation for password
			if (aPassword.equals(bPassword)) {
				PreparedStatement createUser;
				createUser = connection.prepareStatement("INSERT into accounts" + "(pin,user,password)" + "VALUES(?,?,?)");
				createUser.setInt(1, pin);
				createUser.setString(2, request.getParameter("username"));
				createUser.setString(3, request.getParameter("aPassword"));
				int rowsUpdated = createUser.executeUpdate();
				createUser.close();
				response.sendRedirect("login.html");
			} else {
				//Message , if the passwords do not match 
				PrintWriter out = response.getWriter(); // this
				out.print("<html><head><title>Password Error</title></head>"
						+ "<body><h1>Sorry passwords do not match!</h1>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
