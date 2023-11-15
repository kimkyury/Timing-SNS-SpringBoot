import axios from 'axios';
import mem from 'mem';

const server = axios.create({ baseURL: import.meta.env.VITE_APP_API });

const REFRESH_URL = '/api/v1/auth/reissue';

server.interceptors.request.use((config) => {
    if (!config.headers) return config;

    let token = null;

    if (config.url !== REFRESH_URL) {
        token = sessionStorage.getItem('accessToken');
    }

    if (token !== null) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

const getRefreshToken = mem(
    async () => {
        try {
            const {
                data: { accessToken },
            } = await server.post(REFRESH_URL, { withCredentials: true });

            sessionStorage.setItem('accessToken', accessToken);

            return accessToken;
        } catch (e) {
            sessionStorage.removeItem('accessToken');
        }
    },
    { maxAge: 1000 }
);

server.interceptors.response.use(
    (res) => res,
    async (err) => {
        const {
            config,
            response: { status },
        } = err;

        if (config.url === REFRESH_URL || status !== 401 || config.sent) {
            return Promise.reject(err);
        }

        config.sent = true;
        const accessToken = await getRefreshToken();

        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
            return server(config);
        }

        return Promise.reject(err);
    }
);

export default server;
