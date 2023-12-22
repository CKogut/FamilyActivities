const API_URL = `http://localhost:8080`;
// alert("test");

// uses FETCH web api
// https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
//
//
function fetchActivitiesData() {
  fetch(`${API_URL}/api/activities`)
    .then(res => {
      return res.json();
    })
    .then(data => {
      showActivityList(data);
    })
    .catch(error => {
      console.log(`Error Fetching data : ${error}`);
      document.getElementById('activities').innerHTML = 'Error Loading Data';
    });
}

// takes a UNIX integer date, and produces a prettier human string
function dateOf(date) {
  const milliseconds = date * 1000; // 1575909015000
  const dateObject = new Date(milliseconds);
  const humanDateFormat = dateObject.toLocaleString(); //2019-12-9 10:30:15
  return humanDateFormat;
}

function showActivityList(data) {
  // the data parameter will be a JS array of JS objects
  // this uses a combination of "HTML building" DOM methods (the document createElements) and
  // simple string interpolation (see the 'a' tag on title)
  // both are valid ways of building the html.
  const activities = document.getElementById('activities');
  const list = document.createDocumentFragment();

  data.map(function (activity) {
    let div = document.createElement('div');
    let title = document.createElement('h3');

    title.innerHTML = `<a href="details.html?activityid=${activity.id}">${activity.description}</a>`;

    div.appendChild(title);
    list.appendChild(div);
  });

  activities.appendChild(list);
}

function handlePage() {
  console.log('load all activities');
  fetchActivitiesData();
}

handlePage();
