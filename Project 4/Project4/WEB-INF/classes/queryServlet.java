/*  Name: Prince Patel  
     Course: CNT 4714 – Fall 2021 – Project Four 
     Assignment title:  A Three-Tier Distributed Web-Based Application 
     Date:  November 21, 2021 
*/ 
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class queryServlet extends HttpServlet {
	private Connection dbConnection;
	private Statement statement;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			Class.forName(config.getInitParameter("dbDriver"));
			dbConnection = DriverManager.getConnection(config.getInitParameter("dbName"), config.getInitParameter("username"), config.getInitParameter("password"));
			statement = dbConnection.createStatement();
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new UnavailableException(e.getMessage());
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String statement = request.getParameter("sqlStatement");
		String result = null;
		
		if (statement.contains("select")) { //Do if there is a query

			try {
				result = processSelectQuery(statement);
			} catch (SQLException e) {
				result = "<div class='container' style='background-color:red;color:white;padding:12px;'> <h1 style='font-size:20px'>Error executing the SQL statement:</h1> <p>"
				+ e.getMessage() + "</p></div>";

				e.printStackTrace();
			}
		}
		//Process any updates to the database
		else {
			try {
				result = processUpdateQuery(statement);
			}catch(SQLException e) {
				result = "<div class='container' style='background-color:red;color:white;padding:12px;'> <h1 style='font-size:20px'>Error executing the SQL statement:</h1> <p>"
				+ e.getMessage() + "</p></div>";

				e.printStackTrace();
			}
		}

		HttpSession currSession = request.getSession();
		currSession.setAttribute("result", result);
		currSession.setAttribute("sqlStatement", statement);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
		//Notes: Update the query with the forward statement so that it is invisible to the client
		//The way this works is by updating the index.jsp
	}

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public String processSelectQuery(String textBox) throws SQLException {
		String result;
		
		ResultSet resTable = statement.executeQuery(textBox);
		ResultSetMetaData metaData = resTable.getMetaData(); //Obtain metadata
		int numCol = metaData.getColumnCount();

		String initHTML = "<div class='container-fluid'><div class='row justify-content-center'><div class='table-responsive-sm-10 table-responsive-md-10 table-responsive-lg-10'><table id='resdata' class='table table-striped'>";

		String initColHTML = "<thead><tr>";
		for (int i = 1; i <= numCol; i++) {
			initColHTML += "<th scope='col'>" + metaData.getColumnName(i) + "</th>";
		}

		initColHTML += "</tr></thead>";

		String tableBodyHTML = "<tbody>";
		while (resTable.next()) {
			tableBodyHTML += "<tr>";
			for (int i = 1; i <= numCol; i++) {
				if (i == 1)
					tableBodyHTML += "<td scope'row'>" + resTable.getString(i) + "</th>";
				else
					tableBodyHTML += "<td>" + resTable.getString(i) + "</th>";
			}
			tableBodyHTML += "</tr>";
		}

		tableBodyHTML += "</tbody>";

		String tableClosingHTML = "</table></div></div></div>";
		result = initHTML + initColHTML + tableBodyHTML + tableClosingHTML;

		return result;
	}
	
	private String processUpdateQuery(String convSqlCase) throws SQLException {
		String result = null;
		int affectedRows = 0;
		ResultSet quantCheckBefore = statement.executeQuery("select COUNT(*) from shipments where quantity >= 100");
		quantCheckBefore.next();
		int numShipment100Prior = quantCheckBefore.getInt(1);
		
		statement.executeUpdate("create table beforeShipment like shipments");
		statement.executeUpdate("insert into beforeShipment select * from shipments");
		
		affectedRows = statement.executeUpdate(convSqlCase);
		result = "<div class='container' style='background-color:#00e600;color:black;padding:12px;font-weight:bold'><div>The statement executed successfully." + affectedRows + " row(s) affected</div>";
		
		ResultSet quantCheckAfter = statement.executeQuery("select COUNT(*) from shipments where quantity >= 100");
		quantCheckAfter.next();
		int numShipment100After = quantCheckAfter.getInt(1);
		
		
		if(numShipment100Prior < numShipment100After) {
			//Bonus problem
			int rowsAffectedIncrement5 = statement.executeUpdate("update suppliers set status = status + 5 where snum in ( select distinct snum from shipments left join beforeShipment using (snum, pnum, jnum, quantity) where beforeShipment.snum is null)");
			result += "<div>Business Logic Detected! - Updating Supplier Status</div>";
			result += "<div>Business Logic updated " + rowsAffectedIncrement5 + " supplier status marks</div>";
		}

		result +="</div";
		
		statement.executeUpdate("drop table beforeShipment");
		
		return result;
	}

}