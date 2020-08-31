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

@WebServlet("/GetMessagesServlet")
public class GetMessagesServlet extends HttpServlet {
	private static final long serialVersionUID = 2052424406343911308L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// String result = "{ MessageID: '0', TotalVotes: '0'}";
		// JSONObject result = new JSONObject();
		JSONArray result = new JSONArray();
		try {
			String GroupID = request.getParameter("GroupID");
			String message = null;
			Connection con = null;
			MySQL_ConnectionLevel3 mysql = new MySQL_ConnectionLevel3();
			con = mysql.connectDB();
			con.setAutoCommit(false);
			PreparedStatement res = con.prepareStatement(
					"select messageid, TotalVote,Message from chatApp.Votes where GroupName=? order by TotalVote desc");
			res.setString(1, GroupID);
			ResultSet rs;
			rs = res.executeQuery();
			con.commit();

			while (rs.next()) {
				String mID = rs.getString("messageid");
				String vote = rs.getString("TotalVote");
				String Message = rs.getString("Message");
				// result += "[ MessageID: '"+mID+"', TotalVotes: '"+vote+"' ], ";
				JSONObject jsonobject = new JSONObject();
				jsonobject.put("TotalVotes", vote);
				jsonobject.put("MessageID", mID);
				jsonobject.put("Message", Message);
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

}
