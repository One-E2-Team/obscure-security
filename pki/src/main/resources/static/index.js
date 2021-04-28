async function login() {
  if (!emailValidation(document.getElementById("username").value)) {
    alert('Invalid mail format!');
    return;
  }
  let xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      sessionStorage.setItem("JWT", JSON.stringify(JSON.parse(this.response)));
      window.location.href = '/certificates.html';
    } else if (this.readyState == 4 && this.status == 401) {
      alert('Email doesn\'t exist or isn\'t validated!');
    }
  };
  xhttp.open("POST", "/api/auth/login", true);
  xhttp.setRequestHeader("Content-type", "application/json");
  /*let encoder = new TextEncoder();
  let hash = await window.crypto.subtle.digest('SHA-512', encoder.encode(document.getElementById("password").value));
  let hexPass = (Array.from(new Uint8Array(hash))).map(b => b.toString(16).padStart(2, '0')).join('');*/
  let data = {
    email: document.getElementById("username").value,
    password: document.getElementById("password").value
  };
  xhttp.send(JSON.stringify(data));
}

function register() {
  window.location.href = '/register.html';
}

async function requestRecovery() {
  let email = document.getElementById('username').value;
  if (!emailValidation(email)) {
    alert('Invalid mail format!');
    return;
  }
  let xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      alert('Check email and change password!');
    } else if (this.readyState == 4 && this.status == 418) {
      alert('Bad request!');
    }
  };
  xhttp.open("POST", "/api/users/request-recovery", true);
  xhttp.setRequestHeader("Content-type", "application/json");
  xhttp.send(email);
}