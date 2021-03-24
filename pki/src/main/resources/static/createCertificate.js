var extensions = []
var selectedExtension = []
var selectedTextId = ""

document.addEventListener("DOMContentLoaded", function(event) {
    startupFunction(); 
    document.getElementById("create-btn").addEventListener("click",function() {createCertificate()});
    document.getElementById("update-text-btn").addEventListener("click", function(){updateText(selectedTextId)})
  });

function addExtensions(){
    console.log(selectedExtension)
}

function startupFunction(){
    let xhr = new XMLHttpRequest();

    xhr.open("GET", "/api/extensions");
    xhr.setRequestHeader("Authorization", "Bearer " + getJWTToken());
    xhr.responseType='json';
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            extensions = this.response;
            populateExtensions();
        }
      };
    xhr.send();
}

function createCertificate(){
    let request = {
        type : document.getElementById('type').value,
        parent: document.getElementById('parent').innerText,
        startDate: document.getElementById('start-date').value,
        endDate : document.getElementById('end-date').value,
    }
    console.log(request);
}


function populateExtensions(){
    let table = document.getElementById('availableExtensions');
    for(let ext of extensions){
        let tr = document.createElement('tr');
        let td = document.createElement('td');
        td.innerText = ext.name;
        tr.appendChild(td);
        table.appendChild(tr);
    }

    table.addEventListener('click', function (item) {
        selectRow(item)
    });

}


function selectRow(item){
  
        var row = item.path[1];
        let selectedRow ;
        for (var j = 0; j < row.cells.length; j++) {

            selectedRow = {
                id : row.rowIndex,
                text: row.cells[j].innerHTML}
        };
        console.log(selectedRow)
        if (row.classList.contains('highlight')){
            row.classList.remove('highlight');
            removeExtensionToCertificate(selectedRow.id);
            
        }
        else{           
            row.classList.add('highlight');
            addExtensionToCertificate(selectedRow);
        }
}

function addExtensionToCertificate(item){
    let table = document.getElementById('certificate-form');
    let tr = document.createElement('tr');
    tr.setAttribute("id","extension-"+item.id)
    
    let td1 = document.createElement('td');
    td1.innerText = item.text;
    tr.appendChild(td1);

    let td2 = document.createElement('td');
    td2.appendChild(createDisabledInputTextWithClickEvent(item.id))
    tr.appendChild(td2);
    table.appendChild(tr);
}

function createDisabledInputTextWithClickEvent(id){
    let input = document.createElement('input');
    input.type="text"
    input.setAttribute("id","ext-data"+id);
    input.addEventListener("click", function(){ addTextToTextArea("ext-data"+id) ;})
    return input;
}

function addTextToTextArea(elementId){
    let el = document.getElementById(elementId);
    selectedTextId = elementId;
    document.getElementById("ta-description").innerText = el.value;
}

function updateText(selectedTextId){
    let el = document.getElementById(selectedTextId);
    el.value = document.getElementById("ta-description").value;
}

function removeExtensionToCertificate(id){
    var elem = document.getElementById('extension-'+id);
    elem.parentNode.removeChild(elem);
}

function getJWTToken() {
    return JSON.parse(sessionStorage.getItem('JWT')).accessToken;
}