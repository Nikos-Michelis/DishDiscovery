import AbstractView from "./AbstractView.js";
import NavBar from "../layout/navbar/NavBar.js";

export default class extends AbstractView {
  constructor(params, isAuthenticated) {
    super(params, isAuthenticated);
    this.setTitle("Contact");
  }
  async getContentHtml() {
    return `
           <section class="contact-section">
            <div class="contact-form-wrapper">
                <div class="heading-container">
                    <div class="letter-box">
                        <span><i class="fa-regular fa-envelope"></i></span>
                    </div>
                    <div class="title">
                        <h2>Contact</h2>
                    </div>
                </div>
                <div class="contact-form-container">
                    <div class="form-box">
                        <div class="form-content">
                            <form action="#">
                                <div class="input-field">
                                    <input type="text" id="username-contact" required>
                                    <label>Full Name</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="input-field">
                                    <input type="text" id="email-contact" required>
                                    <label>Email</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="input-field">
                                    <input type="text" id="subject-contact" required>
                                    <label>Subject</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="input-field">
                                    <textarea name="message" class="form-control" id="message" cols="30" rows="4" required></textarea>
                                    <label>Message</label>
                                    <span class="error-message"></span>
                                </div>
                                <div class="btn-box">
                                    <button type="submit">Contact</button>
                                </div>
                            </form>
                        </div>
                        <div class="map-container">
                            <div class="map-responsive">
                                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d42035.2306927685!2d23.61374191147388!3d38.457930062279594!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x14a1164bc2d6174b%3A0x380b0a5f7b34c904!2zzqfOsc67zrrOr860zrEgMzQxIDAw!5e0!3m2!1sel!2sgr!4v1721984746212!5m2!1sel!2sgr"
                                        style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        `;
  }
}
