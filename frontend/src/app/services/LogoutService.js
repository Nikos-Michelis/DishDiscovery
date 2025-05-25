import HttpService from "./HttpService.js";
import router from "../app.js";

export default class LogoutService {
    constructor() {
        this.httpRequest = new HttpService();
    }

    async logout() {
        const url = 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/auth/logout';
        try {
            const result = await this.httpRequest.handlePostData(url, {});
            console.log(result);
            router.navigateTo(null, '/');
        } catch (error) {
            router.navigateTo(null, '/');
        }
    }

}