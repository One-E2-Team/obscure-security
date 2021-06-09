function passwordValidation(password, password2) {
  if (!password || !password2) {
    alert('Empty input!');
    return false;
  }

  if (password !== password2) {
    alert('Passwords are not same!');
    return false;
  }

  let passRegex = new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[*.!@#$%^&(){}\\[\\]:;<>,.?~_+\\-=|\\/])[A-Za-z0-9*.!@#$%^&(){}\\[\\]:;<>,.?~_+\\-=|\\/]{8,}$');
  if (!passRegex.test(password)) {
    alert('Password must contain at least one lower, one capital letter, one number and one special character!\nPassword must have at least 8 characters!');
    return false;
  }

  if (passwordEntersMostCommonPatern(password)) {
    alert('Your password enters in most common password pattern. Please, enter different password.');
    return false;
  }

  return true;
}

function emailValidation(email) {
  let mailRegex = new RegExp(/^([a-zA-Z0-9]+\.?)*[a-zA-Z0-9]@[a-z0-9]+(\.[a-z]{2,3})+$/);
  return mailRegex.test(email);
}

function certificateCommonNameValidation(commonName) {
  let regex = new RegExp('([a-zA-Z0-9]+\.?)*[a-zA-Z0-9]');
  return regex.test(commonName);
}

function passwordEntersMostCommonPatern(password) {
  let regex = new RegExp(/^[A-Z][a-z]+[0-9]+[!@#$%^&*)(+=._-]+$/);
  return regex.test(password);
}