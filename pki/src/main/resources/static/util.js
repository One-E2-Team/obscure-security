function getJWTToken() {
  return JSON.parse(sessionStorage.getItem('JWT')).accessToken;
}

function getRole() {
  return String(JSON.parse(sessionStorage.getItem('JWT')).userType);
}

function dateToDDMMYYYY(dateString) {
  let date = new Date(dateString);
  return date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() + '.';
}
