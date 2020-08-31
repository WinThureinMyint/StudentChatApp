package edu.qc.cs;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 205242440643911308L;

	int checkMessage(Connection con, String GroupName, String MessageID) throws SQLException {
		PreparedStatement res = con.prepareStatement(
				"select voteID from `chatApp`.`Votes` as v where (v.GroupName=?) and (v.MessageID=?)");
		res.setString(1, GroupName);
		res.setString(2, MessageID);

		ResultSet rs = res.executeQuery();
		int voteID = 0;
		while (rs.next()) {
			voteID = rs.getInt("voteID");
		}
		return voteID;
	}

	void insertVotes(Connection con, String GroupName, String MessageID,String Message) throws SQLException {
		PreparedStatement res = con
				.prepareStatement("INSERT INTO `chatApp`.`Votes`(`GroupName`, `MessageID`,`TotalVote`,`Message`) VALUES(?,?,0,?)");
		res.setString(1, GroupName);
		res.setString(2, MessageID);
		res.setString(3, Message);
		res.executeUpdate();
		con.commit();
	}

	void insertVoteUser(Connection con, String UserID, int VoteID) throws SQLException {
		String insertVoteUser = "INSERT INTO `chatApp`.`VotedUser`(`VoteID`,`UserID`)VALUES(" + VoteID + ",'" + UserID
				+ "');";
		String updateTotalVote = "UPDATE `chatApp`.`Votes` SET `TotalVote` =TotalVote+(select weight from chatApp.UserWeight where userID='"
				+ UserID + "') WHERE `VoteID` =" + VoteID + ";";
		String insertUserWeight = "INSERT INTO `chatApp`.`UserWeight` VALUES('" + UserID
				+ "',1) ON DUPLICATE KEY UPDATE Weight = Weight;";
		System.out.println(updateTotalVote);
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.addBatch(insertVoteUser);
		stmt.addBatch(insertUserWeight);
		stmt.addBatch(updateTotalVote);
		stmt.executeBatch();
		con.commit();
	}

	/*
	 * void test() { Connection conn = null;
	 * 
	 * try { String UserID = "U03"; String GroupID = "G01"; String MessageID =
	 * "M02"; MySQL_ConnectionLevel3 mysql = new MySQL_ConnectionLevel3(); conn =
	 * mysql.connectDB(); conn.setAutoCommit(false); int voteID = 0; voteID =
	 * checkMessage(conn, GroupID, MessageID); System.out.println(voteID); // just
	 * assume all the user weight are equal if (voteID == 0) { insertVotes(conn,
	 * GroupID, MessageID); voteID = checkMessage(conn, GroupID, MessageID);
	 * insertVoteUser(conn, UserID, voteID); } else { insertVoteUser(conn, UserID,
	 * voteID); } // message = " successfully!"; } catch (SQLException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } finally { try { //
	 * CheckMessage.close(); conn.close(); } catch (SQLException e) {
	 * e.printStackTrace(); }
	 * 
	 * } }
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message = null;
		Connection conn = null;
		//String result = "{ Status: 'successful' }";
		JSONObject result = new JSONObject();
		result.put("Status", "successful");
		try {
			String UserID = request.getParameter("UserID");
			String GroupID = request.getParameter("GroupID");
			String MessageID = request.getParameter("MessageID");
			String Message = request.getParameter("Message");
			MySQL_ConnectionLevel3 mysql = new MySQL_ConnectionLevel3();
			conn = mysql.connectDB();
			conn.setAutoCommit(false);
			int voteID = 0;
			voteID = checkMessage(conn, GroupID, MessageID);
			System.out.println(voteID);
			if (voteID == 0) {
				insertVotes(conn, GroupID, MessageID, Message);
				voteID = checkMessage(conn, GroupID, MessageID);
				insertVoteUser(conn, UserID, voteID);
			} else {
				insertVoteUser(conn, UserID, voteID);
			}
			//message = "successfully!";
		} catch (SQLException e) {
			result.put("Status", "fail");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				result.put("Status", "fail");
			}
			// request.setAttribute("message", message);
			// response.setContentType("text/html");
			// getServletContext().getRequestDispatcher("/response.jsp").forward(request,
			// response);
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(result);
			out.flush();

		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//String result = "{ vote: 'false' }";
		JSONObject result = new JSONObject();
		result.put("vote", "false");
		try {

			String UserID = request.getParameter("UserID");
			String GroupID = request.getParameter("GroupID");
			String MessageID = request.getParameter("MessageID");
			String message = null;
			Connection con = null;
			MySQL_ConnectionLevel3 mysql = new MySQL_ConnectionLevel3();
			con = mysql.connectDB();
			con.setAutoCommit(false);
			PreparedStatement res = con.prepareStatement(
					"select vu.UserID from chatApp.VotedUser as vu where(vu.UserID = ?) and (vu.VoteID = (select VoteID from chatApp.Votes as v where(v.GroupName = ?) && (v.MessageID=?)))");
			res.setString(1, UserID);
			res.setString(2, GroupID);
			res.setString(3, MessageID);
			ResultSet rs;
			rs = res.executeQuery();
			con.commit();
			while (rs.next()) {
				result.put("vote", "true");
			}
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(result);
			out.flush();
		}
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(result);
		out.flush();

	}
	/*
	 * protected void doGet(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException { String message = null;
	 * PreparedStatement pstmt = null; Connection conn = null; String returnHtml =
	 * null; try { if (true) { MySQL_ConnectionLevel3 mysql = new
	 * MySQL_ConnectionLevel3(); conn = mysql.connectDB();
	 * conn.setAutoCommit(false); pstmt =
	 * conn.prepareStatement("SELECT * FROM CS370Db.FileUpload;"); ResultSet rs; rs
	 * = pstmt.executeQuery(); returnHtml = "<table border=\"2\">" + "<tr>\n" +
	 * "    <th>ID</th>\n" + "    <th>File Name</th>\n" + "    <th>Content</th>\n" +
	 * "  </tr>"; while (rs.next()) { int id = rs.getInt("file_Id"); String name =
	 * rs.getString("fileName"); String content = rs.getString("content");
	 * returnHtml += "<tr>\n" + "    <td>" + id + "</td>\n" + "    <td>" + name +
	 * "</td>\n" + "    <td  width=\"40%\">" + content + "</td>\n" + "  </tr>";
	 * 
	 * } returnHtml += "</table>"; // pstmt.executeUpdate(); conn.commit(); message
	 * = " successfully!"; } else { // request.setAttribute("fileName", returnHtml);
	 * request.setAttribute("message", message);
	 * getServletContext().getRequestDispatcher("/response.jsp").forward(request,
	 * response); } } catch (Exception e) { System.err.println("Error: " +
	 * e.getMessage()); message = " Failed!"; e.printStackTrace(); } finally { try {
	 * pstmt.close(); conn.close(); } catch (SQLException e) { // TODO
	 * e.printStackTrace(); } // fis.close(); request.setAttribute("fileName",
	 * returnHtml); request.setAttribute("message", message); //
	 * writeToResponse(response, message);
	 * getServletContext().getRequestDispatcher("/response.jsp").forward(request,
	 * response);
	 * 
	 * } }
	 */
}
