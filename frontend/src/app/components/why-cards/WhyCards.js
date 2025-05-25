// ProductCard.js
export default class {
    getHtml() {
        return `
              <section class="why-section">
            <div class="heading-container">
                <h2>WHY COOK WITH US</h2>
            </div>
            <div class="why-container">
                <div class="box">
                    <i class="fa-solid fa-plate-wheat"></i>
                    <div>
                        <h2>Diverse Recipes</h2>
                        <p>
                            Explore a vast collection of recipes spanning various cuisines and skill levels.
                            From quick and easy meals to gourmet delights, there's something for every taste.
                        </p>
                    </div>
                </div>

                <div class="box">
                    <i class="fa-solid fa-bookmark"></i>
                    <div>
                        <h2>Personalized Favorites</h2>
                        <p>
                            Save and organize your favorite recipes for quick access.
                            Build your own digital cookbook tailored to your preferences.
                        </p>
                    </div>
                </div>

                <div class="box">
                    <i class="fa-regular fa-face-smile"></i>
                    <div>
                        <h2>Step-by-Step Guides</h2>
                        <p>
                            Master the art of cooking with detailed, step-by-step instructions.
                            Our recipes come with handy tips and tricks to elevate your culinary skills.
                        </p>
                    </div>
                </div>
            </div>
        </section>
        `;
    }
}
