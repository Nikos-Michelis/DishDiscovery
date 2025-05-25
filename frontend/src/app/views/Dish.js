import AbstractView from "./AbstractView.js";
import HttpService from "../services/HttpService.js";

export default class Dish extends AbstractView {
    constructor(params, isAuthenticated) {
        super(params, isAuthenticated);
        this.setTitle("Dishes");
        this.isAuthenticated = isAuthenticated;
        this.params = params;
        console.log(this.params.pathParams);
        this.httpService = new HttpService();
        this.url = `http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/recipe/${this.params.pathParams.id}`
    }

    async getContentHtml() {
        const data = await this.httpService.handleGetData(this.url);
        try {
            return `
             <div class="item-details-container">
                <div class="item-details-wrapper">
                    <div class="item-info-title">
                        <div class="item-image-box">
                            <img src="${data.data.image}" alt="" class="item-image">
                        </div>
                        <section class="item-details">
                            <div class="detail-title-box">
                                <h1>${data.data.name}</h1>
                                <div class="detail-title-info-box">
                                    <div class="detail">
                                        <i class="fa-solid fa-user"></i>
                                        <span class="recipe-category">${data.data.author.username}</span>
                                    </div>
                                    <div class="detail">
                                        <span class="recipe-category">${data.data.category}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="detail-info-box">
                                <div class="detail">
                                    <i class="fa-solid fa-clock"></i>
                                    <span class="recipe-execution-time">${data.data.executionTime} min</span>
                                </div>
                                <div class="detail">
                                    <i class="fa-solid fa-star"></i>
                                    <span class="recipe-difficulty">${data.data.difficulty}</span>
                                </div>
                                <div class="detail">
                                    <i class="fa-solid fa-bowl-food"></i>
                                    <span class="recipe-execution-time">2-3</span>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="item-info-main">
                        <section class="recipe-ingredients" id="recipe-ingredients">
                            <h2>Ingredients</h2>
                            <hr>
                            ${this.generateIngredientsHtml(data)}
                        </section>
                        <section class="recipe-steps" id="recipe-steps">
                            <h2>Instructions </h2>
                            <hr>
                            ${this.generateStepsHtml(data)}
                        </section>
            
                    </div>
                </div>
            </div>
            `;
        } catch (error) {
            console.error("Error loading dishes:", error);
            return "<p>Error loading dishes. Please try again later.</p>";
        }
    }
    generateIngredientsHtml(data) {
        return data.data.ingredients.map((ingredient) =>
            ` 
                <div class="single-list-item grocery-list-item ">
                    <span class="check"></span>
                    <div class="details-wrapper">
                        <span class="text-grey">${ingredient.amount} ${ingredient.measurement.name}.</span> ${ingredient.name}
                    </div>
                </div>
            `).join('');
    }
    generateStepsHtml(data) {
        return data.data.steps.map((step) =>
            ` 
                 <div class="single-list-item step-list-item"><p>
                    <div class="instruction-step">
                        <span>Step ${step.step} - </span>
                    </div>
                    <p>${step.instruction}</p>
                 </div>
            `).join('');
    }
}
