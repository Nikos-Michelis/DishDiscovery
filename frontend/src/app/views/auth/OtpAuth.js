import OtpForm from "../../forms/OtpForm.js";
import AbstractView from "../AbstractView.js";
import router from "../../app.js";

export default class OtpAuth extends AbstractView{
    constructor(params, isAuthenticated, history) {
        super(params, isAuthenticated, history)
        this.state = history.state;
        console.log(this.state);
        this.validateRequestUrl = "http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/auth/validate-request";
        this.isValidRequestId(this.validateRequestUrl, this.state !== null? this.state.uuid : "");
        this.otpForm = new OtpForm(this.state);
        this.setTitle("Confirmation");
    }
    getContentHtml() {
       const otpFormHtml = this.otpForm.getHtml();
       return `${otpFormHtml}`;
    }
    async isValidRequestId(url, uuid) {
        console.log(uuid);
        const response = await fetch(url, {
            method: 'POST',
            body: JSON.stringify({uuid: uuid}),
            headers: {
                'Content-Type': 'application/json; charset=UTF-8',
            },
        });

        console.log(response);
        if (response.ok) {
            return await response.json();
        } else if (response.status === 404) {
            router.replaceState(null, "/404");
        } else {
            const errorData = await response.json();
            throw new Error(`Failed to fetch data. Status: ${response.status}, Message: ${errorData.error}`);
        }
    }
}
