import axios from 'axios';

const api = axios.create({
    baseURL: 'https://j9e203.p.ssafy.io/api/v1/private',
});

export const setAuthToken = (token) => {
    localStorage.setItem('token', token);
};

const getAuthToken = () => {
    return localStorage.getItem('token');
};

const authAxios = (config) => {
    const token = getAuthToken();

    console.log(token);

    const headers = {
        Authorization: token,
    };

    config.headers = headers;

    api(config)
        .then((response) => {
            console.log(response);
        })
        .catch((e) => {
            console.error(e);
        });
};

export default authAxios;
