import Validator from "./validator/Validator.js";
import HttpService from "../services/HttpService.js";
import AuthService from "../services/AuthService.js";
import router from "../app.js";

export default class ResetPasswordForm {
    constructor() {
        this.httpRequest = new HttpService();
        this.state = history.state;
        console.log(this.state);
        this.authService = new AuthService();
        this.clearHistoryStateOnNavigation();
    }

    getHtml() {
        const otpFormHtml = `
            <section class="reset-password-form-section">
                <div class="otp-form-wrapper">
                    <div class="otp-container">
                        <div class="form-box otp">
                            <div class="form-content">
                                <div class="otp-title-secondary">
                                        <h1>Reset Password</h1>
                                </div>
                               <div class="response-error-wrapper"></div>
                                <form action="">
                                     <div class="input-field">
                                        <label class="static-label">Request ID</label>
                                        <input type="text" id="request_id" name="request_id" value="${this.state.uuid}" readonly>
                                    </div>
                                    <div class="input-field">
                                        <input type="password" id="reset_password" name="reset_password" value="" required>
                                        <label>New Password</label>
                                    </div>
                                     <div class="input-field">
                                        <input type="password" id="repeat_reset_password" name="repeat_reset_password" value="" required>
                                        <label>Repeat New Password</label>
                                    </div>
                                    
                                    <div class="btn-box">
                                        <button type="submit">Continue</button>
                                    </div>
                                </form>
                            <div>
                        </div>
                    </div>
                </div>
            </section>
        `;
        setTimeout(() => {
            this.init();
            this.initializeForm();
        }, 300);
        return otpFormHtml;
    }

    init(){
        this.form = document.querySelector(".otp-container");
    }

    validationOnsubmit(form, validator) {
        let url =  this.state.links.self;

        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            let errorCount = validator.retrieveFieldErrors();
            if (errorCount === 0) {
                const inputData = {
                    uuid: document.querySelector(`#request_id`).value.trim(),
                   // otp: document.querySelector(`#otp_code`).value.trim(),
                    password: document.querySelector(`#reset_password`).value.trim(),
                    confirmPassword: document.querySelector(`#repeat_reset_password`).value.trim()
                };
                console.log(inputData);
                await this.httpRequest.handlePostData(url, inputData)
                    .then(outputData =>{
                        if (outputData) {
                            router.replaceState(null, '/');
                        }
                    }).catch(error => {
                        console.error('Error during Password reset request:', error);
                    });

            } else {
                console.log('Form validation failed. Please correct the errors and try again.');
            }
        });
    }
    clearHistoryStateOnNavigation() {
        window.addEventListener('beforeunload', () => {
            router.replaceState(null, '', '/');
        });
    }

    initializeForm() {
        let formElement = this.form.querySelector(`.form-box.otp`);
        let fields = ["request_id", "reset_password", "repeat_reset_password"];

        this.validationOnsubmit(formElement, new Validator(fields));

    }

}
