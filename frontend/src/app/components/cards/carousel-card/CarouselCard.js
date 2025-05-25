export default class CarouselCard {
    constructor(product) {
        this.product = product;
    }

    getHtml(index, isActive) {
        return `
             <label for="carousel-item-${index + 1}" 
             class="card ${isActive ? 'active' : index === 1 ? 'hidden-right' 
                                    : index === 2 ? 'hidden-left' 
                                    : 'hidden-middle'} card-container" id="card-${index + 1}">
                 <div class="card-info-main">
                     <div class="new-card">
                         <p class="new-text">New</p>
                     </div>
                     <a href="http://localhost:3000/dish/${this.product.id}" class="hero-image-container">
                         <img class="card_image" src="${this.product.image}" alt="${this.product.name}"/>
                     </a>
                     <div class="card-info-details">
                         <div class="card-info">
                             <span class="card-category">${this.product.category}</span>
                             <div class="card-info-author">
                                 <i class="fa-solid fa-user"></i>
                                 <span class="card-category">${this.product.author.username}</span>
                             </div>
                         </div>
                         <h2 class="card-title">${this.product.name}</h2>
                     </div>
                 </div>
                 <div class="card-button-container"></div>
             </label>
        `;
    }
}
