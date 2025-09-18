// document.addEventListener("DOMContentLoaded", async () => {
//     const orderTableBody = document.getElementById("order-items");
//     const token = localStorage.getItem("token");

//     if (!token) {
//         alert("Please log in first!");
//         window.location.href = "/login.html";
//         return;
//     }
    
//     try {
//         const response = await fetch("http://localhost:8080/api/orders/myOrders", {
//             method: "GET",
//             headers: {
//                 "Authorization": `Bearer ${token}`,
//                 "Content-Type": "application/json"
//             }
//         });
        
//         if (!response.ok) {
//             throw new Error("Failed to fetch orders");
//         }
        
//         const orders = await response.json();
//         orderTableBody.innerHTML = ""; 
        
//         if (orders.length === 0) {
//             orderTableBody.innerHTML = '<tr><td colspan="8" style="text-align:center; color:red;">No orders found.</td></tr>';
//             return;
//         }

//         let imageUrl = `http://localhost:8080/api/images/products/${product.image1}`;
        
//         orders.forEach(order => {
//             const row = document.createElement("tr");
//             row.innerHTML = `
//                 <td>${order.orderId}</td>
//                 <td>${new Date(order.createdAt).toLocaleString()}</td>
//                 <td><img src="${imageUrl}" alt="${product.name}" width="50" onerror="this.onerror=null; this.src='/images/placeholder.png';"></td>
//                 <td>${order.product.name}<br><small>${order.product.description}</small></td>
//                 <td>₹${order.product.disPrice}</td>
//                 <td>${order.quantity}</td>
//                 <td>₹${order.product.disPrice * order.quantity}</td>
//                 <td>${order.status}</td>
//             `;
//             orderTableBody.appendChild(row);
//         });
        
//     } catch (error) {
//         console.error("Error fetching orders:", error);
//         orderTableBody.innerHTML = '<tr><td colspan="8" style="text-align:center; color:red;">Error loading orders.</td></tr>';
//     }
// });


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
        let response = await fetch("http://localhost:8080/api/orders/myOrders", {
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

    orders.forEach(order => {
        let product = order.product;
        let imageUrl = `http://localhost:8080/api/images/products/${product.image1}`;
        let totalPrice = product.disPrice * order.quantity;
        let orderDate = new Date(order.createdAt).toLocaleString();

        let row = document.createElement("tr");
        row.innerHTML = `
            <td>${order.orderId}</td>
            <td>${orderDate}</td>
            <td><img src="${imageUrl}" alt="${product.name}"></td>
            <td><strong>${product.name}</strong><br>${product.description}</td>
            <td>₹${product.disPrice}</td>
            <td>${order.quantity}</td>
            <td>₹${totalPrice}</td>
            <td>${order.status}</td>
        `;
        orderTable.appendChild(row);
    });
}
