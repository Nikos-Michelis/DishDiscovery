import HttpService from "../../../services/HttpService.js";

export default class DishCard {
    constructor(dish) {
        this.dish = dish;
        this.httpService = new HttpService();
    }

    getHtml() {
        const wishlistHtml =  `    
            <article class="card-container card-wishlist">
                <div class="card-info-main">
                    <a href="/" class="hero-image-container">
                        <img class="card_image" src="${this.dish.image}" alt="${this.dish.name}"/>
                    </a>
                    <div class="card-info-details">
                        <div class="card-info">
                            <span class="card-category">${this.dish.category}</span>
                            <div class="card-info-author">
                                <i class="fa-solid fa-user"></i>
                                <span class="card-category">${this.dish.author.username}</span>
                            </div>
                        </div>
                        <h2 class="card-title">${this.dish.name}</h2>
                        <div class="card-description">
                            <p class="card-text">Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                                Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                                It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</p>
                        </div>
                    </div>
                    <div class="card-btn-tools">
                        <a class="remove-item item-${this.dish.id}" href="#"><span><i class="fa-solid fa-xmark"></i></span></a>
                    </div>
                </div>
            </article>`;

        setTimeout(() => {
            this.addEventListeners();
        }, 0);
        return wishlistHtml;
    }

    addEventListeners() {
        const removeItem = document.querySelector(`.item-${this.dish.id}`);
        removeItem.addEventListener("click",() =>{
           this.removeFromWishlist();
        });

    }
    removeFromWishlist() {
        console.log(`Removing recipe ID ${this.dish.id} from wishlist`);
        let url = `http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/recipe/delete/wishlist/${this.dish.id}`
        this.httpService.handleDeleteData(url);
    }

}
