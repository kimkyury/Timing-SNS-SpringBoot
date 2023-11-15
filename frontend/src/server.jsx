import axios from 'axios';

const server = axios.create({ baseURL: import.meta.env.VITE_APP_API });

export default server;
