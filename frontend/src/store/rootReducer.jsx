import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import { combineReducers } from '@reduxjs/toolkit';
import auth from './slices/authSlice';
import feed from './slices/feedSlice';
import search from './slices/searchSlice';
const persistConfig = {
    key: 'root',
    storage: storage,
    whitelist: ['auth', 'feed', 'search'],
};

const reducer = combineReducers({
    auth,
    feed,
    search,
});

const persistedReducer = persistReducer(persistConfig, reducer);

export default persistedReducer;
