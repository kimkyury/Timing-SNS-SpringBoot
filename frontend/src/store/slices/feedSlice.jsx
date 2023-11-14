import { createSlice } from '@reduxjs/toolkit';
import { PURGE } from 'redux-persist';

const initialState = {
    feeds: [],
};

const feedSlice = createSlice({
    name: 'feed',
    initialState,
    reducers: {
        setFeed: (state, action) => {
            state.feeds = [...state.feeds, ...action.payload];
        },
    },
    extraReducers: (builder) => {
        builder.addCase(PURGE, () => {
            return initialState;
        });
    },
});

export const { setFeed } = feedSlice.actions;
export default feedSlice.reducer;
