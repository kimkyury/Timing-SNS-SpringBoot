import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from '../../server';
// import styles from './Influence.module.css';
import { useState } from 'react';
import { AnimatedTree } from 'react-tree-graph';

function Influence() {
    const navigate = useNavigate();
    const location = useLocation();
    const id = location.state;
    const [accessToken] = useState(sessionStorage.getItem('accessToken'));
    const [state, setState] = useState(null);

    useEffect(() => {
        axios
            .get(`/api/v1/feeds/${id}/influence`, {
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
                height: 100,
                width: 100,
                href: original.thumbnailUrl,
            },
            children: original.childs.map((child) => convertObject(child)),
        };
    };

    const handleClick = (node) => {
        console.log(node);
        axios
            .get(`/api/v1/feeds/${node}`, {
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
                        onClick: (event, node) => handleClick(node),
                    }}
                />
            )}
        </div>
    );
}

export default Influence;
