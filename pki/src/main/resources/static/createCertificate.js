var selectedExtensions = []
var pubKey = {}
var users = []

document.addEventListener("DOMContentLoaded", function() {
  if (getRole() !== null && (getRole() === 'ADMINISTRATOR' || getRole() === 'INTERMEDIARY_CA')) {
    document.getElementById('main-div').style.display = 'block';
    document.getElementById('error-div').style.display = 'none';
  } else {
    document.getElementById('main-div').style.display = 'none';
    document.getElementById('error-div').style.display = 'block';
    return;
  }
  _initializeExtensions();
  _initializeUsers();
  _initializeParent().then(issuerId => document.getElementById("create-btn").addEventListener("click", function() { createCertificate(issuerId) }));
});

function createCertificate(issuerId) {
  let commonName = document.getElementById("common-name").value;
  if (!certificateCommonNameValidation(commonName)) {
    alert("Common name contains characters thas are not allowed");
    return;
  }
  let request = {
    type: document.getElementById('type').value,
    startDate: document.getElementById('start-date').value,
    endDate: document.getElementById('end-date').value,
    email: document.getElementById('user').value,
    commonName: commonName,
    publicKey: _getChosenPublicKey(),
    issuerSerialNumber: issuerId,
    extensions: getUsedExtensions()
  }

  sendHTTPRequest("POST", "/api/certificates/create", JSON.stringify(request));
}

function chRecommendKeysChange() {
  let el = document.getElementById("ch-generate-key");
  if (el.checked)
    _generatePublicKey();
  else
    document.getElementById("generated-keys").innerHTML = "";

  if (isType("END"))
    document.getElementById("public-key").disabled = el.checked ? true : false;
}

function changeType() {
  let pk = document.getElementById("public-key");
  pk.value = "";
  pk.disabled = !isType("END");
  _setElementVisibility(document.getElementById("checks"), !isType("ROOT"));
  _setElementVisibility(document.getElementById("generated-keys"), !isType("ROOT"));
  document.getElementById("ch-generate-key").checked = false;
  chRecommendKeysChange();
}

function isType(type) {
  return document.getElementById("type").value == type
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

async function _initializeExtensions() {
  sendHTTPRequest("GET", "/api/extensions")
    .then(response => _populateExtensions(response));
}

async function _initializeParent() {
  const promise = new Promise((resolve) => {
    let issuerId = _getIssuerId();
    if (issuerId == null)
      resolve(null);
    else {
      sendHTTPRequest("GET", "/api/certificates/user/" + issuerId)
        .then(response => {
          issuer = response;
          issuer.id = issuerId;
          let parent = document.getElementById('parent');
          parent.innerText = issuer.id;
          parent.addEventListener("click", function() { _showObjectToTextArea(issuer); })
          resolve(issuer.id);
        });
    }
  })
  return promise;
}

async function _initializeUsers() {
  sendHTTPRequest("GET", "/api/users")
    .then(response => {
      users = response;
      _populateUsers(users);
    });
}

function _getChosenPublicKey() {
  if (isType("INTERMEDIATE"))
    return document.getElementById("ch-generate-key").checked ? document.getElementById("generated-keys").value : "";
  if (isType("END"))
    return document.getElementById("ch-generate-key").checked ? document.getElementById("generated-keys").value : document.getElementById("public-key").value;
  return "";
}

function _populateExtensions(extensions) {
  let table = document.getElementById('availableExtensions');
  for (let ext of extensions) {
    let tr = document.createElement('tr');
    let td = document.createElement('td');
    td.innerText = ext.name;
    tr.appendChild(td);
    table.appendChild(tr);
  }

  table.addEventListener('click', function(item) {
    _selectRow(item)
  });

}

function _populateUsers(users) {
  for (let user of users) {
    let option = document.createElement('option');
    option.value = user.email;
    option.innerText = user.email;
    document.getElementById("user").appendChild(option);
  }
}

function selectUser() {
  let email = document.getElementById("user").value;
  user = _getUserByEmail(email);
  if (user != null)
    _showObjectToTextArea(user.userSubject);
}

function _getUserByEmail(email) {
  for (user of users) {
    if (user.email == email) return user;
  }
  return null;
}

function _generatePublicKey() {
  let mail = document.getElementById('user').value;
  sendHTTPRequest("POST", "/api/certificates/issuerpubkeys", mail).then(response => _populatePublicKeys(response));
}

function _populatePublicKeys(keys) {
  let keySelect = document.getElementById("generated-keys");
  for (let key of keys) {
    let option = document.createElement('option');
    option.value = key.publicKey;
    option.innerText = key.validUntil;
    keySelect.appendChild(option);
  }
}

function _setElementVisibility(element, visible) {
  if (visible)
    element.style.display = "block";
  else
    element.style.display = "none";;
}

function _showObjectToTextArea(object) {
  document.getElementById("ta-description").innerHTML = _generateOutputFromObject(object);
}

function _generateOutputFromObject(object, childNumber = 0) {
  let info = "";

  if (object == undefined || object == null) return "";

  for (let property of Object.keys(object)) {
    if (typeof object[property] !== 'string' && Object.keys(object[property]).length > 0) {
      info += insertWhiteSpaces(childNumber) + property + ": " + "&#13;&#10;" + _generateOutputFromObject(object[property], childNumber + 1);
      continue;
    }
    info += insertWhiteSpaces(childNumber) + property + ": " + object[property] + "&#13;&#10;";
  }

  return removeTags(info);
}

function insertWhiteSpaces(numOfWhitespaces = 0) {
  whitespaces = [];
  while (numOfWhitespaces--) whitespaces.push("   ");
  return whitespaces.join("");
}


function _selectRow(item) {

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
    _removeExtensionFromCertificate(selectedRow.id);
    selectedExtensions = _removeElementFromList(selectedExtensions, selectedRow);
  } else {
    row.classList.add('highlight');
    selectedExtensions.push(selectedRow)
    _addExtensionToCertificate(selectedRow);
  }
  console.log(selectedExtensions)
}

function _addExtensionToCertificate(item) {
  let table = document.getElementById('certificate-body');
  let tr = document.createElement('tr');
  tr.setAttribute("id", "extension-" + item.id)

  let tdExtension = document.createElement('td');
  tdExtension.innerText = item.text;
  tr.appendChild(tdExtension);

  let tdValue = document.createElement('td');
  tdValue.appendChild(_createInputTextLinkedToTextArea(item.id))
  tr.appendChild(tdValue);
  table.appendChild(tr);
}

function _createInputTextLinkedToTextArea(id) {
  let input = document.createElement('input');
  input.type = "text"
  input.setAttribute("id", "ext-data" + id);
  ['click', 'change'].forEach(evt =>
    input.addEventListener(evt, function() { _showInputValueToTextArea("ext-data" + id); })
  );
  return input;
}

function _showInputValueToTextArea(elementId = "") {
  let el = document.getElementById(elementId);
  document.getElementById("ta-description").innerHTML = el.value;
}

function _removeExtensionFromCertificate(id) {
  var elem = document.getElementById('extension-' + id);
  elem.parentNode.removeChild(elem);
}

function _removeElementFromList(list, item) {
  for (var i = list.length - 1; i >= 0; i--) {
    if (list[i].id === item.id) {
      list.splice(i, 1);
    }
  }
  return list;
}

function _getIssuerId() {
  issuerId = getUrlVars()['serial-number']
  if (issuerId === undefined)
    return null;
  return issuerId
}