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
    document.getElementById("type").addEventListener("change", function() { disablePublicKey() });
    document.getElementById("ch-generate-key").addEventListener("change", function() {
        if (document.getElementById("ch-generate-key").checked) {
            generatePublicKey();
        }
        disablePublicKey()
    });
    document.getElementById("update-text-btn").addEventListener("click", function() { updateText(selectedTextId) });
    document.getElementById("user").addEventListener("change", function() {
        let email = document.getElementById("user").value;
        showInTextArea(getUserByEmail(email).userSubject)
    })
});

function generatePublicKey() {
    let xhr = new XMLHttpRequest();

    xhr.open("POST", "/api/certificates/issuerpubkeys");
    xhr.setRequestHeader("Authorization", "Bearer " + getJWTToken());
    xhr.responseType = 'json';
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            pubKey = this.response[0];
            console.log(pubKey.publicKey)
            document.getElementById('public-key').value = pubKey.publicKey
            console.log(this.response);
        }
    };
    xhr.send(document.getElementById('user').value);
}

function getUserByEmail(email) {
    for (user of users) {
        if (user.email == email) return user;
    }
    return null;
}

function showInTextArea(object) {
    let info = ""
    for (let property of Object.keys(object)) {
        info += property + ": " + object[property] + "\n";
    }
    document.getElementById("ta-description").innerText = info;
}


async function collectExtensions() {
    let xhr = new XMLHttpRequest();

    xhr.open("GET", "/api/extensions");
    xhr.setRequestHeader("Authorization", "Bearer " + getJWTToken());
    xhr.responseType = 'json';
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            extensions = this.response;
            populateExtensions();
        }
    };
    xhr.send();
}

async function getIssuer() {
    let url = window.location.href;
    let paramsUrl = url.split('?')[1]
    let params = paramsUrl.split('&')
    let issuerSerialId = params[0].split('=')[1];

    let xhr = new XMLHttpRequest();

    xhr.open("GET", "/api/certificates/user/" + issuerSerialId);
    xhr.setRequestHeader("Authorization", "Bearer " + getJWTToken());
    xhr.responseType = 'json';
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            issuer = this.response;
            issuer.id = issuerSerialId
            let parent = document.getElementById('parent');
            parent.innerText = issuer.id;
            parent.addEventListener("click", function() {
                showInTextArea(issuer);
            })
        }
    };
    xhr.send();
}

async function getUsers() {
    let xhr = new XMLHttpRequest();

    xhr.open("GET", "/api/users");
    xhr.setRequestHeader("Authorization", "Bearer " + getJWTToken());
    xhr.responseType = 'json';
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            users = this.response;
            console.log(users);
            populateUsers();
        }
    };
    xhr.send();
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

function disablePublicKey() {
    document.getElementById("public-key").disabled = publicKeyShouldBeDisabled();
}

function publicKeyShouldBeDisabled() {
    return document.getElementById("type").value == "ROOT" || document.getElementById("ch-generate-key").checked
}

function createCertificate() {
    let pubkey = publicKeyShouldBeDisabled() ? "" : document.getElementById("public-key").value
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
    console.log(request);
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