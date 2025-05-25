import LogoutService from "../../services/LogoutService.js";
export default class NavBar {
    constructor(isAuthenticated) {
        this.logout = new LogoutService();
        this.isAuthenticated = isAuthenticated;
    }
    async getHtml() {
        return `
            <nav class="navbar">
                <a class="navbar-brand" href="/">
                    <span>Dish Discovery</span>
                </a>
                <input class="hamb-checkbox" type="checkbox" id="checkbox"/>
                <label class="hamb-label" for="checkbox" aria-label="Toggle Navigation">
                    <span class="line1 hamb-line" aria-hidden="true"></span>
                    <span class="line2 hamb-line" aria-hidden="true"></span>
                    <span class="line3 hamb-line" aria-hidden="true"></span>
                </label>
                <div class="nav-list-container">
                    <ul class="nav-list">
                        <li class="nav-item"><a href="/" data-link>Home</a></li>
                        <li class="nav-item"><a href="/dishes" data-link>Dishes</a></li>
                        <li class="nav-item"><a href="/contact" data-link>Contact</a></li>
                        <div class="user-option">
                            ${this.showLoginBtn()}
                            <a href="/wishlist" data-link><i class="fa-regular fa-heart"></i></a>
                            <a href="/my-articles" data-link><i class="fa-regular fa-pen-to-square"></i></a>
                            ${this.showLogoutBtn()}
                        </div>
                    </ul>
                </div>
            </nav>
        `;
    }
    showLoginBtn() {
        if (!this.isAuthenticated) {
            return `<a class="login-btn"><i class="fa-regular fa-user"></i></a>`
        }
        return '';
    }
    showLogoutBtn() {
        if(this.isAuthenticated) {
            return `<a class="logout-btn"><i class="fa-solid fa-right-from-bracket"></i></a>`
        }
        return '';
    }
    addEventListeners() {
        const logout =  document.querySelector("a.logout-btn")
        if (logout !== null) {
            document.querySelector("nav a.logout-btn").addEventListener("click", async () => {
                await this.logout.logout();
            });
        }
    }
}
