var certificates = []

document.addEventListener("DOMContentLoaded", function(event) {
  startupFunction();
});

async function startupFunction() {
  let xhttp = new XMLHttpRequest();
  populateCertificates(certificates);
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      certificates = JSON.parse(this.response);
      populateCertificates(certificates);
    }
  };
  xhttp.open("GET", "/api/certificates/my", true);
  xhttp.send();
}

function populateCertificates(certificates) {
  let table = document.getElementById('certificates-table');
  for (let certificate of certificates) {
    let tr = document.createElement('tr');
    tr.appendChild(createTd(certificate.serialNumber));
    tr.appendChild(createTd(certificate.startDate));
    tr.appendChild(createTd(certificate.endDate));
    tr.appendChild(createTd(certificate.publicKey));
    tr.appendChild(createTd(certificate.commonName));
    tr.appendChild(createTd(certificate.organization));
    tr.appendChild(createTd(certificate.organizationalUnit));
    tr.appendChild(createSelectTd(certificate));
    let tdArea = document.createElement('td');
    let area = document.createElement('textarea');
    area.id = 'area-' + certificate.serialNumber;
    tdArea.appendChild(area);
    tr.appendChild(tdArea);
    tr.appendChild(createTd(certificate.type));
    let revoked = certificate.revoked;
    tr.appendChild(createTd(revoked));
    if (!revoked) {
      tr.appendChild(createButtonTd(certificate.serialNumber, 'Revoke'));
    }
    table.appendChild(tr);
  }

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

function createButtonTd(id, text) {
  let tdButton = document.createElement('td');
  let button = document.createElement('button');
  button.type = "submit";
  button.id = id;
  button.innerText = text;
  tdButton.appendChild(button);
  return tdButton;
}
