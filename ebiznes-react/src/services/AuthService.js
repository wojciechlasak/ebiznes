import { getUrlAuth, getRequestInitAuth, getRequestInit, getUrl } from "../utils/request";
import decode from 'jwt-decode';


export async function authenticate(provider, queryParams)  {
    return fetch(
            getUrlAuth(`authenticate/${provider}?${queryParams}`),
            getRequestInit({ method: "GET", credentials: 'include', })
    )
        .then((response) => {
            if (response.status >= 400 && response.status < 600) {
                throw new Error('Bad response from server');
            }
            return response.json();
        })
        .then((fetchedUser) => {
            return fetchedUser;
        })
        .catch(function (response) {
            console.log(response);
        });
}

export function signOut(user) {
    fetch(
        getUrl(`signout`),
        getRequestInitAuth({ method: "GET", credentials: 'include' }, user.token)
    );
}

export function getSession() {
    let token = localStorage.getItem('e-biznes-user');
    let confirm = token && decode(token);
    return confirm;
}

