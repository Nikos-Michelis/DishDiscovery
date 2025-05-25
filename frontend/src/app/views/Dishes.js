import AbstractView from "./AbstractView.js";
import ProductFilter from "../components/product-filter/ProductFilter.js";
import Pagination from "../components/pagination/Pagination.js";
import HttpService from "../services/HttpService.js";
import DishesTable from "../components/cards-table/dishes/DishesTable.js";
import UrlManager from "../utils/UrlManager.js";

export default class Dishes extends AbstractView {
    constructor(params, isAuthenticated) {
        super(params, isAuthenticated);
        this.setTitle("Dishes");
        this.isAuthenticated = isAuthenticated;
        this.headingText = 'Our Dishes';
        this.params = params;
        this.data = new HttpService();
        this.urlManager = new UrlManager();
        this.onPageLoadUrlParameters();
        this.onLoadInitUrl();
    }

    async getContentHtml() {
        try {
            const queryParams = this.urlManager.getQueryParamsObject();
            const dishData = await this.data.handleGetData(this.baseUrl + window.location.search);

            this.productFilter = new ProductFilter(queryParams, this.onFilterChange.bind(this), this.onResetFilters.bind(this), this.headingText);
            this.dishesTable = new DishesTable(dishData.data, this.isAuthenticated);
            this.pagination = new Pagination(dishData.meta, queryParams.page);
            return `
                ${await this.productFilter.getHtml()}
                ${this.pagination.getHtml()}
                ${(this.dishesTable.getHtml())}
                ${this.pagination.getHtml()}
            `;
        } catch (error) {
            console.error("Error loading dishes:", error);
            return "<p>Error loading dishes. Please try again later.</p>";
        }
    }
    onLoadInitUrl(){
        if (this.isAuthenticated) {
            this.baseUrl = `http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/recipe/user/recipeInfo`;
        }else{
            this.baseUrl = `http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/recipe/recipeInfo`;
        }
    }
    onPageLoadUrlParameters() {
        const queryParams = this.urlManager.getQueryParamsObject();
        if (!queryParams.limit) {
            this.urlManager.setQueryParam('limit', 5);
        }
        if (!queryParams.page) {
            this.urlManager.setQueryParam('page', 1);
        }
    }
    async onPageChange(page) {
        try {
            this.urlManager.setQueryParam('page', page);
            await this.updateContent();
        } catch (error) {
            console.error("Error navigating to page:", error);
        }
    }

    async onFilterChange(queryParams) {
        try {
            this.urlManager.setQueryParamsObject(queryParams);
            await this.updateContent();
        } catch (error) {
            console.error("Error fetching filtered data:", error);
        }
    }
    async onResetFilters() {
        const filterElements = document.querySelectorAll('.toolbar-option select');
        filterElements.forEach(element => {
            element.value = "";
        });
        document.getElementById("limit-per-page").value = 5;
    }

    async updateContent() {
        try {
             const queryParams = this.urlManager.getQueryParamsObject();
             const updatedDishData = await this.data.handleGetData(this.baseUrl + window.location.search);

             this.pagination = new Pagination(updatedDishData.meta, queryParams.page);
             this.dishesTable = new DishesTable(updatedDishData.data, this.isAuthenticated);

            document.getElementById("cards-grid-section").innerHTML = this.dishesTable.getHtml();
            document.querySelectorAll(".pagination-section").forEach(section => {
                section.outerHTML = this.pagination.getHtml();
            });
            await this.onAfterRender();
        } catch (error) {
            console.error("Error updating content:", error);
        }
    }
}
