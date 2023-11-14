import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import { combineReducers } from '@reduxjs/toolkit';
import auth from './slices/authSlice';
import feed from './slices/feedSlice';
const persistConfig = {
    key: 'root',
    storage: storage,
    whitelist: ['auth', 'feeds'],
};

const reducer = combineReducers({
    auth,
    feed,
});

const persistedReducer = persistReducer(persistConfig, reducer);

export default persistedReducer;
