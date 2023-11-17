import { createSlice } from '@reduxjs/toolkit';
import { PURGE } from 'redux-persist';

const initialState = {
    feeds: [],
    pageID: 1,
    isStop: false,
};

const feedSlice = createSlice({
    name: 'feed',
    initialState,
    reducers: {
        setFeed: (state, action) => {
            state.feeds = [...state.feeds, ...action.payload];
        },
        setPageID: (state, action) => {
            state.pageID = action.payload;
        },
        setIsStop: (state, action) => {
            state.isStop = action.payload;
        },
        resetFeedState: (state) => {
            return initialState;
        },
    },
    extraReducers: (builder) => {
        builder.addCase(PURGE, () => {
            return initialState;
        });
    },
});

export const { setFeed, setPageID, setIsStop, resetFeedState } = feedSlice.actions;
export default feedSlice.reducer;
