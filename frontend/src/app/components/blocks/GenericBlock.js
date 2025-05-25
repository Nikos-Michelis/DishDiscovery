import PopupAuthForm from "../../forms/PopupAuthForm.js";

export default class GenericBlock {
    constructor({ imageUrl, imageAlt, heading, paragraph, buttonText, buttonLink }) {
        this.imageUrl = imageUrl;
        this.imageAlt = imageAlt;
        this.heading = heading;
        this.paragraph = paragraph;
        this.buttonText = buttonText;
        this.buttonLink = buttonLink;
        this.popupAuthForm = new PopupAuthForm();

    }

    getHtml() {
        return `
            <section class="promo-section">
                <div class="promo-container">
                    <div class="img-box">
                        <img src="${this.imageUrl}" alt="${this.imageAlt}" />
                    </div>
                    <div class="detail-box">
                        <h2>${this.heading}</h2>
                        <p>${this.paragraph}</p>
                        <div class="btn-box">
                            ${this.setBtnType()}
                        </div>
                    </div>
                </div>
            </section>
        `;
    }

    setBtnType(){
        if(this.buttonLink !== undefined) {
          return `<a href="${this.buttonLink}" class="filled-button">${this.buttonText}</a>`;
        }else{
            return `<a class="register-btn filled-button">${this.buttonText}</a>`;
        }
    }

}
