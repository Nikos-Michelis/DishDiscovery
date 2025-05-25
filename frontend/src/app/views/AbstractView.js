import NavBar from "../layout/navbar/NavBar.js";
import Footer from "../layout/footer/Footer.js";
import PopupAuthForm from "../forms/PopupAuthForm.js";
import Pagination from "../components/pagination/Pagination.js";

export default class AbstractView {
  constructor(params, isAuthenticated, history) {
    this.params = params;
    this.isAuthenticated = isAuthenticated;
    this.history = history;
    this.defaultNavBar = new NavBar(this.isAuthenticated);
    this.defaultFooter = new Footer();
    this.popupAuthForm = new PopupAuthForm();
    this.pagination = new Pagination();
  }

  setTitle(title) {
    document.title = title;
  }

  async getHtml() {
    const [
        navBarHtml,
        footerHtml,
        contentHtml,
        loginForm
    ] = await Promise.all([
        this.getNavBarHtml(),
        this.getFooterHtml(),
        this.getContentHtml(),
        this.isAuthenticated ? '' :  this.popupAuthForm.getHtml()
    ]);
    document.addEventListener("DOMContentLoaded", () => {
        this.onAfterRender();
        this.newEventListeners();
    });
    return `
        <header>${navBarHtml}</header>
        <main class="container">${contentHtml}</main>
        ${loginForm}
        ${footerHtml}
    `;

  }

  onAfterRender() {
      this.defaultNavBar.addEventListeners();
      this.isAuthenticated ? '' : this.popupAuthForm.addEventListeners();
      this.pagination.addEventListeners(this.onPageChange !== undefined? this.onPageChange.bind(this) : '');
  }
  newEventListeners(){}

  getNavBarHtml() {
    return this.defaultNavBar.getHtml();
  }

  async getFooterHtml() {
    return await this.defaultFooter.getHtml();
  }

  async getContentHtml() {
    return "";
  }
}
