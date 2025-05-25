import AuthService from "../services/AuthService.js";

export default class Router {
    constructor(routes, rootElement) {
        this.routes = routes;
        this.rootElement = rootElement;
        window.addEventListener('popstate', this.handleRoute.bind(this));
        this.setupRouteListener();
        this.authService = new AuthService();
    }

    pathToRegex(path) {
        return new RegExp("^" + path.replace(/\//g, "\\/").replace(/:\w+/g, "(.+)") + "$");

    }

    getParams(match) {
        const values = match.result.slice(1);
        const keys = Array.from(match.route.path.matchAll(/:(\w+)/g)).map(result => result[1]);

        const pathParams = Object.fromEntries(keys.map((key, i) => [key, values[i]]));
        const queryParams = new URLSearchParams(window.location.search);
        //console.log(Object.fromEntries(queryParams.entries()));
        return { pathParams, queryParams: Object.fromEntries(queryParams.entries()) };
    }

    navigateTo(data, url) {
        history.pushState(data, null, url);
        this.handleRoute();
    }

    replaceState(data, url) {
        history.replaceState(data, null, url);
        this.handleRoute();
    }

    async handleRoute() {
        const potentialMatches = this.routes.map(route => ({
            route,
            result: location.pathname.match(this.pathToRegex(route.path))
        }));
        let match = potentialMatches.find(potentialMatch => potentialMatch.result !== null);
        if (!match) {
            match = {
                route: this.routes[0],
                result: [location.pathname]
            };
        }
        let isAuthenticated = await this.authService.isAuthenticated();
        console.log(isAuthenticated);
        if(match && match.route.protected && !isAuthenticated) {
            this.replaceState(null, "/");
            return;
        }

        if(match && !match.route.allowHistory && isAuthenticated){
            this.replaceState(null, "/404");
            return;
        }else if(match && !match.route.allowHistory && history.state === null){
            this.replaceState(null, "/404");
            return;
        }

        const params = this.getParams(match);
        const view = new match.route.view(params, isAuthenticated, history);
        this.rootElement.innerHTML = await view.getHtml();
        if (view.onAfterRender) {
            await view.onAfterRender();
        }
    }

    loadRoute() {
        this.handleRoute();
    }

    setupRouteListener() {
        document.body.addEventListener("click", e => {
            if (e.target.matches("[data-link]")) {
                e.preventDefault();
                this.navigateTo(null, e.target.href);
            }
        });
    }
}
