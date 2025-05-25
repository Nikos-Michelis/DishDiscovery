import MemberCard from "../../cards/member-card/MemberCard.js";

export default class Members {
    constructor(memberData) {
        this.cards = memberData.map(data => new MemberCard(data));
    }

     getHtml() {
        //console.log(this.generateCardsHtml());
        return `
        <section class="members-section">
            <div class="heading-container">
                <h2>OUR MEMBERS</h2>
            </div>
            <div class="promo-members-container">
                 ${this.generateCardsHtml()}
            </div>
        </section>
        `;
    }

    generateCardsHtml() {
        return this.cards.map((card) => card.getHtml()).join('');
    }
}
