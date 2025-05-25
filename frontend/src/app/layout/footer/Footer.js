export default class Footer {
    async getHtml() {
        return `
            <footer class="info_section">
                <div class="social_container">
                    <div class="social_box">
                        <a href="">
                            <i class="fa-brands fa-facebook"></i>
                        </a>
                        <a href="">
                            <i class="fa-brands fa-square-x-twitter"></i>
                        </a>
                        <a href="">
                            <i class="fa-brands fa-instagram"></i>
                        </a>
                        <a href="">
                            <i class="fa-brands fa-youtube"></i>
                        </a>
                    </div>
                </div>
                <div class="info_container">
                    <div class="info_box">
                        <div class="about_us_box">
                            <h6>
                                ABOUT US
                            </h6>
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, consectetur adipiscing elit, sed doLorem ipsum dolor sit amet,
                            </p>
                        </div>
                    </div>
                    <div class="info_box">
                        <div class="help_box">
                            <h6>
                                NEED HELP
                            </h6>
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed doLorem ipsum dolor sit amet, consectetur adipiscing elit, sed doLorem ipsum dolor sit amet,
                            </p>
                        </div>
                    </div>
                    <div class="info_box">
                        <div class="contact_box">
                            <h6>
                                CONTACT US
                            </h6>
                            <div class="info_link-box">
                                <a href="">
                                    <i class="fa fa-map-marker" aria-hidden="true"></i>
                                    <span> Gb road 123 london Uk </span>
                                </a>
                                <a href="">
                                    <i class="fa fa-phone" aria-hidden="true"></i>
                                    <span>+01 12345678901</span>
                                </a>
                                <a href="">
                                    <i class="fa fa-envelope" aria-hidden="true"></i>
                                    <span> example@gmail.com</span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="footer_section">
                    <div class="container">
                        <p>
                            &copy; <span id="displayYear"></span> All Rights Reserved By
                            <a>Michelis Nick</a>
                        </p>
                    </div>
                </div>
            </footer>
        `;
    }
}
