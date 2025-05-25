export default class AuthService {
    constructor() {
        this.userInfoUrl  = 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/auth/user-info';
        this.refreshTokenUrl  = 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/auth/refresh-token';
    }

    async isAuthenticated() {
        try {
            return await this.handleAuthRequest(this.userInfoUrl);
        } catch (error) {
            console.error("Error checking authentication status:", error);
            return false;
        }
    }

    async handleAuthRequest(url) {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include',
        });

        if (response.ok) {
            return await response.json();
        } else if (response.status === 401) {
            const isTokenRefreshed = await this.refreshToken();
            if (isTokenRefreshed) {
                return this.handleAuthRequest(this.userInfoUrl);
            }
        } else {
            const errorData = await response.json();
            throw new Error(`Failed to fetch data. Status: ${response.status}, Message: ${errorData.error}`);
        }
    }

    async refreshToken() {
        const response = await fetch(this.refreshTokenUrl, {
            method: 'POST',
            credentials: 'include',
        });
        return response.ok;
    }
}
