var extensions = []
var selectedExtensions = []
var selectedTextId = ""
var issuer = {}
var users = []
var pubKey = {}


document.addEventListener("DOMContentLoaded", function(event) {
  collectExtensions();
  getIssuer();
  getUsers();

  document.getElementById("create-btn").addEventListener("click", function() { createCertificate() });
  document.getElementById("type").addEventListener("change", function() { changeType() });
  document.getElementById("ch-generate-key").addEventListener("change", function() {
    if (document.getElementById("ch-generate-key").checked) {
      generatePublicKey();
    }
    if (isType("END"))
      document.getElementById("public-key").disabled = document.getElementById("ch-generate-key").checked ? true : false;
  });
  document.getElementById("ch-generate-key").addEventListener("change", function() {
    if (document.getElementById("ch-generate-key").checked == false)
      document.getElementById("generated-keys").innerHTML = "";
  })
  document.getElementById("update-text-btn").addEventListener("click", function() { updateText(selectedTextId) });
  document.getElementById("user").addEventListener("change", function() {
    let email = document.getElementById("user").value;
    showInTextArea(getUserByEmail(email).userSubject)
  })
});

async function generatePublicKey() {
  HTTPRequest("POST", "/api/certificates/issuerpubkeys", document.getElementById('user').value).then(response => populatePublicKeys(response));
}

function populatePublicKeys(keys) {
  let keySelect = document.getElementById("generated-keys");
  for (let key of keys) {
    let option = document.createElement('option');
    option.value = key.publicKey;
    option.innerText = key.validUntil;
    keySelect.appendChild(option);
  }
}

function getUserByEmail(email) {
  for (user of users) {
    if (user.email == email) return user;
  }
  return null;
}

function setElementVisibility(element, visible) {
  if (visible)
    element.style.display = "block";
  else
    element.style.display = "none";;
}

function showInTextArea(object) {
  let info = ""
  for (let property of Object.keys(object)) {
    info += property + ": " + object[property] + "\n";
  }
  document.getElementById("ta-description").innerText = info;
}


async function collectExtensions() {

  HTTPRequest("GET", "/api/extensions")
    .then(response => {
      extensions = response;
      populateExtensions();
    })
}

async function getIssuer() {
  let url = window.location.href;
  let paramsUrl = url.split('?')[1]
  let params = paramsUrl.split('&')
  let issuerSerialId = params[0].split('=')[1];



  HTTPRequest("GET", "/api/certificates/user/" + issuerSerialId)
    .then(response => {
      issuer = response;
      issuer.id = issuerSerialId
      let parent = document.getElementById('parent');
      parent.innerText = issuer.id;
      parent.addEventListener("click", function() {
        showInTextArea(issuer);
      })
    });


}

async function getUsers() {

  HTTPRequest("GET", "/api/users")
    .then(response => {
      users = response;
      console.log(users);
      populateUsers();
    });
}

function populateUsers() {
  let userSelect = document.getElementById("user");
  for (let user of users) {
    let option = document.createElement('option');
    option.value = user.email;
    option.innerText = user.email;
    userSelect.appendChild(option);
  }
}


function changeType() {
  let pk = document.getElementById("public-key")
  pk.value = "";
  pk.disabled = !isType("END");
  setElementVisibility(document.getElementById("checks"), !isType("ROOT"));
  setElementVisibility(document.getElementById("generated-keys"), !isType("ROOT"));
  document.getElementById("ch-generate-key").checked = false;
  document.getElementById("generated-keys").innerHTML = "";
}

function isType(type) {
  return document.getElementById("type").value == type
}

function createCertificate() {
  let pubkey;
  if (isType("ROOT"))
    pubkey = "";
  else if (isType("INTERMEDIATE")) {
    pubkey = document.getElementById("ch-generate-key").checked ? document.getElementById("generated-keys").value : "";
  } else {
    pubkey = document.getElementById("ch-generate-key").checked ? document.getElementById("generated-keys").value : document.getElementById("public-key").value;
  }
  let request = {
    type: document.getElementById('type').value,
    startDate: document.getElementById('start-date').value,
    endDate: document.getElementById('end-date').value,
    email: document.getElementById('user').value,
    commonName: document.getElementById("common-name").value,
    publicKey: pubkey,
    issuerSerialNumber: issuer.id,
    extensions: getUsedExtensions()
  }

  HTTPRequest("POST", "/api/certificates/create", request)
    .then(response => (console.log(response)));
}

function getUsedExtensions() {
  let result = []
  for (let ext of selectedExtensions) {
    let el = {
      field: {
        name: ext.text
      },
      value: document.getElementById("ext-data" + ext.id).value
    }
    result.push(el)
  }
  return result;
}


function populateExtensions() {
  let table = document.getElementById('availableExtensions');
  for (let ext of extensions) {
    let tr = document.createElement('tr');
    let td = document.createElement('td');
    td.innerText = ext.name;
    tr.appendChild(td);
    table.appendChild(tr);
  }

  table.addEventListener('click', function(item) {
    selectRow(item)
  });

}


function selectRow(item) {

  var row = item.path[1];
  let selectedRow;
  for (var j = 0; j < row.cells.length; j++) {

    selectedRow = {
      id: row.rowIndex,
      text: row.cells[j].innerHTML
    }
  };
  if (row.classList.contains('highlight')) {
    row.classList.remove('highlight');
    removeExtensionToCertificate(selectedRow.id);
    selectedExtensions = removeElementFromList(selectedExtensions, selectedRow);
  } else {
    row.classList.add('highlight');
    selectedExtensions.push(selectedRow)
    addExtensionToCertificate(selectedRow);
  }
  console.log(selectedExtensions)
}

function addExtensionToCertificate(item) {
  let table = document.getElementById('certificate-form');
  let tr = document.createElement('tr');
  tr.setAttribute("id", "extension-" + item.id)

  let td1 = document.createElement('td');
  td1.innerText = item.text;
  tr.appendChild(td1);

  let td2 = document.createElement('td');
  td2.appendChild(createDisabledInputTextWithClickEvent(item.id))
  tr.appendChild(td2);
  table.appendChild(tr);
}

function createDisabledInputTextWithClickEvent(id) {
  let input = document.createElement('input');
  input.type = "text"
  input.setAttribute("id", "ext-data" + id);
  input.addEventListener("click", function() { addTextToTextArea("ext-data" + id); })
  return input;
}

function addTextToTextArea(elementId) {
  let el = document.getElementById(elementId);
  selectedTextId = elementId;
  document.getElementById("ta-description").innerText = el.value;
}

function updateText(selectedTextId) {
  let el = document.getElementById(selectedTextId);
  el.value = document.getElementById("ta-description").value;
}

function removeExtensionToCertificate(id) {
  var elem = document.getElementById('extension-' + id);
  elem.parentNode.removeChild(elem);
}


function removeElementFromList(list, item) {
  for (var i = list.length - 1; i >= 0; i--) {
    if (list[i].id === item.id) {
      list.splice(i, 1);
    }
  }
  return list;
}