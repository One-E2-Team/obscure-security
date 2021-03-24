var extensions = []

document.addEventListener("DOMContentLoaded", function(event) {
    alert("djes")
    startupFunction();
  });

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


function populateExtensions(){
    let table = document.getElementById('extensions-table');
    for(let ext of extensions){
        let tr = document.createElement('tr');
        let td = document.createElement('td');
        td.innerText = ext.name;
        tr.appendChild(td);
        table.appendChild(tr);
    }

}

document.getElementById('extensions-table')
            .addEventListener('click', function (item) {
  
                var row = item.path[1];
  
                var row_value = "";
  
                for (var j = 0; j < row.cells.length; j++) {
  
                    row_value += row.cells[j].innerHTML;
                    row_value += " | ";
                }
  
                alert(row_value);
  
                // Toggle the highlight
                if (row.classList.contains('highlight'))
                    row.classList.remove('highlight');
                else
                    row.classList.add('highlight');
            });

function getJWTToken() {
    return JSON.parse(sessionStorage.getItem('JWT')).accessToken;
}