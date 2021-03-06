async function register() {
  let email = document.getElementById('email').value;
  let country = document.getElementById('country').value;
  let state = document.getElementById('state').value;
  let locality = document.getElementById('locality').value;
  let organization = document.getElementById('organization').value;
  let organizationalUnit = document.getElementById('organizationalUnit').value;
  let userType = document.getElementById('type').value;
  let password = document.getElementById('password').value;
  let password2 = document.getElementById('password2').value;

  if (!email || !country || !state || !locality || !organization || !organizationalUnit || !password || !password2) {
    alert('Empty input!');
    return;
  }

  if (!emailValidation(email)) {
    alert('Invalid mail format!');
    return;
  }

  let passLower = password.toLowerCase();
  if (passLower.includes(country.toLowerCase()) || passLower.includes(state.toLowerCase()) || passLower.includes(locality.toLowerCase()) ||
    passLower.includes(organization.toLowerCase()) || passLower.includes(organizationalUnit.toLowerCase())) {
    alert('Password can\'t contain your personal data!');
    return;
  }

  if (!passwordValidation(password, password2)) {
    return;
  }

  let xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 201) {
      alert('Account successfully created!\nCheck email for validation.');
    } else if (this.readyState == 4 && this.status == 400) {
      let message = '';
      let errResponse = JSON.parse(this.response);
      if (errResponse == null) {
        alert('Password can\'t contain your personal data!');
        return;
      }
      for (let item in errResponse) {
        message += errResponse[item] + '\n';
      }
      alert(message);
    } else if (this.readyState == 4 && this.status == 401) {
      alert('Email already exists!');
    }
  };
  xhttp.open("POST", "/api/auth/register", true);
  xhttp.setRequestHeader("Content-type", "application/json");
  /*let encoder = new TextEncoder();
  let hash = await window.crypto.subtle.digest('SHA-512', encoder.encode(password));
  let hexPass = (Array.from(new Uint8Array(hash))).map(b => b.toString(16).padStart(2, '0')).join('');*/
  let data = {
    email: email,
    password: password,
    country: country,
    state: state,
    locality: locality,
    organization: organization,
    organizationalUnit: organizationalUnit,
    userType: userType
  };
  xhttp.send(JSON.stringify(data));
}