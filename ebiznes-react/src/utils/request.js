export const getUrl = (endpoint) => `http://localhost:9000/api/${endpoint}`;

export const getRequestInit = (init) => ({
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin':'http://localhost:3000',
    },
    mode: 'cors',
    ...init,
});