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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private List<String> facts;

  @Override
  public void init() {
      facts = new ArrayList<>();

      facts.add("I'm one of a triplet - and we are all females");
      facts.add("My big brothers are twins");
      facts.add("The names of my entire family start with 'א'");
      facts.add("I have played handball for 8 years, 13 games seasons");
 }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fact = facts.get((int) (Math.random() * facts.size()));

    response.setContentType("text/html; charset=UTF-8");
    response.getWriter().println(fact);
  }
}
