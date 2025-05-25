import DishCard from "../../cards/dish-card/DishCard.js";

export default class DishesTable {
    constructor(dishData, isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
        console.log(this.isAuthenticated );
        this.cards = dishData.map(data => new DishCard(data,  this.isAuthenticated));
    }

    getHtml() {
        const tableHtml =  `
            <section class="cards-grid-section" id="cards-grid-section">
                <div class="cards-wrapper">
                <div class="cards-grid-container">
                    <div class="cards-grid-layout">
                        ${this.generateCardsHtml()}
                    </div>
                </div>
                </div>
            </section>
        `;


        return tableHtml;
    }

    generateCardsHtml() {
        return this.cards.map((card) => card.getHtml()).join('');
    }

}
