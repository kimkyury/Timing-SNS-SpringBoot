import React, { useState } from 'react';
import styles from './Test.module.css';

function Tree() {
    const [leafCount, setLeafCount] = useState(5);

    // 나무 성장 로직을 구현
    const growTree = () => {
        if (leafCount < 10) {
            setLeafCount(leafCount + 1);
        }
    };

    return (
        <div className={styles.tree}>
            <div className={styles.trunk}></div>
            {[...Array(leafCount)].map((_, index) => (
                <div key={index} className={styles.leaf}></div>
            ))}
            <button onClick={growTree}>Grow Tree</button>
        </div>
    );
}
export default Tree;
