var extensions = []
var selectedExtension = ""

document.addEventListener("DOMContentLoaded", function(event) {
  startupFunction();
});

function addExtension() {
  alert(selectedExtension)
}

function startupFunction() {
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

  for (var j = 0; j < row.cells.length; j++) {

    selectedExtension += row.cells[j].innerHTML;
  }

  if (row.classList.contains('highlight')) {
    row.classList.remove('highlight');
    selectedExtension = "";
  } else {

    row.classList.add('highlight');
    document.getElementById("btn-add-cert").addEventListener("click", alert(selectedExtension));
  }
}
