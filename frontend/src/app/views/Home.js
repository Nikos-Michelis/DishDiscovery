import AbstractView from "./AbstractView.js";
import ExtendedHeader from "../layout/extended-header/ExtendedHeader.js";
import PopularDishesTable from "../components/cards-table/popular-dishes/PopularDishesTable.js";
import Carousel from "../components/carousel/Carousel.js";
import PromoBlock from "../components/blocks/PromoBlock.js";
import WhyCards from "../components/why-cards/WhyCards.js";
import Members from "../components/cards-table/members/MembersTable.js";
import NewSletter from "../components/newsletter/NewSletter.js";
import HttpService from "../services/HttpService.js";

export default class Home extends AbstractView {
    constructor(params, isAuthenticated) {
        super(params, isAuthenticated);
        this.setTitle("Home");
        this.isAuthenticated = isAuthenticated;
        this.httpService = new HttpService();
        this.extendedNavBar = new ExtendedHeader(this.isAuthenticated);
        this.promoBlock = new PromoBlock()
        this.whyCards = new WhyCards();
        const teamMembers = [
            {
                image: "src/assets/images/team-1.jpg",
                name: "Alice Smith",
                role: "Head Chef",
                facebook: "https://www.facebook.com/alice",
                twitter: "https://www.twitter.com/alice",
                instagram: "https://www.instagram.com/alice"
            },
            {
                image: "src/assets/images/team-2.jpg",
                name: "Bob Johnson",
                role: "Sous Chef",
                facebook: "https://www.facebook.com/bob",
                twitter: "https://www.twitter.com/bob",
                instagram: "https://www.instagram.com/bob"
            },
            {
                image: "src/assets/images/team-3.jpg",
                name: "Carol Williams",
                role: "Pastry Chef",
                facebook: "https://www.facebook.com/carol",
                twitter: "https://www.twitter.com/carol",
                instagram: "https://www.instagram.com/carol"
            },
            {
                image: "src/assets/images/team-4.jpg",
                name: "Dave Brown",
                role: "Line Cook",
                facebook: "https://www.facebook.com/dave",
                twitter: "https://www.twitter.com/dave",
                instagram: "https://www.instagram.com/dave"
            },
        ];
        this.members = new Members(teamMembers);
        this.newsletter = new NewSletter();
    }
    async getNavBarHtml() {
        return await this.extendedNavBar.getHtml();
    }
    newEventListeners(){
        this.promoBlock.addEventListeners();
    }


    async getContentHtml() {
        const productsData = await this.httpService.handleGetData("http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/recipe/weekly");
        this.popularProducts = new PopularDishesTable(productsData.data);
        this.carousel = new Carousel(productsData.data);
        const popularProductsHtml = await this.popularProducts.getHtml();
        const carouselProductsHtml = await this.carousel.getHtml();
        const promoBlockHtml = this.promoBlock.getBlockHtml();
        const whyCardsHtml = this.whyCards.getHtml();
        const promoReverseBlockHtml = this.promoBlock.getReverseBlockHtml();
        const membersHtml =  this.members.getHtml();
        const newSletterHtml =  this.newsletter.getHtml();
        return `
           ${popularProductsHtml}
           ${carouselProductsHtml}
           ${promoBlockHtml}
           ${whyCardsHtml}
           ${promoReverseBlockHtml}
           ${membersHtml}
           ${newSletterHtml}
        `;
    }
}
