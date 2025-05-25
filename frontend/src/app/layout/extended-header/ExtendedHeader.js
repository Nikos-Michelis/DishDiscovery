import NavBar from "../navbar/NavBar.js";
import Slider from "../../components/slider/Slider.js";

export default class ExtendedNavBar {
    constructor(isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
        this.navbar = new NavBar(this.isAuthenticated);
        const slideData = [
            {
                title: "Welcome To App Name",
                description: "Sequi perspiciatis nulla reiciendis, rem, tenetur impedit, eveniet non necessitatibus error distinctio mollitia suscipit.",
                link: "/contact",
                image: "src/assets/images/dish_logo.png"
            },
            {
                title: "Discover Our Features",
                description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, consectetur adipiscing elit.",
                link: "/contact",
                image: "src/assets/images/dish_logo.png"
            },
            {
                title: "New Dishes Daily",
                description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, consectetur adipiscing elit.",
                link: "/contact",
                image: "src/assets/images/dish_logo.png"
            },
            {
                title: "Explore Our Awarded Chefs",
                description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, consectetur adipiscing elit.",
                link: "/contact",
                image: "src/assets/images/dish_logo.png"
            },
        ];
        this.carousel = new Slider(slideData);
    }

    async getHtml() {
        const navBarHtml = await this.navbar.getHtml();
        const carouselHtml = await this.carousel.getHtml();

        return `
            ${navBarHtml}
            ${carouselHtml}
        `;
    }
}
