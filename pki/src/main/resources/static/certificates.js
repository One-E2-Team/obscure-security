var certificates = []

document.addEventListener("DOMContentLoaded", function(event) {
  startupFunction();
});

async function startupFunction() {
  sendHTTPRequest("GET", "/api/certificates/my")
    .then(response => {
      certificates = response;
      populateCertificates(certificates);
    })
}



function populateCertificates(certificates) {
  if (getRole() === 'ADMINISTRATOR' || getRole() === 'INTERMEDIARY_CA') {
    document.getElementById('add-new').style.display = 'block';
  } else {
    document.getElementById('add-new').style.display = 'none';
  }
  let table = document.getElementById('certificates-table');
  for (let certificate of certificates) {
    let tr = document.createElement('tr');
    tr.appendChild(createTd(certificate.serialNumber));
    tr.appendChild(createTd(dateToDDMMYYYY(certificate.startDate)));
    tr.appendChild(createTd(dateToDDMMYYYY(certificate.endDate)));
    tr.appendChild(createTd(certificate.subject.commonName));
    tr.appendChild(createTd(certificate.subject.userSubject.organization));
    tr.appendChild(createTd(certificate.subject.userSubject.organizationalUnit));
    tr.appendChild(createSelectTd(certificate));
    let tdArea = document.createElement('td');
    let area = document.createElement('textarea');
    area.id = 'area-' + certificate.serialNumber;
    area.disabled = true;
    tdArea.appendChild(area);
    tr.appendChild(tdArea);
    tr.appendChild(createTd(certificate.type));
    let revoked = certificate.revoked;
    tr.appendChild(createTd(revoked));
    if (!revoked && getRole() === 'ADMINISTRATOR') {
      tr.appendChild(createButtonTd(certificate.serialNumber, 'Revoke', revoke));
      if (certificate.type != "END")
        tr.appendChild(createButtonTd(certificate.serialNumber + "c", 'Create', createCertificate));
      else
        tr.appendChild(document.createElement('td'));
    } else {
      tr.appendChild(document.createElement('td'));
      tr.appendChild(document.createElement('td'));
    }
    //tr.appendChild(createButtonTd(certificate.serialNumber, 'Download', download));
    let td = document.createElement('td');
    let a = document.createElement('a');
    a.href = "/api/certificates/download/" + certificate.serialNumber;
    a.innerText = 'Download';
    a.setAttribute('download', certificate.serialNumber + '.cer');
    td.appendChild(a);
    tr.appendChild(td);
    table.appendChild(tr);
  }

}

function createCertificate() {
  let serialNumber = event.target.id
  serialNumber = serialNumber.substring(0, serialNumber.length - 1);
  window.location.href = '/newCertificate.html?serial-number=' + serialNumber;
}

function createSelectTd(certificate) {
  let tdSelect = document.createElement('td');
  let select = document.createElement('select');
  select.id = certificate.serialNumber;
  let defaultOption = document.createElement('option');
  defaultOption.value = '-';
  defaultOption.innerText = '-';
  defaultOption.selected = true;
  select.appendChild(defaultOption);
  for (let extension of certificate.extensions) {
    let option = document.createElement('option');
    let name = extension.field.name;
    option.value = name;
    option.innerText = name;
    select.appendChild(option);
  }
  select.onchange = function() { selectedExtension(); };
  tdSelect.appendChild(select);
  return tdSelect;
}

function selectedExtension() {
  let selection = event.target;
  let selectedName = selection.value;
  let serialNum = selection.id;
  let areaId = 'area-' + serialNum;
  document.getElementById(areaId).value = '';
  for (let certificate of certificates) {
    if (certificate.serialNumber == serialNum) {
      for (let extension of certificate.extensions) {
        if (extension.field.name == selectedName) {
          document.getElementById(areaId).value = extension.value;
        }
      }
    }
  }
}

function createTd(text) {
  let td = document.createElement('td');
  td.innerText = text;
  return td;
}

async function revoke() {
  let selection = event.target;
  let serialNum = selection.id;
  sendHTTPRequest("POST", "/api/certificates/revoke/" + serialNum).then(location.reload());
}

async function download() {
  let serialNumber = event.target.id;
  serialNumber = serialNumber.substring(0, serialNumber.length - 1);
  sendHTTPRequest("GET", "/api/certificates/download/" + serialNumber);
}

function createButtonTd(id, text, fun) {
  let tdButton = document.createElement('td');
  let button = document.createElement('button');
  button.id = id;
  button.innerText = text;
  button.addEventListener("click", function() { fun() });
  tdButton.appendChild(button);
  return tdButton;
}