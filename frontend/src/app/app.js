import Router from './router/Router.js';
import Home from "./views/Home.js";
import PageNotFound from "./views/error/PageNotFound.js";
import Dishes from "./views/Dishes.js";
import Contact from "./views/Contact.js";
import OtpAuth from "./views/auth/OtpAuth.js";
import Wishlist from "./views/protected/Wishlist.js";
import MyArticles from "./views/protected/MyArticles.js";
import ResetPassword from "./views/auth/ResetPassword.js";
import Dish from "./views/Dish.js";


const routes = [
    { path: "/404", view: PageNotFound,  allowHistory: true },
    { path: "/", view: Home,  allowHistory: true },
    { path: "/dishes", view: Dishes,  allowHistory: true },
    { path: "/dish/:id", view: Dish, protected: false,  allowHistory: true },
    { path: "/contact", view: Contact,  allowHistory: true },
    { path: "/wishlist", view: Wishlist, protected: true,  allowHistory: true },
    { path: "/my-articles", view: MyArticles, protected: true,  allowHistory: true },
    { path: "/account/confirmation", view: OtpAuth, allowHistory: false },
    { path: "/account/reset-password", view:  ResetPassword, allowHistory: false }
];

const rootElement = document.getElementById("app");
const router = new Router(routes, rootElement);

export default router;
