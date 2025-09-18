document.getElementById('registerForm').addEventListener('submit', function (event) {
  event.preventDefault();

  const password = document.getElementById('password').value;
  const confirmPassword = document.getElementById('confirmPassword').value;

  if (password !== confirmPassword) {
    alert('Passwords do not match. Please re-enter.');
    return;
  }

  const user = {
    username: document.getElementById('username').value,
    password: password,
    email: document.getElementById('email').value,
    phone: document.getElementById('phone').value,
    address: document.getElementById('address').value,
  };

  fetch('http://localhost:8080/api/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(user)
  })
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      console.log("Response Data:", data);
      alert('Registration successful!');
      window.location.href = 'login.html';
    } else {
      alert('Registration failed: ' + data.message);
    }
  })
  .catch(error => {
    console.error('There was an error registering!', error);
  });
});