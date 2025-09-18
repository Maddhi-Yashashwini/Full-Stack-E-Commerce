document.addEventListener("DOMContentLoaded", function () {
    let token = localStorage.getItem("token");

    if (!token) {
        alert("Please log in to view your cart.");
        window.location.href = "/Frontend/login.html";
        return;
    }

    fetchCart();

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

async function fetchCart() {
    let token = localStorage.getItem("token");

    try {
        let response = await fetch("http://localhost:8080/api/cart", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) throw new Error("Failed to fetch cart");

        let cartData = await response.json();
        console.log("Cart Data:", cartData);

        let cartItemsContainer = document.getElementById("cart-items");
        let cartTable = document.getElementById("cart-table");
        let orderSummary = document.querySelector(".order-summary");
        let emptyCartMsg = document.getElementById("empty-cart-msg");

        cartItemsContainer.innerHTML = "";
        let totalAmount = 0;

        if (!cartData.cartItems || cartData.cartItems.length === 0) {
            cartTable.style.display = "none";
            orderSummary.style.display = "none";
            emptyCartMsg.style.display = "block";
            return;
        }

        cartTable.style.display = "table";
        orderSummary.style.display = "block";
        emptyCartMsg.style.display = "none";

        cartData.cartItems.forEach(item => {
            let product = item.product;
            let totalPrice = item.quantity * product.disPrice;
            totalAmount += totalPrice;

            let imageUrl = `http://localhost:8080/api/images/products/${product.image1}`;

            let row = `
                <tr>
                    <td><img src="${imageUrl}" alt="${product.name}" width="50" onerror="this.onerror=null; this.src='/images/placeholder.png';"></td>
                    <td>${product.name} <br> ${product.description}</td>
                    <td>₹${product.disPrice.toLocaleString()}</td>
                    <td>${item.quantity}</td>
                    <td>₹${totalPrice.toLocaleString()}</td>
                    <td><button class="delete-btn" onclick="removeFromCart(${product.productId})">🗑 Delete</button></td>
                </tr>
            `;
            cartItemsContainer.innerHTML += row;
        });

        document.getElementById("total-amount").textContent = totalAmount.toLocaleString();

    } catch (error) {
        console.error("Error fetching cart:", error);
    }
}

async function removeFromCart(productId) {
    let token = localStorage.getItem("token");

    try {
        let response = await fetch(`http://localhost:8080/api/cart/products/${productId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) throw new Error("Failed to delete item");

        alert("Item removed from cart!");
        fetchCart();

    } catch (error) {
        console.error("Error removing item:", error);
    }
}

// ✅ Checkout functionality
document.getElementById("checkout-btn").addEventListener("click", async function () {
    let token = localStorage.getItem("token");

    if (!token) {
        alert("User not authenticated. Please log in.");
        return;
    }

    try {
        let response = await fetch("http://localhost:8080/api/orders/checkout", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) throw new Error("Checkout failed");

        alert("Order placed successfully!");
        window.location.href = "/Frontend/OrderSuccess.html";

    } catch (error) {
        console.error("Error during checkout:", error);
        alert("Checkout failed. Please try again.");
    }
});
