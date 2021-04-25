function getJWTToken() {
  return JSON.parse(sessionStorage.getItem('JWT')).accessToken;
}

function getRole() {
  return String(JSON.parse(sessionStorage.getItem('JWT')).userType);
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

function passwordValidation(password, password2) {
  if (!password || !password2) {
    alert('Empty input!');
    return false;
  }

  if (password !== password2) {
    alert('Passwords are not same!');
    return false;
  }

  let passRegex = new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[*.!@#$%^&(){}\\[\\]:;<>,.?~_+-=|\\/])[A-Za-z0-9*.!@#$%^&(){}\\[\\]:;<>,.?~_+-=|\\/]{8,}$');
  if (!passRegex.test(password)) {
    alert('Password must contain at least one lower, one capital letter, one number and one special character!\nPassword must have at least 8 characters!');
    return false;
  }
  return true;
}

function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m, key, value) {
    vars[key] = value;
  });
  return vars;
}