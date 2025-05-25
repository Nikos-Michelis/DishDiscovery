import WishlistCard from "../../cards/wishlist-card/WishlistCard.js";

export default class WishlistTable {
    constructor(dishData) {
        //console.log(dishData);
        this.cards = dishData.map(data => new WishlistCard(data));
    }

    getHtml() {
        return `
             <section class="cards-grid-section" id="cards-grid-section">
                <div class="cards-wrapper">
                    <div class="cards-grid-container">
                        <div class="cards-grid-wishlist">
                           ${this.generateCardsHtml()}
                        </div>
                    </div>
                </div>
            </section>
        `;
    }

    generateCardsHtml() {
        return this.cards.map((card) => card.getHtml()).join('');
    }
}
