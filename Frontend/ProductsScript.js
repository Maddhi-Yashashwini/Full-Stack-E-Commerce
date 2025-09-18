document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const categoryId = params.get("category"); 
    const subCategoryId = params.get("subCategory"); 

    if (!categoryId || !subCategoryId) {
        console.error("Category ID or SubCategory ID is missing.");
        return;
    }

    fetchProducts(categoryId, subCategoryId); 

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

function fetchProducts(categoryId, subCategoryId) {
    fetch(`http://localhost:8080/api/products/${categoryId}/${subCategoryId}`)
        .then(response => response.json())
        .then(products => {
            console.log(products);
            const productGrid = document.getElementById("productGrid"); // Fix: Match HTML ID

            if (!productGrid) {
                console.error("Error: Element with ID 'productGrid' not found.");
                return;
            }

            const defaultImage = "https://via.placeholder.com/150"; // Fallback image if null

            productGrid.innerHTML = products.map(product => `
                <div class="product-card" data-product-id="${product.productId}" data-category-id="${categoryId}" data-subcategory-id="${subCategoryId}">
                    <span class="discount-badge">${product.discount}% OFF</span>
                    
                    <!-- Wishlist Icon -->
                    <i class="fas fa-heart wishlist-icon"></i>
                    
                    <img src="${product.image1 ? product.image1 : defaultImage}" alt="${product.name}" class="product-image">
                    <div class="product-name">${product.name}</div>
                    <div class="product-description">${product.description}</div>
                    <div class="product-pricing">
                        <span class="original-price">₹${product.price.toLocaleString()}</span>
                        <span class="discounted-price">₹${product.disPrice.toLocaleString()}</span>
                    </div>
                </div>
            `).join("");

            // Wishlist functionality
            document.querySelectorAll(".wishlist-icon").forEach(icon => {
                icon.addEventListener("click", (event) => {
                    event.stopPropagation(); // Prevent redirection when clicking wishlist icon
                    icon.classList.toggle("active");
                });
            });

            // Click event for product card redirection
            document.querySelectorAll(".product-card").forEach(card => {
                card.addEventListener("click", function () {
                    const productId = this.getAttribute("data-product-id");
                    const categoryId = this.getAttribute("data-category-id");
                    const subCategoryId = this.getAttribute("data-subcategory-id");

                    window.location.href = `ProductDetailing.html?categoryId=${categoryId}&subCategoryId=${subCategoryId}&productId=${productId}`;
                });
            });
        })
        .catch(error => console.error("Error fetching products:", error));
}

