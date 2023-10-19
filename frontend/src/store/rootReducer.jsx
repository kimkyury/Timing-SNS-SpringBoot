import { persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";
import { combineReducers } from "@reduxjs/toolkit";
import auth from "./slices/authSlice";
const persistConfig = {
  key: "root",
  storage: storage,
  whitelist: [
    "auth",
  ],
};

const reducer = combineReducers({
  auth,
});

const persistedReducer = persistReducer(persistConfig, reducer);

export default persistedReducer;