const API_URL = `http://localhost:8080`;
// alert("test");

// uses FETCH web api
// https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
function fetchActivitiesData() {
  fetch(`${API_URL}/api/activities`)
    .then(res => {
      return res.json();
    })
    .then(data => {
      showSuggestion(data);
    })
    .catch(error => {
      console.log(`Error Fetching data : ${error}`);
      document.getElementById('activities').innerHTML = 'Error Loading Data';
    });
}

// Function to pick a random object from the JSON data
function getRandomObject(data) {
  var randomIndex = Math.floor(Math.random() * data.length);
  return data[randomIndex];
}

function showSuggestion(data) {
  // the data parameter will be a JS array of JS objects
  // this uses a combination of "HTML building" DOM methods (the document createElements) and
  // simple string interpolation (see the 'a' tag on title)
  // both are valid ways of building the html.
  const activities = document.getElementById('activities');
  const list = document.createDocumentFragment();

  let div = document.createElement('div');
  let title = document.createElement('h3');

  var randomObject = getRandomObject(data);
  console.log(randomObject);

  title.innerHTML = `<a href="details.html?activityid=${randomObject.id}">${randomObject.description}</a>`;

  div.appendChild(title);
  list.appendChild(div);

  activities.appendChild(list);
}

function handlePage() {
  console.log('load all activities');
  fetchActivitiesData();
}

handlePage();
