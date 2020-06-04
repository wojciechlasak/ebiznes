import Cookies from 'js-cookie';

export function getToken() {
    let csrf = Cookies.get("csrfToken")
    console.log(csrf)
    return csrf;
}