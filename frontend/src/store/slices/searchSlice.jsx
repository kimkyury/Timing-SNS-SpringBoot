import { createSlice } from '@reduxjs/toolkit';
import { PURGE } from 'redux-persist';

const initialState = {
    searchs: [],
    searchPageId: 1,
    isStop: false,
    tagID: 0,
};

const searchSlice = createSlice({
    name: 'search',
    initialState,
    reducers: {
        setSearch: (state, action) => {
            state.searchs = [...state.searchs, ...action.payload];
        },
        setPageID: (state, action) => {
            state.searchPageId = action.payload;
        },
        setIsStop: (state, action) => {
            state.isStop = action.payload;
        },
        setTagID: (state, action) => {
            state.tagID = action.payload;
        },
        resetsearchState: (state) => {
            return initialState;
        },
    },
    extraReducers: (builder) => {
        builder.addCase(PURGE, () => {
            return initialState;
        });
    },
});

export const { setSearch, setPageID, setIsStop, setTagID, resetsearchState } = searchSlice.actions;
export default searchSlice.reducer;
