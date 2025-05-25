import ProductCard from "../../cards/popular-product-card/ProductCard.js";

export default class PopularDishesTable {
    constructor(productData) {
        this.cards = productData.map(data => new ProductCard(data));
    }

    async getHtml() {
        return `
            <section class="popular-products">
                <div class="container">
                    <div class="heading-container">
                        <h2>Popular Dishes</h2>
                    </div>
                    <div class="cards-grid-container">
                        <div class="cards-grid-layout">
                            <div class="cards-wrapper">
                                ${this.generateCardsHtml(0, 2)} <!-- First wrapper with 2 cards -->
                            </div>
                            <div class="cards-wrapper">
                                ${this.generateCardsHtml(2, 3)} <!-- Second wrapper with 3 cards -->
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        `;
    }

    generateCardsHtml(startIndex, count) {
        if (typeof startIndex === 'undefined') {
            startIndex = 0;
            count = this.cards.length;
        }
        if (typeof count === 'undefined') {
            count = this.cards.length - startIndex;
        }
        const cardsToRender = this.cards.slice(startIndex, startIndex + count);
        return cardsToRender.map((card) => card.getHtml()).join('');
    }
}
