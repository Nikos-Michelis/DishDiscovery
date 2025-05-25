import Validator from "./validator/Validator.js";
import HttpService from "../services/HttpService.js";
import router from "../app.js";

export default class PopupAuthForm {
    constructor() {
        this.httpRequest = new HttpService();
        this.url_login = 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/auth/login';
        this.url_register = 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/auth/register';
        this.url_forgot_password = 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/auth/request-password-reset';
        this.formElement = null;
        this.fields = null;
    }
    getHtml() {
        return `
            <section class="login-section" id="login-section">
                <div class="blur-bg-overlay"></div>
                <div class="form-popup-container">
                    <span class="close-btn"><i class="fa-solid fa-xmark"></i></span>
                    <span class="back-btn"><i class="fa-solid fa-arrow-left"></i></span>
                    <div class="form-box login">
                        <div class="form-details">
                            <h2>Create Account</h2>
                            <p>To become a part of our community, please sign up using your personal information.</p>
                            <div class="bottom-link">
                                Don't have an account?
                                <a id="signup-link">Sign-up</a>
                            </div>
                        </div>
                        <div class="form-content form-content-login">
                            <h2>Do you already have an account?</h2>
                            <div class="response-error-wrapper"></div>                            
                           <form>
                                <div class="input-field">
                                    <input type="text" id="username" required>
                                    <label>Username</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="input-field">
                                    <input type="password" id="password" required>
                                    <label>Password</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="password-options">
                                    <div class="remember-box">
                                        <input type="checkbox" id="checkbox-remember">
                                        <label><span>Remember me</span></label>
                                    </div>
                                    <a class="forgot-pass-link" id="forgot-pass-link">Forgot password?</a>
                                </div>
                                <div class="btn-box">
                                    <button type="submit">Log In</button>
                                </div>
                           </form>
                        </div>
                        <div class="form-content forgot-password">
                            <h2>Do you already have an account?</h2>
                            <div class="response-error-wrapper"></div>                            
                            <form>
                                <div class="input-field">
                                    <input type="text" id="email-forgot-password" required>
                                    <label>Email</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="btn-box">
                                    <button type="submit">Continue</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="form-box register">
                        <div class="form-content">
                            <h2>Register</h2>
                            <div class="response-error-wrapper"></div>
                            <form action="#">
                                <div class="input-field">
                                    <input type="text" id="username-register" required>
                                    <label>Username</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="input-field">
                                    <input type="text" id="email-register" required>
                                    <label>Email</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="input-field">
                                    <input type="password" id="password-register" required>
                                    <label>Password</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="input-field">
                                    <input type="password" id="repeat-password" required>
                                    <label>Repeat Password</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="policy-text">
                                    <input type="checkbox" id="policy">
                                    <label for="policy">
                                        I agree the
                                        <a href="#" class="option">Terms & Conditions</a>
                                    </label>
                                </div>
                                <div class="btn-box">
                                    <button type="submit">Register</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </section>
        `;

    }
    init(){
        this.showPopupBtn = document.querySelector("nav a.login-btn");
        this.showRegisterBtn = document.querySelector("div a.register-btn");
        this.showForgetPassBtn = document.querySelector("a.forgot-pass-link");
        this.backPopupBtn = document.querySelector(".back-btn");
        this.formPopup = document.querySelector(".form-popup-container");
        this.hidePopupBtn = this.formPopup.querySelector("span.close-btn");
        this.signupLoginLink = this.formPopup.querySelectorAll(".bottom-link a");
    }
    addEventListeners() {
        try {
             this.init();
             this.showPopupBtn !== null ? this.showPopupBtn.addEventListener("click", () => {
                 document.body.classList.add("show-popup");
                 this.initializeForm('login');
             }):'';

             // Hide login popup
             this.hidePopupBtn.addEventListener("click", () => document.body.classList.remove("show-popup"));

             // Handle back button
             this.backPopupBtn.addEventListener("click", () => {
                 this.formPopup.classList.remove("show-signup");
                 this.formPopup.classList.remove("show-forgot-password");
                 history.pushState({popup: true, form: 'login', fields: []}, "");
                 this.initializeForm('login');
             });

             // Show or hide signup form
             this.signupLoginLink.forEach((link) => {
                 link.addEventListener("click", (e) => {
                     e.preventDefault();
                     const formType = link.id === "signup-link" ? 'register' : 'login';
                     if (formType === 'register') {
                         this.formPopup.classList.add("show-signup");
                     } else {
                         this.formPopup.classList.remove("show-signup");
                     }
                     this.initializeForm(formType);
                 });
             });

             // Handle "forgot password" link
             this.showForgetPassBtn.addEventListener("click", (e) => {
                 e.preventDefault();
                 this.formPopup.classList.add("show-forgot-password");
                 this.initializeForm('forgot-password');
             });

            this.showRegisterBtn !== null ? this.showRegisterBtn.addEventListener("click", () => {
                document.body.classList.add("show-popup");
                this.formPopup.classList.add("show-signup");
                this.initializeForm('register');
            }):'';
        }catch (error){
            console.error('Unable to setup popup form event listeners: ', error);

        }
    }
    validationOnsubmit(form, validator, url, data) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            const submitButton = form.querySelector('button[type="submit"]');
            submitButton.disabled = true;
            let errorCount = validator.retrieveFieldErrors();
            if (errorCount === 0) {
                const inputData =  data();
                if (!inputData) {
                    console.error("Invalid input fields");
                    submitButton.disabled = false;
                    return;
                }
                console.log(inputData);
                await this.httpRequest.handlePostData(url, inputData)
                    .then(outputData => {
                        console.log(outputData);
                        if (outputData && outputData.links.ui) {
                            document.body.classList.remove("show-popup");
                            router.replaceState(outputData, outputData.links.ui);
                        } else {
                            console.error('No redirect URL found in the response.');
                        }
                    }).catch(error => {
                        console.error('Error during request:', error);
                    }).finally(() => {
                        submitButton.disabled = false;
                    });
            } else {
                console.log('Form validation failed. Please correct the errors and try again.');
                submitButton.disabled = false;
            }
        });
    }
    initilizeFormElements(formType) {
        console.log(`Initializing form: ${formType}`);

        switch (formType) {
            case 'forgot-password':
                return this.formElement = this.formPopup.querySelector(`.form-box.login .form-content.forgot-password`);
            case 'login':
                return this.formElement = this.formPopup.querySelector(`.form-box.login .form-content.form-content-login`);
            case 'register':
                return this.formElement = this.formPopup.querySelector(`.form-box.register .form-content`);
            default:
                console.error(`Unknown form type: ${formType}`);
                return;  // Exit if an unknown form type is passed
        }
    }
    initializeForm(formType) {
        this.formElement = this.initilizeFormElements(formType);
        if (formType === 'login') {
            this.fields = ["username", "password"];
            this.validationOnsubmit(this.formElement,
                new Validator(this.fields),
                this.url_login,
                this.loginData.bind(this)
            );
        } else if (formType === 'register') {
            this.fields = ["username-register", "email-register", "password-register", "repeat-password"];
            this.validationOnsubmit(this.formElement,
                new Validator(this.fields),
                this.url_register,
                this.registerData.bind(this)
            );
        } else if (formType === 'forgot-password') {
            console.log(this.formElement);
            this.fields = ["email-forgot-password"];
            this.validationOnsubmit(this.formElement,
                new Validator(this.fields),
                this.url_forgot_password,
                this.resetPasswordData.bind(this)
            );
        }
    }
    resetPasswordData() {
        const email = document.querySelector(`#email-forgot-password`).value;
        if (email) {
            return {
                email: email,
            };
        } else {
            return null;
        }
    }
    loginData() {
        const username = document.querySelector(`#username`).value;
        const password = document.querySelector(`#password`).value;
        const rememberMe = document.querySelector(`#checkbox-remember`);
        if (username && password) {
            return {
                username: username,
                password: password,
                rememberMe: !!rememberMe.checked

            };
        } else {
            return null;
        }
    }
    registerData() {
        const username = document.querySelector(`#username-register`).value;
        const email = document.querySelector('#email-register').value;
        const password = document.querySelector(`#password-register`).value;

        if (username && email && password) {
            return {
                username: username,
                email: email,
                password: password
            };
        } else {
            return null;
        }
    }
}
