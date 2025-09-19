document.getElementById('loginForm').addEventListener('submit', function (event) {
  event.preventDefault();

  const user = {
    username: document.getElementById('username').value,
    password: document.getElementById('password').value,
  };

  fetch('http://localhost:8080/api/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(user)
  })
  .then(response => {
    if (!response.ok) {
      return response.json().then(err => { throw new Error(err.error || "Login failed"); });
    }
    return response.json();
  })
  .then(data => {
    if (data.token) {
      alert('Login successful!');
      localStorage.setItem('token', data.token);
      localStorage.setItem('username', data.username);
      localStorage.setItem('role', data.role);

      // Retrieve last visited page
      const lastVisitedPage = localStorage.getItem("lastVisitedPage");
      localStorage.removeItem("lastVisitedPage"); // Clear it after redirect

      // Redirect to last visited page or homepage
      window.location.href = lastVisitedPage ? lastVisitedPage : "Homepageindex.html";
    } else {
      throw new Error("Unexpected error. No token received.");
    }
  })
  .catch(error => {
    console.error('Login failed:', error);
    alert(error.message);
  });
});

