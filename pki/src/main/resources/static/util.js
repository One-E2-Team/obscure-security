function getJWTToken() {
  return JSON.parse(sessionStorage.getItem('JWT')).accessToken;
}

function getRole() {
  let token = JSON.parse(sessionStorage.getItem('JWT'));
  if (token !== null) {
    return String(JSON.parse(sessionStorage.getItem('JWT')).userType);
  }
  return null;
}

function dateToDDMMYYYY(dateString) {
  let date = new Date(dateString);
  return date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() + '.';
}

function sendHTTPRequest(method, url, params) {

  const promise = new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    if (params)
      xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader("Authorization", "Bearer " + getJWTToken());
    xhr.responseType = 'json';

    xhr.onreadystatechange = () => {
      if (xhr.status >= 400)
        reject(xhr.response);
      else if (xhr.readyState == 4)
        resolve(xhr.response);
    };

    xhr.send(params)
  });
  return promise;
}

function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m, key, value) {
    vars[key] = value;
  });
  return vars;
}