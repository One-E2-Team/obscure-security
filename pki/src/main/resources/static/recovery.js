async function recovery() {
  let password = document.getElementById('password').value;
  let password2 = document.getElementById('password2').value;
  if (!passwordValidation(password, password2)) {
    return;
  }

  let xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      alert('Password successfully changed!');
      window.location.href = '/';
    } else if (this.readyState == 4 && this.status == 418) {
      alert('Bad request!');
      return;
    }
  };
  xhttp.open("PUT", "/api/users/recovery", true);
  xhttp.setRequestHeader("Content-type", "application/json");
  let id = getUrlVars()['id']
  let str = getUrlVars()['str']
  let encoder = new TextEncoder();
  let hash = await window.crypto.subtle.digest('SHA-512', encoder.encode(password));
  let hexPass = (Array.from(new Uint8Array(hash))).map(b => b.toString(16).padStart(2, '0')).join('');
  let data = {
    id: id,
    password: hexPass,
    uuid: str
  };
  xhttp.send(JSON.stringify(data));
}