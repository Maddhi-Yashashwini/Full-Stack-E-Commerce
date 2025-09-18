document.addEventListener("DOMContentLoaded", function () {
    let token = localStorage.getItem("token");

    if (!token) {
        alert("Please log in to view your orders.");
        window.location.href = "/login.html";
        return;
    }

    fetchOrders();

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
});

async function fetchOrders() {
    let token = localStorage.getItem("token");

    try {
        let response = await fetch("http://localhost:8080/api/merchant/vieworders", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) throw new Error("Failed to fetch orders");
        
        let orders = await response.json();
        displayOrders(orders);
    }
    catch (error) {
        console.error("Error fetching orders:", error);
    }
}

function displayOrders(orders) {
    let orderTable = document.getElementById("order-items");
    orderTable.innerHTML = "";

    if (orders.length === 0) {
        orderTable.innerHTML = `<tr><td colspan="9">No orders found.</td></tr>`;
        return;
    }

    orders.forEach(order => {
        let product = order.product;
        let customer = order.user;
        let imageUrl = product.image1;
        let totalPrice = product.disPrice * order.quantity;
        let orderDate = new Date(order.createdAt).toLocaleString();

        let row = document.createElement("tr");
        row.innerHTML = `
            <td>${order.orderId}</td>
            <td>${orderDate}</td>
            <td>${customer.username}</td>
            <td><img src="${imageUrl}" alt="${product.name}"></td>
            <td><strong>${product.name}</strong><br>${product.description}</td>
            <td>₹${product.disPrice}</td>
            <td>${order.quantity}</td>
            <td>₹${totalPrice}</td>
            <td>${customer.address}</td>
            <td>
                <select onchange="updateOrderStatus(${order.orderId}, this.value)">
                    <option value="Pending" ${order.status === "Pending" ? "selected" : ""}>Pending</option>
                    <option value="Shipped" ${order.status === "Shipped" ? "selected" : ""}>Shipped</option>
                    <option value="Delivered" ${order.status === "Delivered" ? "selected" : ""}>Delivered</option>
                    <option value="Cancelled" ${order.status === "Cancelled" ? "selected" : ""}>Cancelled</option>
                </select>
            </td>
        `;
        orderTable.appendChild(row);
    });
}

async function updateOrderStatus(orderId, newStatus) {
    let token = localStorage.getItem("token");
    
    try {
        let response = await fetch("http://localhost:8080/api/merchant/updateorderstatus", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                orderId: orderId,
                status: newStatus
            })
        });

        let result = await response.json();
        if (!response.ok) {
            throw new Error(data.message || "Failed to update order status");
        }

        alert(result.message);
    }
    catch (error) {
        console.error("Error updating order status:", error);
    }
}
