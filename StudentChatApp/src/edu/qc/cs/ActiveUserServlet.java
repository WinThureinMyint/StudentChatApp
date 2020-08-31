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

@WebServlet("/ActiveUserServlet")
public class ActiveUserServlet extends HttpServlet {
	private static final long serialVersionUID = 2052424406343911308L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONArray result = new JSONArray();
		try {
			String GroupName = request.getParameter("GroupName");
			Connection con = null;
			MySQL_ConnectionLevel3 mysql = new MySQL_ConnectionLevel3();
			con = mysql.connectDB();
			con.setAutoCommit(false);
			PreparedStatement res = con.prepareStatement(
					"SELECT `GroupName`, `UserID`, `UserName`, `lastActiveTime`, `active` FROM chatApp.ActiveUser where GroupName=?;");
			res.setString(1, GroupName);
			ResultSet rs;
			rs = res.executeQuery();
			con.commit();
			while (rs.next()) {
				String GroupName1 = rs.getString("GroupName");
				String UserID = rs.getString("UserID");
				String UserName = rs.getString("UserName");
				String lastActiveTime = rs.getString("lastActiveTime");
				boolean active = rs.getBoolean("active");
				JSONObject jsonobject = new JSONObject();
				jsonobject.put("GroupName", GroupName1);
				jsonobject.put("UserID", UserID);
				jsonobject.put("UserName", UserName);
				jsonobject.put("lastActiveTime", lastActiveTime);
				jsonobject.put("active", active);
				result.add(jsonobject);

			}

		} catch (SQLException e) {
			result.clear();
		}
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(result);
		out.flush();

	}
	//http://localhost:8080/CS370_Project2/ActiveUserServelt?UserID=cs1&GroupName=cs99&UserName=LMAO
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message = null;
		Connection conn = null;
		JSONObject result = new JSONObject();
		result.put("Status", "successful");
		try {
			String GroupName = request.getParameter("GroupName");
			String UserID = request.getParameter("UserID");
			String UserName = request.getParameter("UserName");
			System.out.print(GroupName + " " + UserID + " " + UserName);
			MySQL_ConnectionLevel3 mysql = new MySQL_ConnectionLevel3();
			conn = mysql.connectDB();
			conn.setAutoCommit(false);
			insertVotes(conn, GroupName, UserID,UserName);
			
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			result.put("Status", "fail");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.print(e.getMessage());
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
				.prepareStatement("INSERT INTO `chatApp`.`ActiveUser`(`GroupName`, `UserID`, `UserName`, `lastActiveTime`, `active`) VALUES (?,?,?,NOW(),true) ON DUPLICATE KEY UPDATE `lastActiveTime` = NOW() , `active`=true;");
		res.setString(1, GroupName);
		res.setString(2, UserID);
		res.setString(3, UserName);
		res.executeUpdate();
		con.commit();
	}
}
