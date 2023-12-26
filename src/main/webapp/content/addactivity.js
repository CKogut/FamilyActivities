const API_URL = `http://localhost:8080`;

function doPostOfForm(event) {
  event.preventDefault();
  const formData = new FormData(event.target);

  var object = {};
  formData.forEach(function (value, key) {
    object[key] = value;
  });
  postJSON(object);

  document.getElementById('addactivityform').reset();
}

async function postJSON(data) {
  // data here is a JS Object (keys/values) "JSON"
  try {
    const response = await fetch(`${API_URL}/api/activities`, {
      method: 'POST', // or 'PUT'
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    const result = await response.json();
    console.log('Success:', result);
  } catch (error) {
    console.error('Error:', error);
  }
}

const form = document.getElementById('addactivityform');
form.addEventListener('submit', doPostOfForm);
