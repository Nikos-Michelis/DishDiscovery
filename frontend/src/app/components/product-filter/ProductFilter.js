import UrlManager from "../../utils/UrlManager.js";
import HttpService from "../../services/HttpService.js";

export default class ProductFilter {
    constructor(queryParams, onFilterChange, onResetFilters, headingText) {
        this.queryParams = queryParams;
        //console.log(this.queryParams);
        this.onFilterChange = onFilterChange;
        this.onResetFilters = onResetFilters;
        this.urlManager = new UrlManager(this.queryParams);
        this.data = new HttpService();
        this.baseUrl = `http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/recipe/filter`;
        //this.setHeading('Our Dishes');
        this.headingText = headingText;
    }

    async getHtml() {
        const filterData = await this.data.handleGetData(this.baseUrl);
        const { ingredient_categories, measurements, ingredients, categories } = filterData.data || {};
        const unitsOptions = this.generateOptions(measurements, this.queryParams.unit);
        const ingredientOptions = this.generateOptions(ingredients, this.queryParams.ingridient_category);
        const ingredientCategoriesOptions = this.generateOptions(ingredient_categories, this.queryParams.ingridient_category);
        const categoryOptions = this.generateOptions(categories, this.queryParams.category);

        const productFilterHtml = `
        <section class="product-filter-section" id="product-filter-section">
            <div class="toolbar-container">
                ${this.setHeading(this.headingText)}
                <div class="search-container">
                    <input type="hidden" name="action" value="search">
                    <input class="search-bar" id="search-bar" value="" type="text" name="search" placeholder="Say My Name...">
                    <div class="search-button">
                        <i class="fas fa-search"></i>
                    </div>
                </div>
                <div class="limit-option">
                    <div class="btn-box">
                        <button class="open-options-btn" id="open-options-btn"><i class="fas fa-filter"></i></button>
                    </div>
                    <div class="limit-page-select">
                        <select name="limit" id="limit-per-page">
                            ${this.generateLimitOptions()}
                        </select>
                    </div>
                </div>
                <div class="toolbar-option" id="toolbar-option">
                    <div class="select-option-container">
                        ${this.generateSelectElement('ingridient_category', 'Ingridient Category', ingredientCategoriesOptions)}
                        ${this.generateSelectElement('ingredient', 'Ingredients', ingredientOptions)}
                        ${this.generateSelectElement('category', 'Category', categoryOptions)}
                        ${this.generateSelectElement('Measurement', 'Measurements', unitsOptions)}
                    </div>
                    <div class="toolbar-option-reset">
                        <button class="reset-options-btn" id="reset-options-btn"><i class="fa-solid fa-arrows-rotate"></i></button>
                    </div>
                </div>
            </div>
        </section>
    `;

        setTimeout(() => {
            this.init();
        }, 0);

        return productFilterHtml;
    }

    setHeading(headingText){
        return `
            <div class="search-heading-container">
                <h2>${headingText}</h2>
            </div>`;
    }

    generateOptions(items, selectedValue) {
        return items ? items.map(item => `<option value="${item.id}" ${selectedValue === item.id ? 'selected' : ''}>${item.name}</option>`).join('') : '';
    }

    generateLimitOptions() {
        const limits = [5, 20, 50];
        return limits.map(limit => `<option value="${limit}" ${Number(this.queryParams.limit) === limit ? 'selected' : ''}>Limit ${limit}</option>`).join('');
    }

    generateSelectElement(name, defaultText, optionsHtml) {
        return `
        <div class="select-option">
            <select name="${name}" id="${name}">
                <option value="" selected>${defaultText}</option>
                ${optionsHtml}
            </select>
        </div>
    `;
    }

    init() {
        this.showFilterBtn = document.querySelector(".open-options-btn");
        this.filterForm = document.querySelector(".toolbar-option");
        this.addEventListeners();
    }


    addEventListeners() {
        this.showFilterBtn.addEventListener('click', () => {
            this.filterForm.classList.toggle("show-popup");
        });

        document.getElementById("search-bar").addEventListener('selectionchange', () => {
           console.log(document.getElementById("search-bar").value);
        });

        const filterElements = document.querySelectorAll('.toolbar-container select');
        filterElements.forEach(element => {
            document.getElementById(element.id).addEventListener('change', async () => {
                if(document.getElementById(element.id).value !== "" && document.getElementById(element.id).value !== undefined) {
                    const newQueryParams = await this.setQueryParam(element.name, document.getElementById(element.id).value);
                    await this.onFilterChange(newQueryParams);
                }else{
                    this.urlManager.removeQueryParam(element.name);

                }
            });
        });

        document.getElementById("reset-options-btn").addEventListener('click', async () => {
            this.urlManager.clearQueryParams();
            this.urlManager.setQueryParam('limit', 5);
            this.urlManager.setQueryParam('page', 1);
            this.onResetFilters();
            await this.onFilterChange(this.urlManager.getQueryParamsObject());
        });
    }

    async setQueryParam(key, value) {
        this.urlManager.setQueryParam(key, value);
        return this.urlManager.getQueryParamsObject();
    }

}
