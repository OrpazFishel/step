// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handle comments data. */
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Load the comments from the Datastore.
        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);
        int maxNumberOfComments = Integer.parseInt(request.getParameter("limit"));
        
        List<Comment> comments = new ArrayList<>();
        for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(maxNumberOfComments))) {
            long id = entity.getKey().getId();
            String name = (String) entity.getProperty("name");
            String text = (String) entity.getProperty("text");
            long timestamp = (long) entity.getProperty("timestamp");

            Comment comment = new Comment(id, name, text, timestamp);
            comments.add(comment);

            if (comments.size() >= maxNumberOfComments) {
                break;
            }
        }

        Gson gson = new Gson();
        String jsonComment = gson.toJson(comments);

        response.setContentType("application/json;");
        response.getWriter().println(jsonComment);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String name = getParameter(request, "name", "");
    String text = getParameter(request, "comment", "");
    long timestamp = System.currentTimeMillis();

    // Respond with the result.
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("text", text);
    commentEntity.setProperty("timestamp", timestamp);

    //Store the data.
    datastore.put(commentEntity);
    // Redirect back to the HTML page.
    response.sendRedirect("/comments.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}