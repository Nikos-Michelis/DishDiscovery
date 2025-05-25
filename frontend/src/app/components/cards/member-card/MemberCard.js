// ProductCard.js
export default class MemberCard {
    constructor(memberData) {
        this.memberData = memberData;
    }

    getHtml() {
        return `
             <article class="card-container">
                    <div class="promo-card-main">
                        <div class="img-box">
                            <img class="card_image" src="${this.memberData.image}" alt="${this.memberData.name}"/>
                        </div>
                        <div class="card-info-details">
                            <div class="card-info">
                                <h2 class="card-title">${this.memberData.name}</h2>
                                <p class="card-text">${this.memberData.role}</p>
                                <div class="social-box">
                                    <a href="${this.memberData.facebook}">
                                        <i class="fa-brands fa-facebook"></i>
                                    </a>
                                    <a href="${this.memberData.twitter}">
                                        <i class="fa-brands fa-square-x-twitter"></i>
                                    </a>
                                    <a href="${this.memberData.instagram}">
                                        <i class="fa-brands fa-instagram"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </article>
        `;
    }
}
