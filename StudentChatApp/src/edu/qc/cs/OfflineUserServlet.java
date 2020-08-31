package edu.qc.cs;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/OfflineUserServlet")
public class OfflineUserServlet extends HttpServlet {
	private static final long serialVersionUID = 2052424403443911308L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message = null;
		Connection conn = null;
		JSONObject result = new JSONObject();
		result.put("Status", "successful");
		try {
			String UserID = request.getParameter("UserID");
			String GroupName = request.getParameter("GroupName");
			String UserName = request.getParameter("MessageID");
			MySQL_ConnectionLevel3 mysql = new MySQL_ConnectionLevel3();
			conn = mysql.connectDB();
			conn.setAutoCommit(false);
			insertVotes(conn, GroupName, UserID,UserName);
		} catch (SQLException e) {
			result.put("Status", "fail");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				result.put("Status", "fail");
			}

			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(result);
			out.flush();

		}
	}
	void insertVotes(Connection con, String GroupName, String UserID,String UserName) throws SQLException {
		PreparedStatement res = con
				.prepareStatement("INSERT INTO `chatApp`.`ActiveUser`(`GroupName`, `UserID`, `UserName`, `lastActiveTime`, `active`) VALUES (?,?,?,NOW(),true) ON DUPLICATE KEY UPDATE  `active`=false;");
		res.setString(1, GroupName);
		res.setString(2, UserID);
		res.setString(3, UserName);
		res.executeUpdate();
		con.commit();
	}
}
