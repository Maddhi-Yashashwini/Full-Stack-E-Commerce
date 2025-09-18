document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const categoryId = params.get("category"); // Corrected to 'category'
    if (!categoryId) {
        console.error("Category ID is missing.");
        return;
    }

    fetchSubCategories(categoryId); // Pass categoryId to the function

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

/* <script>
    let dropdown = document.querySelector(".dropdown");
    let menu = dropdown.querySelector(".dropdown-menu");

    dropdown.addEventListener("click", function () {
        menu.classList.toggle("show"); // Toggles visibility
    });
</script>

<style>
    .dropdown-menu {
        display: none;
        background: lightgray;
        padding: 10px;
    }
    .show {
        display: block;
    }
</style> */

function fetchSubCategories(categoryId) {
    fetch(`http://localhost:8080/api/subcategories/${categoryId}`)
        .then(response => response.json())
        .then(subCategories => {
            console.log(subCategories); 
            const subCategoriesWrapper = document.getElementById("subcategories-wrapper");
            subCategories.forEach((subCategory, index) => {
                setTimeout(() => {
                    console.log('Creating card for:', subCategory.subCategoryName);
            
                    const subCategoryCard = document.createElement("div");
                    subCategoryCard.classList.add("subCategory-card");
                    
                    const subCategoryLink = document.createElement("a");
                    subCategoryLink.href = `ProductsIndex.html?category=${encodeURIComponent(categoryId)}&subCategory=${encodeURIComponent(subCategory.subCategoryId)}`;

                    subCategoryLink.classList.add("subCategory-link");
                    subCategoryLink.addEventListener("click", function(event) {
                        console.log("Clicked:", subCategory.subCategoryname);
                    });
                    

                    const subCategoryImage = document.createElement("img");
                    subCategoryImage.src = `${subCategory.subImage}`;
                    subCategoryImage.alt = subCategory.subCategoryName;
                    subCategoryImage.classList.add("subCategory-image");
            
                    const subCategoryName = document.createElement("p");
                    subCategoryName.classList.add("subCategory-name");
                    subCategoryName.textContent = subCategory.subCategoryName;
            
                    subCategoryLink.appendChild(subCategoryImage);
                    subCategoryLink.appendChild(subCategoryName);

                    subCategoryCard.appendChild(subCategoryLink);
            
                    subCategoriesWrapper.appendChild(subCategoryCard);
                }, index * 100); 
            });
            
        })
        .catch(error => {
            console.error("Error fetching subcategories:", error);
        });
}
