import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { store, persistor } from './store/store.jsx';
import TokenRefresher from './components/TokenRefresher';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')).render(
    <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
            {/* <CookiesProvider> */}
            <BrowserRouter>
                {/* <TokenRefresher /> */}
                <App />
            </BrowserRouter>
            {/* </CookiesProvider> */}
        </PersistGate>
    </Provider>
);
