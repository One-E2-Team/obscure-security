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
    }
  };
  xhttp.open("PUT", "/api/users/recovery", true);
  xhttp.setRequestHeader("Content-type", "application/json");
  let url = window.location.href;
  let paramsUrl = url.split('?')[1];
  let paramethers = paramsUrl.split('&');
  let id = paramethers[0].split('=')[1];
  let str = paramethers[1].split('=')[1];
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