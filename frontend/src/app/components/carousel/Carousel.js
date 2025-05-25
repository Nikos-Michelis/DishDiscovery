import CarouselCard from "../cards/carousel-card/CarouselCard.js";
import CarouselControls from "../carousel/CarouselControls.js";

export default class Carousel {

    constructor(slideData) {
        this.cards = slideData.map(data => new CarouselCard(data));
    }

    async getHtml() {
        const carouselHtml = `
            <section class="carousel-section">
                <div class="heading-container">
                    <h2>Latest Dishes</h2>
                </div>
                <div class="carousel-wrapper">
                    <div class="carousel-container">
                        <div class="cards">
                            <div class="carousel-btn-container">
                                <div class="left-btn" id="left-btn">
                                    <i class="fa-solid fa-angle-left"></i>
                                </div>
                                <div class="right-btn" id="right-btn">
                                    <i class="fa-solid fa-angle-right"></i>
                                </div>
                            </div>
                            ${this.generateCardsHtml()}
                        </div>
                        <div class="radio-btn-container">
                           ${this.generateRadioButtonsHtml()}
                        </div>
                    </div>
                </div>
            </section>
        `;
        // After generating the HTML, initialize the carousel controls
        setTimeout(() => {
            new CarouselControls({
                cardSelector: 'label.card',
                radioButtonName: 'carousel',
                leftButtonSelector: '#left-btn',
                rightButtonSelector: '#right-btn',
                cycleInterval: 3000,
                debounceInterval: 700
            }).init();
        }, 0);

        return carouselHtml;
    }
    /* if index===0 then we have active slide*/
    generateCardsHtml() {
        return this.cards.map((slide, index) => slide.getHtml(index, index === 0)).join('');
    }

    generateRadioButtonsHtml() {
        return this.cards.map((_, index) => `
            <input type="radio" name="carousel" id="slider-item-${index + 1}" ${index === 0 ? 'checked' : ''}>
        `).join('');
    }
}
