async function login() {
  let xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      sessionStorage.setItem("JWT", JSON.stringify(JSON.parse(this.response)));
      window.location.href = '/certificates.html';
    }
  };
  xhttp.open("POST", "/api/auth/login", true);
  xhttp.setRequestHeader("Content-type", "application/json");
  let encoder = new TextEncoder();
  let hash = await window.crypto.subtle.digest('SHA-512', encoder.encode(document.getElementById("password").value));
  let hexPass = (Array.from(new Uint8Array(hash))).map(b => b.toString(16).padStart(2, '0')).join('');
  let data = {
    email: document.getElementById("username").value,
    password: hexPass
  };
  xhttp.send(JSON.stringify(data));
}

function register() {
  window.location.href = '/register.html';
}

async function requestRecovery() {
  let email = document.getElementById('username').value;
  if (!email) {
    alert('Enter your email!');
    return;
  }
  let xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      alert('Check email and change password!');
    }
  };
  xhttp.open("POST", "/api/users/request-recovery", true);
  xhttp.setRequestHeader("Content-type", "application/json");
  xhttp.send(email);
}