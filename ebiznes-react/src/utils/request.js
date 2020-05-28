export const getUrl = (endpoint) => `http://localhost:9000/api/${endpoint}`;
export const getUrlAuth = (endpoint) => `http://localhost:9000/${endpoint}`;


export const getRequestInit = (init) => ({
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin':'http://localhost:3000',
    },
    mode: 'cors',
    ...init,
});

export const getRequestInitAuth = (init,token) => ({
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin':'http://localhost:3000',
        'X-Auth-Token': `${token}`
    },
    mode: 'cors',
    ...init,
});
