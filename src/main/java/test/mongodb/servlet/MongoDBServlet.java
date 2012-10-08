package test.mongodb.servlet;
/* JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

/**
 * Do some mongoDB tutorial stuff as a demo
 * http://www.mongodb.org/display/DOCS/Java+Tutorial
 * 
 * @author Scott stark (sstark@redhat.com) (C) 2011 Red Hat Inc.
 * @version $Revision:$
 */
@WebServlet(name = "MongoDBServlet")
public class MongoDBServlet extends HttpServlet {
    private Mongo mongo;
    private DB mongoDB;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
	String host = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
        String sport = System.getenv("OPENSHIFT_MONGODB_DB_PORT");
        String db = System.getenv("OPENSHIFT_APP_NAME");
        if(db == null)
            db = "mydb";
        String user = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
        String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
        int port = Integer.decode(sport);

        try {
            mongo = new Mongo(host , port);
        } catch (UnknownHostException e) {
            throw new ServletException("Failed to access Mongo server", e);
        }
        mongoDB = mongo.getDB(db);
        if(mongoDB.authenticate(user, password.toCharArray()) == false) {
            throw new ServletException("Failed to authenticate against db: "+db);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        handleCmd(cmd, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        handleCmd(cmd, response);
    }
    protected void handleCmd(String cmd, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        if(cmd == null) {
            // Initialize the db with some of the tutorial objects
            tutorial(pw);
        }
        else if(cmd.equalsIgnoreCase("query")) {

        }
        else {
            pw.format("<h1>Error, on %s</h1>\nNo support for handling the command exists yet", cmd);
        }
        pw.flush();
    }

    protected void tutorial(PrintWriter pw) {
        try {
            mongoDB.requestStart();
            pw.format("<h1>Tutorial Objects Added to DB</h1>\n");
            DBCollection coll = mongoDB.getCollection("testCollection");
            BasicDBObject doc = new BasicDBObject();

            doc.put("name", "MongoDB");
            doc.put("type", "database");
            doc.put("count", 1);

            BasicDBObject info = new BasicDBObject();

            info.put("x", 203);
            info.put("y", 102);

            doc.put("info", info);

            coll.insert(doc);
            DBObject myDoc = coll.findOne();
            pw.format("<h2>testCollection</h2><pre>%s</pre>\n", myDoc.toString());

            for (int i=0; i < 100; i++) {
                coll.insert(new BasicDBObject().append("i", i));
            }

            BasicDBObject query = new BasicDBObject();
            query.put("i", 71);
            DBCursor cur = coll.find(query);
            pw.format("<h2>100 i objects, #71:</h2><pre>%s</pre>\n", cur.next());

            coll.createIndex(new BasicDBObject("i", 1));  // create index on "i", ascending
        } finally {
            mongoDB.requestDone();
        }
    }

    protected void handleQuery(PrintWriter pw, String i) {
        try {
            mongoDB.requestStart();
            DBCollection coll = mongoDB.getCollection("testCollection");
            BasicDBObject query = new BasicDBObject();
            query.put("i", 71);
            DBCursor cur = coll.find(query);
            if(cur.count() == 1)
                pw.format("<h1>>testCollection.%s result: </h2><pre>%s</pre>\n", cur.next());
            else
                pw.format("<h1>>testCollection.%s result</h2>No matches found!\n");
        } finally {
            mongoDB.requestDone();
        }
    }
}
