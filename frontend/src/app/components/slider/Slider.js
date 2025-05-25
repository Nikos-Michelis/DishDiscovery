import Slide from "../cards/slider-card/Slide.js";
import CarouselControls from "../carousel/CarouselControls.js";

export default class Slider {

    constructor(slideData) {
        this.slides = slideData.map(data => new Slide(data));
    }

    async getHtml() {
        const sliderHtml =  `
            <div class="slider-wrapper">
                <div class="slider-container">
                    <div class="slider-inner">
                        <div class="slides-container">
                            ${this.generateSlidesHtml()}
                        </div>
                        <div class="slider-controls-box">
                            <a class="slider-prev" id="slider-prev" role="button">
                                <i class="fa fa-arrow-left" aria-hidden="true"></i>
                                <span class="sr-only">Previous</span>
                            </a>
                            <div class="radio-btn-container">
                                ${this.generateRadioButtonsHtml()}
                            </div>
                            <a class="slider-next" id="slider-next" role="button">
                                <i class="fa fa-arrow-right" aria-hidden="true"></i>
                                <span class="sr-only">Next</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        `;
        // After generating the HTML, initialize the carousel controls
        setTimeout(() => {
            new CarouselControls({
                cardSelector: 'label.slide-container',
                radioButtonName: 'slider',
                leftButtonSelector: '#slider-prev',
                rightButtonSelector: '#slider-next',
                cycleInterval: 3000,
                debounceInterval: 700
            }).init();
        }, 100);
        return sliderHtml;
    }
    /* if index===0 then we have active slide*/

    generateSlidesHtml() {
        return this.slides.map((slide, index) => slide.getHtml(index, index === 0)).join('');
    }

    generateRadioButtonsHtml() {
        return this.slides.map((_, index) => `
            <input type="radio" name="slider" id="slider-item-${index + 1}" ${index === 0 ? 'checked' : ''}>
        `).join('');
    }
}
