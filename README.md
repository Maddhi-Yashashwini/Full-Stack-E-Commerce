<h3>Table of Contents</h3>
<ol>
  <li>Overview</li>
  <li>Key Features</li>
  <li>Architecture</li>
  <li>Tech Stack</li>
  <li>Directory Structure</li>
  <li>Data Model</li>
  <li>API Endpoints (Summary)</li>
  <li>Security & Authentication</li>
  <li>Image Handling</li>
</ol>


<h3>1. Overview</h3>
<p>This repository is a learning-oriented full stack e‑commerce prototype built while I was exploring Spring Boot, REST API design, JWT-based authentication and lightweight frontend integration using plain vanilla HTML/CSS/JavaScript. It isn’t production polished, but it covers core flows: user registration/login authentication (JWT), role-based authorization (customer vs merchant), categories/subcategories, product catalog enrichment, dynamic image serving, file uploads for user profile, cart operations, and simple order tracking.</p>


<h3>2. Key Features</h3>
<ul>
  <li>User registration (automatic cart creation)</li>
  <li>JWT-based login & role propagation (CUSTOMER / MERCHANT)</li>
  <li>Category → Subcategory → Product drill-down</li>
  <li>Product image enrichment (URLs rewritten to accessible endpoints)</li>
  <li>Shopping cart add/remove/list operations</li>
  <li>Basic order management (merchant can view and update order status)</li>
  <li>Profile management (update profile data & upload user profile image)</li>
  <li>Modular image-serving controller</li>
  <li>CORS configuration for local frontend development</li>
</ul>


<h3>3. Architecture</h3>
<table role="table" aria-label="Layered design responsibilities" style="border-collapse:collapse; width:100%; max-width:800px;">
  <caption style="caption-side:top; text-align:left; padding:8px 0; font-weight:600;">
    Layered design
  </caption>
  <thead>
    <tr>
      <th scope="col" style="text-align:left; padding:8px; border-bottom:2px solid #ddd;">Layer</th>
      <th scope="col" style="text-align:left; padding:8px; border-bottom:2px solid #ddd;">Responsibility</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:8px; border-bottom:1px solid #eee;">Controller</td>
      <td style="padding:8px; border-bottom:1px solid #eee;">Expose REST endpoints; basic request mapping &amp; response shaping</td>
    </tr>
    <tr>
      <td style="padding:8px; border-bottom:1px solid #eee;">Service (partial – some logic still inside controllers)</td>
      <td style="padding:8px; border-bottom:1px solid #eee;">Business operations (image upload processing, cart logic)</td>
    </tr>
    <tr>
      <td style="padding:8px; border-bottom:1px solid #eee;">Repository (Spring Data JPA)</td>
      <td style="padding:8px; border-bottom:1px solid #eee;">Persistence and query generation</td>
    </tr>
    <tr>
      <td style="padding:8px; border-bottom:1px solid #eee;">Model / Entity</td>
      <td style="padding:8px; border-bottom:1px solid #eee;">Domain data mapped via JPA</td>
    </tr>
    <tr>
      <td style="padding:8px; border-bottom:1px solid #eee;">Security</td>
      <td style="padding:8px; border-bottom:1px solid #eee;">JWT filter + stateless session configuration</td>
    </tr>
    <tr>
      <td style="padding:8px;">Static Frontend</td>
      <td style="padding:8px;">Consumes APIs and renders UI</td>
    </tr>
  </tbody>
</table>


<h3>4. Tech Stack</h3>
<h4>Backend:</h4>
<ul>
  <li>Java 17</li>
  <li>Spring Boot 3.4.2</li>
  <li>spring-boot-starter-web</li>
  <li>spring-boot-starter-security</li>
  <li>spring-boot-starter-data-jpa</li>
  <li>MySQL (JDBC + JPA)</li>
  <li>JWT libraries: jjwt + java-jwt (only one is typically needed—future cleanup advised)</li>
  <li>JUnit / Mockito (tests scaffolded)</li>
  <li>Maven build</li>
</ul>
<h4>Frontend:</h4>
<ul>
  <li>Plain HTML5</li>
  <li>Vanilla JavaScript (fetch API)</li>
  <li>CSS (no framework)</li>
  <li>Local storage for auth token & username</li>
</ul>
<h4>Other:</h4>
<ul>
  <li>Static resource / file system storage for images</li>
  <li>JSON over HTTP</li>
  <li>CORS allowed origins for local development (5500 / 127.0.0.1)</li>
</ul>


<h3>5. Directory Structure</h3>
<pre>
Backend/
  src/main/java/com/example/Ecommerce/
    config/           # Security config, JWT config/filter
    controller/       # REST controllers
    model/            # Entities (User, Product, Cart, Order...) 
    model/order/      # Order + enum
    repo/             # Spring Data repositories
    service/          # Business logic services (CartService, ImageService, JwtService)
  src/main/resources/
    application.properties
    EcomImages/
      EcomCategoriesImages/
      EcomProductsImages/
      EcomSubCategoriesImages/
  pom.xml

Frontend/
  (HTML files: login, register, categories, products, subcategories, profile, cart, orders, merchant pages)
  (JS modules: register.js, login.js, ProductsScript.js, SubcategoriesScript.js, header.js, etc.)
  (CSS styles for each page)
</pre>


<h3>6. Data Model</h3>
<table border="1" cellspacing="0" cellpadding="6" style="border-collapse:collapse; width:100%; text-align:left;">
  <thead>
    <tr style="background-color:#f2f2f2;">
      <th>Entity</th>
      <th>Purpose</th>
      <th>Key Relationships</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>User</td>
      <td>Authenticated principal (role: CUSTOMER/MERCHANT)</td>
      <td>1–1 Cart; Many Orders</td>
    </tr>
    <tr>
      <td>Category</td>
      <td>Top-level grouping</td>
      <td>1–Many SubCategory; 1–Many Product</td>
    </tr>
    <tr>
      <td>SubCategory</td>
      <td>Nested grouping</td>
      <td>Many Products; Many-to-1 Category</td>
    </tr>
    <tr>
      <td>Product</td>
      <td>Sellable item with up to 4 images</td>
      <td>Many-to-1 Category/SubCategory</td>
    </tr>
    <tr>
      <td>Cart</td>
      <td>Per user; aggregates CartItems</td>
      <td>1–Many CartItem</td>
    </tr>
    <tr>
      <td>CartItem</td>
      <td>Product + quantity + discount details</td>
      <td>Many-to-1 Cart, Many-to-1 Product</td>
    </tr>
    <tr>
      <td>Order</td>
      <td>Purchase record incl. status + timestamp</td>
      <td>Many-to-1 User, Many-to-1 Product</td>
    </tr>
    <tr>
      <td>(OrderStatus enum)</td>
      <td>Tracks order lifecycle state</td>
      <td>Used by Order</td>
    </tr>
  </tbody>
</table>


<h3>7. API Endpoints (Selected Summary)</h3>
<p><b>Base:</b> <code>/api</code></p>
<h4>Public:</h4>
<ul>
  <li><b>POST</b> <code>/api/register</code> – Create user (role defaults to CUSTOMER &amp; cart created)</li>
  <li><b>POST</b> <code>/api/login</code> – Authenticate; returns JWT, role, username</li>
  <li><b>GET</b> <code>/api/categories</code> – List categories</li>
  <li><b>GET</b> <code>/api/subcategories/{categoryId}</code> – List subcategories</li>
  <li><b>GET</b> <code>/api/products</code> – List products (image1 URL rewritten)</li>
  <li><b>GET</b> <code>/api/products/{categoryId}/{subCategoryId}</code> – Filtered products</li>
  <li><b>GET</b> <code>/api/products/{categoryId}/{subCategoryId}/{productId}</code> – Product details</li>
  <li><b>GET</b> <code>/api/images/categories/{imageName}</code></li>
  <li><b>GET</b> <code>/api/images/subcategories/{imageName}</code></li>
  <li><b>GET</b> <code>/api/images/products/{imageName}</code></li>
  <li><b>GET</b> <code>/api/images/users/{imageName}</code></li>
</ul>
<h4>Authenticated (Bearer token):</h4>
<ul>
  <li><b>GET</b> <code>/api/user/profile</code></li>
  <li><b>PUT</b> <code>/api/user/profile/update</code></li>
  <li><b>PUT</b> <code>/api/user/profile/change-password</code></li>
  <li><b>POST</b> <code>/api/user/profile/upload-image</code></li>
  <li><b>GET</b> <code>/api/cart</code> – Fetch cart with items</li>
  <li><b>POST</b> <code>/api/cart/add/{productId}</code> – Add product to cart</li>
  <li><b>DELETE</b> <code>/api/cart/products/{productId}</code> – Remove product</li>
</ul>
<h4>Merchant (JWT with role MERCHANT):</h4>
<ul>
  <li><b>POST</b> <code>/api/merchant/addproduct</code> – Upload product + images</li>
  <li><b>GET</b> <code>/api/merchant/vieworders</code></li>
  <li><b>GET</b> <code>/api/merchant/customerdetails</code></li>
  <li><b>POST</b> <code>/api/merchant/updateorderstatus</code></li>
  <li><b>DELETE</b> <code>/api/merchant/deleteproduct</code></li>
  <li><b>POST</b> <code>/api/merchant/updateproduct</code></li>
  <li><b>GET</b> <code>/api/merchant/customer/{customerId}/order-count</code></li>
</ul>


<h3>8. Security and Authentication</h3>
<ul>
  <li>Passwords stored hashed using <code>BCryptPasswordEncoder</code>.</li>
  <li>Stateless sessions: JWT required for protected endpoints.</li>
  <li>JWT inserted in <code>Authorization: Bearer <token></code>.</li>
  <li>Role-based authorization (e.g., merchant endpoints).</li>
  <li>CORS configured to allow local development origins (5500).</li>
  <li>Improvement opportunity: unify scattered <code>@CrossOrigin</code> usages via global config.</li>
</ul>


<h3>9. Image Handling</h3>
<ul>
  <li>Images stored under <code>src/main/resources/EcomImages/...</code> (currently using hard-coded absolute paths in <code>ImageController</code>).</li>
  <li>Controller resolves filenames, sets proper content type using <code>Files.probeContentType</code></li>
  <li>Product / category endpoints rewrite stored image filenames into full URL strings like <code>http://localhost:8080/api/images/products/<filename></code>.</li>
</ul>
