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

/**
 * Adds a random fact about myself to the page.
 */
function displayRandomFact() {
    fetch('/data').then(response => response.text()).then((quote) => {
    document.getElementById('fact-container').innerText = quote;
  });
}

/**
 * Generates a URL for a random image in the images directory and adds an img
 * element with that URL to the page.
 */
function randomizeImage() {
  const images =
      ["/images/assassin'sApprentice.jpg", 
      "/images/elantris.jpg",
      "/images/gameOfThrones.jpg",
      "/images/graceling.jpg",
      "/images/theNameOfTheWind.jpg",
      "/images/theWayOfKings.jpg"];

  const imageIndex = Math.floor(Math.random() * images.length); 
  const imgUrl = images[imageIndex];

  const imgElement = document.createElement('img');
  imgElement.src = imgUrl;

  const imageContainer = document.getElementById('random-image-container');
  // Remove the previous image.
  imageContainer.innerHTML = '';
  imageContainer.appendChild(imgElement);
}

/** Fetches comments from the servers and adds them to the DOM. */
function displayComments() {
  var limit = document.getElementById("limit").value;
  fetch('/comment?limit=' + limit).then(response => response.json()).then((comments) => {
    const commentElement = document.getElementById("comment-container");
    commentElement.innerHTML = "";
    comments.forEach((comment) => {
      commentElement.appendChild(createListElement(comment));
      })
    });
}

/** Creates an <li> element containing text. */
function createListElement(comment) {
  const liElement = document.createElement('li');
  liElement.innerText = comment.name + ": " + comment.text;
  return liElement;
}

/** Delete all the comments. */
function deleteComments() {
  fetch('/delete-comments');
  const commentElement = document.getElementById("comment-container");
  commentElement.innerHTML= "";
}

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

/** Fetches book data and uses it to create a chart. */
function drawChart() {
  fetch('/book-data').then(response => response.json())
  .then((bookVotes) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Book');
    data.addColumn('number', 'Votes');
    Object.keys(bookVotes).forEach((book) => {
      data.addRow([book, bookVotes[book]]);
    });

    const options = {
      'title': 'Favorite Books',
      'width':600,
      'height':500
    };

    const chart = new google.visualization.PieChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
  });
}