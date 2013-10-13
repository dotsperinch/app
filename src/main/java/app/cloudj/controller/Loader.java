package app.cloudj.controller;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Loader
 */
public class Loader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Loader() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String title = request.getParameter("tags[" + i + "][title]");
			if (title == null)
				break;
			String artist = request.getParameter("tags[" + i + "][artist]");
			if (artist == null)
				break;
			String album = request.getParameter("tags[" + i + "][artist]");
			if (album == null)
				break;
			String genre = request.getParameter("tags[" + i + "][artist]");
			if (genre == null)
				break;
			System.out.println("tags:" + title + " - " + artist + " - " + album
					+ " - " + genre + " - ");
		}

	}
}
