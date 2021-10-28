export class TokenHelper {
    static getAccessToken() {
        return localStorage.getItem('access_token');
    }

    static getRefreshToken() {
        return localStorage.getItem('refresh_token');
    }

    static setAccessToken(token: string) {
        localStorage.setItem('access_token', token);
    }

    static setRefreshToken(token: string) {
        localStorage.setItem('refresh_token', token);
    }

    static removeAccessToken() {
        localStorage.removeItem('access_token');
    }

    static removeRefreshToken() {
        localStorage.removeItem('refresh_token');
    }
} 