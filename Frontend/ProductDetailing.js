// ProductDetailing.js

document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const categoryId = params.get("categoryId");
    const subCategoryId = params.get("subCategoryId");
    const productId = params.get("productId");

    if (!categoryId || !subCategoryId || !productId) {
        console.error("Missing required query parameters.");
        return;
    }

    fetchProductDetails(categoryId, subCategoryId, productId);

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

async function fetchProductDetails(categoryId, subCategoryId, productId) {
    try {
        let response = await fetch(`http://localhost:8080/api/products/${categoryId}/${subCategoryId}/${productId}`);
        if (!response.ok) throw new Error("Failed to fetch product details");

        let product = await response.json();

        document.getElementById("product-name").textContent = product.name;
        document.getElementById("product-description").textContent = product.description;
        document.getElementById("product-code").textContent = product.productId;
        document.getElementById("product-stock").textContent = product.stock > 0 ? product.stock : "Out of Stock";
        document.getElementById("original-price").textContent = `₹${product.price.toLocaleString()}`;
        document.getElementById("discounted-price").textContent = `₹${product.disPrice.toLocaleString()}`;
        document.getElementById("discount-percentage").textContent = `(${product.discount}% OFF)`;

        const productImage = document.getElementById("product-image");
        const thumbnailContainer = document.getElementById("thumbnail-container");

        const images = [product.image1, product.image2, product.image3, product.image4].filter(img => img && !img.includes("null"));

        if (images.length > 0) {
            productImage.src = images[0];

            thumbnailContainer.innerHTML = images
                .map((img, index) => `<img src="${img}" class="${index === 0 ? "active" : ""}" onclick="changeImage('${img}', this)">`)
                .join("");
        }

        const cartButton = document.querySelector(".cart-btn");
        cartButton.onclick = () => addToCart(product, cartButton);

        // Check cart status after loading the product details
        await updateCartButtonState(product.productId);

    } catch (error) {
        console.error("Error fetching product details:", error);
    }
}

function changeImage(imageSrc, element) {
    document.getElementById("product-image").src = imageSrc;
    document.querySelectorAll("#thumbnail-container img").forEach(img => img.classList.remove("active"));
    element.classList.add("active");
}

async function addToCart(product, button) {
    let token = localStorage.getItem("token");

    if (!token) {
        localStorage.setItem("lastVisitedPage", window.location.href);
        window.location.href = "/login.html";
        return;
    }

    try {
        let response = await fetch(`http://localhost:8080/api/cart/add/${product.productId}`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) throw new Error(await response.text());

        button.textContent = "✅ Added to Cart";
        button.disabled = true;

    } catch (error) {
        console.error("Error:", error);
    }
}

async function updateCartButtonState(productId) {
    let token = localStorage.getItem("token");
    if (!token) return;

    try {
        let response = await fetch("http://localhost:8080/api/cart", {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) throw new Error("Failed to fetch cart items");

        let cartData = await response.json();

        let cartProductIds = Array.isArray(cartData.cartItems) ? cartData.cartItems.map(item => item.product.productId) : [];

        let cartButton = document.querySelector(".cart-btn");
        if (cartProductIds.includes(parseInt(productId))) {
            cartButton.textContent = "✅ Added to Cart";
            cartButton.disabled = true;
        } else {
            cartButton.textContent = "🛒 Add to Cart";
            cartButton.disabled = false;
        }
    } catch (error) {
        console.error("Error fetching cart:", error);
    }
}
