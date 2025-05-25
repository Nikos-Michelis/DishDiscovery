import Validator from "../forms/validator/Validator.js";

export default class HttpService {
    constructor() {
        this.validator  = new Validator();
    }

    async handleGetData(url) {
        return new Promise((resolve, reject) => {
            fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        return response.json().then(data => {
                            throw new Error(`Failed to fetch data. Status: ${response.status}, Message: ${data.error}`);
                        });
                    }
                })
                .then(data => {
                    resolve(data);
                })
                .catch(error => {
                    reject(error.message);
                });
        });
    }
    async handlePostData(url, data) {
        console.log(JSON.stringify(data));
        return new Promise((resolve, reject) => {
            fetch(url, {
                method: 'POST',
                body: (data) ? JSON.stringify(data) : "",
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8',
                },
                credentials: 'include',  // Include cookies with the request
            }).then(response => {
                console.log(response);
                if (response.ok) {
                    return response.json().then(async data => {
                        console.log(data);
                        return data;
                    });
                } else {
                    return response.json().then(data => {
                        console.log(data)
                        this.validator.showErrorBox(data.error);
                        throw new Error(`Failed to fetch data. Status: ${response.status}, Message: ${data.error}`);
                    });
                }
            }).then(data => {
                console.log(data);
                resolve(data);
            }).catch(error => {
                reject(error.message);
            });
        });
    }

    async handleDeleteData(url) {
        console.log(url);
        return new Promise((resolve, reject) => {
            fetch(url, {
                method: 'DELETE',
                credentials: 'include',  // Include cookies with the request
            }).then(response => {
                console.log(response);
                if (!response.ok) {
                    return response.json().then(data => {
                        console.log(data)
                        throw new Error(`Failed to fetch data. Status: ${response.status}, Message: ${data.error}`);
                    });
                }
            }).then(data => {
                console.log(data);
                resolve(data);
            }).catch(error => {
                reject(error.message);
            });
        });
    }

}



