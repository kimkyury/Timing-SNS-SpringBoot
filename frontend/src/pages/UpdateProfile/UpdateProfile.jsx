import styles from './UpdateProfile.module.css';
import { useEffect, useState } from 'react';
import axios from '../../server';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';

function UpdateProfile() {
    const [accessToken] = useState(sessionStorage.getItem('accessToken'));
    const [nickname, setNickName] = useState('');
    const [img, setImg] = useState(null);
    const [state, setState] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        getProfile();
    }, []);

    const getProfile = () => {
        axios
            .get(`/api/v1/members`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then((response) => {
                setState(response.data);
                setImg(response.data.profileImageUrl);
                setNickName(response.data.nickname);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    const handleFileChange = (e) => {
        const reader = new FileReader();
        reader.onloadend = () => {
            setImg(reader.result);
        };
        reader.readAsDataURL(e.target.files[0]);
    };

    const updateProfile = () => {
        // const formData = new FormData();
        // if (nickname != state.nickname) formData.append('memberUpdateRequest', { nickname: nickname });
        // if (img != state.profileImageUrl) formData.append('profileImage', img);

        const dynamicObject = new Object();
        if (nickname != state.nickname) dynamicObject.memberUpdateRequest = { nickname: nickname };
        if (img != state.profileImageUrl) dynamicObject.profileImage = img;

        axios
            .patch(`/api/v1/members`, dynamicObject, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then(() => {
                navigate('/profile');
            })
            .catch((error) => {
                console.error(error);
            });
    };
    return (
        <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -100 }}
            transition={{ duration: 0.4 }}
            className={styles.container}
        >
            <div className={styles.name}>
                <div>이름 : </div>
                <input type="text" id="text" value={nickname} onChange={(event) => setNickName(event.target.value)} />
            </div>
            <img className={styles.profileImage} src={img}></img>
            <input type="file" id="file" multiple="multipart" onChange={handleFileChange}></input>

            <button onClick={updateProfile} className={styles.summitbtn}>
                수정
            </button>
        </motion.div>
    );
}

export default UpdateProfile;
