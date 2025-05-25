export default class ProductCard {
    constructor(product) {
        this.product = product;
    }

    getHtml() {
        return `
            <article class="card-container promo-card-box">
                <div class="card-info-main">
                    <a href="http://localhost:3000/dish/${this.product.id}" class="img-box">
                        <img class="card-image" src="${this.product.image}" alt="${this.product.name}"/>
                    </a>
                    <div class="title-box">
                        <h2 class="card-title">${this.product.name}</h2>
                    </div>
                </div>
            </article>
        `;
    }
}
