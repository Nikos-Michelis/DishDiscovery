import GenericBlock from "./GenericBlock.js";

export default class PromoBlocks {
    constructor() {
        this.promoBlockData = {
            imageUrl: "src/assets/images/ChefImage.jpg",
            imageAlt: "Chef Image",
            heading: "NEW RECIPES DAILY",
            paragraph: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed doLorem ipsum dolor sit amet.",
            buttonText: "See More",
            buttonLink: "/dishes"
        };

        this.promoBlockDataReverse = {
            imageUrl: "src/assets/images/ChefCookingImage.jpg",
            imageAlt: "Chef Cooking Image",
            heading: "REGISTER NOW",
            paragraph: "Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit, sed doLorem ipsum dolor sit amet," +
                "consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit, sed doLorem ipsum dolor sit amet.",
            buttonText: "Register",
        };

        this.genericBlock = new GenericBlock(this.promoBlockData);
        this.promoBlockReverse = new GenericBlock(this.promoBlockDataReverse);

    }

    getBlockHtml() {
        return `
            ${this.genericBlock.getHtml()} 
        `;
    }
    getReverseBlockHtml() {
        return `
            ${this.promoBlockReverse.getHtml()} 
        `;
    }

}
