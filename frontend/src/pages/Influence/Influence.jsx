import { useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from './Influence.module.css';
import { useState } from 'react';
// import { Tree } from 'react-tree-graph';
import { AnimatedTree } from 'react-tree-graph';

const BASE_URL = `https://timingkuku.shop`;

function Influence() {
    const navigate = useNavigate();
    const [accessToken, setAccessToken] = useState(sessionStorage.getItem('accessToken'));
    const [state, setState] = useState(null);

    useEffect(() => {
        const id = 28;
        axios
            .get(`${BASE_URL}/api/v1/feeds/${id}/influence`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                const convertedObject = convertObject(response.data);
                setState(convertedObject);
            })
            .catch((error) => {
                console.error(error);
            });
    }, []);

    const convertObject = (original) => {
        return {
            name: original.id.toString(),
            label: '',
            labelProp: 'label',
            shape: 'image',
            nodeProps: {
                height: 20,
                width: 20,
                href: original.thumbnailUrl,
            },
            children: original.childs.map((child) => convertObject(child)),
        };
    };

    const handleClick = (event, node) => {
        axios
            .get(`${BASE_URL}/api/v1/feeds/${node}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                response = response.data;
                navigate(`/detailfeed/${response.id}`, { state: response });
            })
            .catch((error) => {
                console.error(error);
            });
    };

    return (
        <div>
            {state && (
                <AnimatedTree
                    data={state}
                    height={400}
                    width={300}
                    gProps={{
                        className: 'node',
                        onClick: (event, node) => handleClick(event, node),
                    }}
                />
            )}
        </div>
    );
}

export default Influence;
