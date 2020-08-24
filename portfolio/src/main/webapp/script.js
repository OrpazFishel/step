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

function getRandomFact() {
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

function getRandomComment() {
    comments = fetch('/comment').then(response => response.json()).then((comments) => {
        for(index = 0; index < comments.length; index++) {
            const commentElement = document.getElementById("comment-container");
            commentElement.appendChild(createListElement(comments[index]));
        }
    });
}

function createListElement(comment) {
  const liElement = document.createElement('li');
  liElement.innerText = comment;
  return liElement;
}