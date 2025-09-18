document.addEventListener("DOMContentLoaded", function () {
    const username = localStorage.getItem("username");
    const role = localStorage.getItem("role");
    const token = localStorage.getItem("token");
    const authLink = document.getElementById("auth-link");
    const cartLink = document.getElementById("cart-link");
    const userDropdown = document.getElementById("user-dropdown"); // User dropdown container

    if (username && token) {
        // ✅ User is logged in: Show username with dropdown
        authLink.textContent = username;
        authLink.href = "#";
        authLink.classList.add("dropdown-toggle");

        // Create Dropdown Items Dynamically
        userDropdown.innerHTML = ""; // Clear existing content

        const profileItem = document.createElement("li");
        const logoutItem = document.createElement("li");

        const profileLink = document.createElement("a");
        profileLink.textContent = "Profile";
        profileLink.href = role === "CUSTOMER" ? "CustomerProfile.html" : "MerchantProfile.html";

        const logoutLink = document.createElement("a");
        logoutLink.textContent = "Logout";
        logoutLink.href = "#";
        logoutLink.addEventListener("click", function () {
            if (confirm("Do you want to logout?")) {
                localStorage.removeItem("username");
                localStorage.removeItem("token");
                localStorage.removeItem("role");
                alert("Logged out successfully!");
                window.location.reload();
            }
        });

        profileItem.appendChild(profileLink);
        logoutItem.appendChild(logoutLink);
        userDropdown.appendChild(profileItem);
        userDropdown.appendChild(logoutItem);

        // ✅ Toggle dropdown visibility on click
        authLink.addEventListener("click", function (event) {
            event.preventDefault();
            event.stopPropagation(); // Prevent closing immediately
            userDropdown.classList.toggle("show"); // Toggle dropdown visibility
        });

        // ✅ Close dropdown when clicking outside
        document.addEventListener("click", function (event) {
            if (!authLink.contains(event.target) && !userDropdown.contains(event.target)) {
                userDropdown.classList.remove("show");
            }
        });

        cartLink.href = "/cart.html"; // Allow cart access

    } else {
        // ❌ User is NOT logged in: Show "Login/Register"
        authLink.textContent = "Login/Register";
        authLink.href = "/login.html";

        cartLink.href = "#";
        cartLink.addEventListener("click", function () {
            alert("Please log in to access your cart.");
            window.location.href = "/login.html";
        });
    }
});

