export default class Slide {

    constructor(slideData) {
        this.slideData = slideData;
    }

    getHtml(index, isActive) {
        return `
            <label for="slider-item-${index + 1}" class="slide-container ${isActive ? 'active' : index === 1 ? 'hidden-right'
                     : index === 2 ? 'hidden-left'
                : 'hidden-middle'}" id="slide-${index + 1}">
                <div class="slide-info-main">
                    <div class="detail-box">
                        <h1>${this.slideData.title}</h1>
                        <p>${this.slideData.description}</p>
                        <div class="btn-box">
                            <a href="${this.slideData.link}" class="filled-button">Contact Us</a>
                        </div>
                    </div>
                    <div class="img-box">
                        <img src="${this.slideData.image}" alt="${this.slideData.title}" />
                    </div>
                </div>
            </label>
        `;
    }
}
