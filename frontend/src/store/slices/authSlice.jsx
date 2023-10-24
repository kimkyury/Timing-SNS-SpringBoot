import { createSlice } from '@reduxjs/toolkit';
import { PURGE } from 'redux-persist';

const initialState = {
    accessToken: '',
    refreshToken: '',
    name: '',
    userId: '',
    image: '',
};

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setAuth: (state, action) => {
            state.accessToken = action.payload.accessToken;
            state.refreshToken = action.payload.refreshToken;
            state.name = action.payload.name;
            state.userId = action.payload.userId;
            state.image = action.payload.image;
        },
    },
    extraReducers: (builder) => {
        builder.addCase(PURGE, () => {
            return initialState;
        });
    },
});

export const { setAuth } = authSlice.actions;
export default authSlice.reducer;
