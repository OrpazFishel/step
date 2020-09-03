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

const books = ["Assassin's Apprentice", 
               "Elantris",
               "Game of Thrones",
               "Graceling",
               "The Name of the Wind",
               "The Way of Kings"];

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
  const imageIndex = Math.floor(Math.random() * books.length); 
  const imgUrl = "/images/" + books[imageIndex] + ".jpg";

  const imgElement = document.createElement('img');
  imgElement.src = imgUrl;

  const imageContainer = document.getElementById('random-image-container');
  // Remove the previous image.
  imageContainer.innerHTML = '';
  imageContainer.appendChild(imgElement);
}

/** Generates the options of the survey.*/
function createSurveyOptions() {
  const optionsContainer = document.getElementsByName('book')[0];
  books.forEach(bookName => {
    optionsContainer.appendChild(new Option(bookName, bookName))
  });
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


    const chart = new google.visualization.PieChart(
        document.getElementById('chart-container'));
    chart.draw(data, {'title': 'Favorite Books',
                      'width':600,
                      'height':500});
  });
}

/** Creates a map and adds it to the page. */
function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 31.771959, lng: 35.217018}, zoom: 7});
  
  // Cities where I had games
  var cities = {TelAviv: {lat: 32.109333, lng: 34.855499},
                Ashdod: {lat: 31.801447, lng: 34.643497},
                Herzliya: {lat: 32.166313, lng: 34.843311},
                Eilat: {lat: 29.55805, lng: 34.94821},
                Holon: {lat: 32.01034, lng: 34.77918},
                Netanya: {lat: 32.33291, lng: 34.85992},
                PetahTiqwa: {lat: 32.08707, lng: 34.88747},
                RamatGan: {lat: 32.08227, lng: 34.81065},
                RishonLeziyyon: {lat: 31.97102, lng: 34.78939},
                KiryayOno: {lat: 32.0549, lng: 34.8589},
                NessZiona: {lat: 31.9321, lng: 34.8013},
                Savyon: {lat: 32.0456, lng: 34.8766},
                Arad: {lat: 31.275165566, lng: 35.12166618}};
  Object.values(cities).forEach(location => new google.maps.Marker({position: location, map: map}));
}