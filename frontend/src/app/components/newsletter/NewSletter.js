export default class NewSletter {

    getHtml() {
        return `
             <section class="newsletter">
            <div class="newsletter-container">
                <h2><span>Don't you want to miss our news?</span></h2>
                <form action="#">
                    <div class="input-field">
                        <input type="text" placeholder="Insert you email here" required>
                    </div>
                    <div class="btn-box">
                        <button class="filled-button medium " type="submit">Subscribe</button>
                    </div>
                </form>
            </div>
        </section>
        `;
    }
}
