import ResetPasswordForm from "../../forms/ResetPasswordForm.js";
import AbstractView from "../AbstractView.js";

export default class OtpAuth extends AbstractView{
    constructor(params, isAuthenticated) {
        super(params, isAuthenticated)
        this.resetPasswordForm = new ResetPasswordForm();
        this.setTitle("Reset Password");
    }
    getContentHtml() {
        const resetPasswordFormHtml = this.resetPasswordForm.getHtml();
        return `${resetPasswordFormHtml}`;
    }
}
