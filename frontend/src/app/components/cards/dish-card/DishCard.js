import HttpService from "../../../services/HttpService.js";

export default class DishCard {
    constructor(dish, isAuthenticated) {
        this.isAuthenticated  = isAuthenticated;
        this.dish = dish;
        this.httpService = new HttpService();
    }

    getHtml() {
        const cardHtml = `
            <article class="card-container card-product">
                <div class="card-info-main">
                    <div class="card-options">
                        <div class="new-card">
                            <p class="new-text">New</p>
                        </div>
                        <div class="wishlist-btn-box wishlist-btn-box-${this.dish.id}">
                            <span class="wishlist-btn" id="whitelist-btn"><i class="fa-regular fa-heart"></i></span>
                        </div>
                    </div>
                    <a href="http://localhost:3000/dish/${this.dish.id}" class="hero-image-container">
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
                    </div>
                </div>
                <div class="card-info-container">
                    <div class="card-details">
                        <i class="fa-solid fa-clock"></i>
                        <span class="recipe-execution-time">${this.dish.executionTime}</span>
                    </div>
                    <div class="card-details">
                        <i class="fa-solid fa-star"></i>
                        <span class="recipe-difficulty">${this.dish.difficulty}</span>
                    </div>
                    <div class="card-details">
                        <i class="fa-solid fa-bowl-food"></i>
                        <span class="recipe-execution-time">${this.dish.portions}</span>
                    </div>
                </div>
            </article>
        `;
        setTimeout(() => {
            this.setWishlist();
            this.addEventListeners();
        }, 0);

        return cardHtml;
    }

    setWishlist() {
        const heartBox = document.querySelector(`.wishlist-btn-box-${this.dish.id}`);
        if (this.dish.wishlisted) {
            heartBox.classList.add("show-heart");
        }
    }

    addEventListeners() {
        const heartBox = document.querySelector(`.wishlist-btn-box-${this.dish.id}`);

        heartBox.addEventListener("click", () => {
            if(this.isAuthenticated) {
                if (heartBox.classList.contains("show-heart")) {
                    heartBox.classList.remove("show-heart");
                    this.removeFromWishlist();
                } else {
                    heartBox.classList.add("show-heart");
                    this.addToWishlist();
                }
            }else {
                console.log("You must log-in to perform that action")
            }
        });
    }

    addToWishlist() {
        console.log(`Adding recipe ID ${this.dish.id} to wishlist`);
        let url = `http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/recipe/wishlist/add/${this.dish.id}`
        this.httpService.handlePostData(url);
    }

    removeFromWishlist() {
        console.log(`Removing recipe ID ${this.dish.id} from wishlist`);
        let url = `http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/recipe/delete/wishlist/${this.dish.id}`
        this.httpService.handleDeleteData(url);
    }
}
