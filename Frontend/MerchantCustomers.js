document.addEventListener("DOMContentLoaded", function () {
    let token = localStorage.getItem("token");

    if (!token) {
        alert("Please log in to view customer details.");
        window.location.href = "/login.html";
        return;
    }

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

    fetchCustomers();

});

async function fetchCustomers() {
    let token = localStorage.getItem("token");
    try {
        let response = await fetch("http://localhost:8080/api/merchant/customerdetails", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) throw new Error("Failed to fetch customers");
        
        let customers = await response.json();
        displayCustomers(customers);
    } catch (error) {
        console.error("Error fetching customers:", error);
    }
}

function displayCustomers(customers) {
    let tableBody = document.getElementById("customers-items");
    tableBody.innerHTML = "";

    customers.forEach(async (customer) => {
        let totalOrders = await fetchTotalOrders(customer.userId);
        console.log(totalOrders);
        let imageUrl = `http://localhost:8080/api/images/users/${customer.userImage}`;
        
        let row = `
            <tr>
                <td><img src="${imageUrl}" alt="User Image" width="50" height="50"></td>
                <td>${customer.userId}</td>
                <td>${customer.username}</td>
                <td>${customer.email}</td>
                <td>${customer.phone}</td>
                <td>${customer.address}</td>
                <td>${totalOrders}</td>
            </tr>
        `;
        
        tableBody.innerHTML += row;
    });
}

async function fetchTotalOrders(userId) {
    console.log("Fetching total orders for user:", userId);
    try {
        let response = await fetch(`http://localhost:8080/api/merchant/customer/${userId}/order-count`, {  
            method: "GET",
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            }
        });

        console.log("HTTP Response Status:", response.status);

        if (!response.ok) throw new Error("Failed to fetch total orders");
        
        let data = await response.json();
        console.log("API Response Data:", data);  // 🔍 Debug response

        return data || 0;
    } catch (error) {
        console.error("Error fetching total orders:", error);
        return 0;
    }
}


