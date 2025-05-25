import Validator from "./validator/Validator.js";
import HttpService from "../services/HttpService.js";
import router from "../app.js";
import OtpAuth from "../views/auth/OtpAuth.js";

export default class OtpForm {
    constructor(state) {
        this.httpRequest = new HttpService();
        this.state = state;
    }

    getHtml() {
        const otpFormHtml = `
            <section class="otp-form-section">
                <div class="otp-form-wrapper">
                    <div class="otp-container">
                        <div class="form-box otp">
                            <div class="form-content">
                                <div class="otp-title-secondary">
                                        <h1>One Time Password (OTP)</h1>
                                </div>
                               <div class="response-error-wrapper"></div>
                                <div class="otp-text">
                                    <p>We can't seem to identify you using your e-mail address alone.
                                        To help us identify your account, please enter a one time password (OTP) associated with a billing address from your account.</p>
                                </div>
                                <form action="">
                                     <div class="input-field">
                                        <label class="static-label">Request ID</label>
                                        <input type="text" id="request_id" name="email" value="${this.state? this.state.uuid : ""}" readonly>
                                    </div>
                                    <div class="input-field">
                                        <input type="text" id="otp_code" name="otp_code" value="" required>
                                        <label>One Time Password</label>
                                    </div>
                                    <div class="btn-box">
                                        <button type="submit">Continue</button>
                                    </div>
                                    <div class="otp-resend">
                                        <p>Didn't receive code? </p>
                                        <span><a type="submit" class="otp-resend-btn">Resend</a></span>
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
        }, 0);
        return otpFormHtml;
    }

    init(){
        this.form = document.querySelector(".otp-container");
    }

    validationOnsubmit(form, validator, resend_url) {

        let url = resend_url !== null ? resend_url : this.state.links.self;

        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            let errorCount = validator.retrieveFieldErrors();
            if (errorCount === 0) {
                const inputData = {
                    uuid: document.querySelector(`#request_id`).value.trim(),
                    otp: document.querySelector(`#otp_code`).value.trim(),
                    rememberMe: this.state.rememberMe
                };
                console.log(inputData);
                await this.httpRequest.handlePostData(url, inputData)
                    .then(outputData =>{
                        console.log(outputData.links);
                        outputData.links !== undefined ? router.replaceState(outputData, `${outputData.links.ui}`)
                            : router.navigateTo(null, '/');

                    }).catch(error => {
                        console.error('Error during OTP request:', error);
                    });

            } else {
                console.log('Form validation failed. Please correct the errors and try again.');
            }
        });
    }
    otpData() {
        const uuid =  document.querySelector(`#request_id`).value.trim();
        const otp = document.querySelector(`#otp_code`).value.trim();
        if (uuid && otp) {
            return {
                uuid: document.querySelector(`#request_id`).value.trim(),
                otp: document.querySelector(`#otp_code`).value.trim(),
                rememberMe: this.state.rememberMe

            };
        } else {
            return null;
        }
    }
    initializeForm() {
        try {
            let resend_url = null;
            let formElement = this.form.querySelector(`.form-box.otp`);
            let fields = ["request_id", "otp_code"];

            this.validationOnsubmit(formElement, new Validator(fields), resend_url);

            document.querySelector(".otp-resend-btn").addEventListener("click", () => {
                resend_url = 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/auth/resend';
                this.handleResendOtp(resend_url);
            });
        }catch (error){
            throw Error("Failed to initilize otp form.");
        }
    }

    handleResendOtp(resend_url) {
        const requestId = document.querySelector(`#request_id`).value.trim();

        const inputData = {
            uuid: requestId
        };

        console.log('Resending OTP with data:', inputData);

        // Perform the submission
        this.httpRequest.handlePostData(resend_url, inputData)
            .then(outputData => {
                if (outputData) {
                    console.log('OTP Resend Successful:', outputData);
                    const currentState = history.state || {};
                    const newState = {
                        ...currentState,
                        uuid: outputData.uuid
                    };
                    console.log('Updated history.state:', history.state);
                    router.navigateTo(newState, window.location.href);

                } else {
                    console.error('No response from server for OTP resend.');
                }
            })
            .catch(error => {
                console.error('Error during OTP resend request:', error);
            });
    }

}
