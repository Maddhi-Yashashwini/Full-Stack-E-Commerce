const API_BASE = "http://localhost:8080/api"; // Adjust here if backend port changes

document
  .getElementById("registerForm")
  .addEventListener("submit", async (event) => {
    event.preventDefault();

    const usernameEl = document.getElementById("username");
    const passwordEl = document.getElementById("password");
    const confirmPasswordEl = document.getElementById("confirmPassword");
    const emailEl = document.getElementById("email");
    const phoneEl = document.getElementById("phone");
    const addressEl = document.getElementById("address");

    if (passwordEl.value !== confirmPasswordEl.value) {
      alert("Passwords do not match. Please re-enter.");
      return;
    }

    const user = {
      username: usernameEl.value.trim(),
      password: passwordEl.value,
      email: emailEl.value.trim(),
      phone: phoneEl.value.trim(),
      address: addressEl.value.trim(),
    };

    try {
      const response = await fetch(`${API_BASE}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user),
      });

      const rawText = await response.text();
      let data = {};
      if (rawText) {
        try {
          data = JSON.parse(rawText);
        } catch {
          data = { raw: rawText };
        }
      }
      console.log("Register response:", response.status, data);

      if (response.status === 201) {
        alert(data.message || "Registration successful!");
        window.location.href = "login.html";
        return;
      }
      if (response.status === 409) {
        alert(data.message || "User already exists.");
        return;
      }
      alert(
        data.message ||
          data.error ||
          `Registration failed (status ${response.status}).`
      );
    } catch (err) {
      if (
        err.message.includes("Failed to fetch") ||
        err.message.includes("NetworkError")
      ) {
        alert(
          "Cannot connect to backend at " +
            API_BASE +
            ". Is the server running?"
        );
      } else {
        alert("Unexpected error: " + err.message);
      }
      console.error("Registration network/error:", err);
    }
  });

// Tip: Make sure you open this page via a local server (http://127.0.0.1:5500 or http://localhost:5500) not file://
