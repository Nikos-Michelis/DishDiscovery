import router from "../app.js";

export default class UrlManager {
    constructor(queryParams = {}) {
        this.queryParams = new URLSearchParams(queryParams);
    }

    setQueryParam(key, value) {
        this.queryParams.set(key, value);
        this.updateUrl(this.getQueryParamsObject());
    }

    removeQueryParam(key) {
        this.queryParams.delete(key);
        this.updateUrl(this.getQueryParamsObject());
    }

    clearQueryParams() {
        this.queryParams = new URLSearchParams();
        this.updateUrl(this.getQueryParamsObject());
    }

    updateUrl(queryParams) {
        console.log(queryParams);
        const queryParamsObject = { ...queryParams };
        const limit = queryParamsObject.limit;
        const page = queryParamsObject.page;
        delete queryParamsObject.limit;
        delete queryParamsObject.page;

        const urlSearchParams = new URLSearchParams(queryParamsObject);

        if (limit !== undefined) {
            urlSearchParams.append('limit', limit);
        }
        if (page !== undefined) {
            urlSearchParams.append('page', page);
        }
      history.replaceState(queryParams, null, window.location.pathname + '?' + urlSearchParams.toString());
    }


    getQueryParamsObject() {
        return Object.fromEntries(this.queryParams.entries());
    }

    setQueryParamsObject(newQueryParams) {
        this.queryParams = new URLSearchParams(newQueryParams);
    }
}
