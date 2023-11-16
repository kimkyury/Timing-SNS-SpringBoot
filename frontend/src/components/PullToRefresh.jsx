import React, { useState } from 'react';
import { CircularProgress } from '@mui/material';

const PullToRefresh = ({ onRefresh, children }) => {
    const [refreshing, setRefreshing] = useState(false);
    const [startY, setStartY] = useState(0);
    const handleTouchStart = (event) => {
        setStartY(event.touches[0].clientY);
    };

    const handleTouchMove = (event) => {
        if (refreshing) {
            return;
        }

        const moveY = event.touches[0].clientY;
        const pullDistance = moveY - startY;

        if (pullDistance > 0) {
            if (pullDistance > 80) {
                setRefreshing(true);
            }
        }

        const handleTouchEnd = () => {
            if (refreshing) {
                onRefresh();
                setRefreshing(false);
            }
            setStartY(0);
        };

        return (
            <div
                style={{
                    overflow: 'auto',
                    WebkitOverflowScrolling: 'touch',
                }}
                onTouchStart={handleTouchStart}
                onTouchMove={handleTouchMove}
                onTouchEnd={handleTouchEnd}
            >
                {refreshing && (
                    <div style={{ height: '20vw', textAlign: 'center', color: 'black' }}>
                        <CircularProgress color="inherit" style={{ width: '20vw', height: '20vw' }} />
                    </div>
                )}

                {children}
            </div>
        );
    };
};

export default PullToRefresh;
