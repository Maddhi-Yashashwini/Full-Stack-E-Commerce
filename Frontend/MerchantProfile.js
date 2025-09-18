/* customerProfile.js */
const token = localStorage.getItem("token");

if (!token) {
    alert("Authentication error! Please log in again.");
    window.location.href = "/Frontend/login.html";
}

document.addEventListener("DOMContentLoaded", () => {
    // Dropdown menu functionality
    const dropdown = document.querySelector(".dropdown");

    if (dropdown) {
        dropdown.addEventListener("click", function (event) {
            event.preventDefault();
            const menu = this.querySelector(".dropdown-menu");
            menu.style.display = menu.style.display === "block" ? "none" : "block";
        });

        document.addEventListener("click", function (event) {
            if (!dropdown.contains(event.target)) {
                dropdown.querySelector(".dropdown-menu").style.display = "none";
            }
        });
    }
    fetchProfile(token);

    document.getElementById("update-profile-btn").addEventListener("click", () => {
        console.log("Update profile button clicked");
        document.getElementById("update-profile-modal").classList.remove("hidden");
    });

    document.getElementById("close-update-modal").addEventListener("click", () => {
        console.log("closing Update profile");
        document.getElementById("update-profile-modal").classList.add("hidden");
    });

    document.getElementById("change-password-btn").addEventListener("click", () => {
        console.log("change password button clicked");
        document.getElementById("change-password-modal").classList.remove("hidden");
    });

    document.getElementById("close-password-modal").addEventListener("click", () => {
        console.log("closing change password");
        document.getElementById("change-password-modal").classList.add("hidden");
    });

    document.getElementById("upload-photo-btn").addEventListener("click", () => {
        document.getElementById("upload-photo-input").click();
    });

    document.getElementById("add-products-btn").addEventListener("click", function () {
        window.location.href = "/Frontend/AddProducts.html";
    });

    document.getElementById("upload-photo-input").addEventListener("change", (event) => uploadPhoto(event, token));
    document.getElementById("update-profile-form").addEventListener("submit", (event) => updateProfile(event, token));
    document.getElementById("change-password-form").addEventListener("submit", (event) => changePassword(event, token));
    document.getElementById("view-Merchantorders-btn").addEventListener("click", viewMerchantOrders);
    document.getElementById("view-customers-btn").addEventListener("click", viewCustomers);
    document.getElementById("logout-btn").addEventListener("click", logout);
});

function fetchProfile(token) {
    fetch("http://localhost:8080/api/user/profile", {
        headers: { "Authorization": `Bearer ${token}` }
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("username").textContent = data.username;
        document.getElementById("role").textContent = data.role;
        document.getElementById("user-id").textContent = data.userId;
        document.getElementById("email").textContent = data.email;
        document.getElementById("phone").textContent = data.phone;
        document.getElementById("address").textContent = data.address;
        document.getElementById("profile-image").src = `http://localhost:8080/api/images/users/${data.userImage}`;

    });
}

function updateProfile(event, token) {
    event.preventDefault();
    const updatedData = {
        email: document.getElementById("new-email").value,
        phone: document.getElementById("new-phone").value,
        address: document.getElementById("new-address").value
    };

    fetch("http://localhost:8080/api/user/profile/update", {
        method: "PUT",
        headers: { 
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(updatedData)
    }).then(() => location.reload());
}

function changePassword(event, token) {
    event.preventDefault();
    const passwordData = {
        oldPassword: document.getElementById("old-password").value,
        newPassword: document.getElementById("new-password").value
    };

    fetch("http://localhost:8080/api/user/profile/change-password", {
        method: "PUT",
        headers: { 
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(passwordData)
    }).then(() => alert("Password updated successfully!"));
}

function uploadPhoto(event, token) {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("image", file);

    fetch("http://localhost:8080/api/user/profile/upload-image", {
        method: "POST",
        headers: { "Authorization": `Bearer ${token}` },
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("profile-image").src = `http://localhost:8080/api/images/users/${data.userImage}`;
    })
    .catch(error => console.error("Error uploading image:", error));
}

function viewMerchantOrders() {
    window.location.href = "/Frontend/MerchantOrders.html";
}

function viewCustomers(){
    window.location.href = "/Frontend/MerchantCustomers.html";
}


function logout() {
    if (confirm("Do you want to logout?")) {
        localStorage.removeItem("username");
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        alert("Logged out successfully!");
        window.location.href= "HomepageIndex.html";
    }
}
