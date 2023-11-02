import styles from './DetailFeed.module.css';
import { useEffect, useState } from 'react';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import DeleteOutlinedIcon from '@mui/icons-material/DeleteOutlined';
import LoopOutlinedIcon from '@mui/icons-material/LoopOutlined';
import dog from '../../assets/dog.jpg';

function DetailFeed() {
    const [user, setUser] = useState(null);
    const [state, setState] = useState(null);

    useEffect(() => {
        const user = { id: '@헬린이', profile_img: `${dog}` };
        setUser(user);
        const state = {
            pk: 1,
            profileimage: `${dog}`,
            name: '하성호',
            id: '@헬린이',
            image: `${dog}`,
            isLiked: false,
            isPublic: true,
            likes: 1234567,
            comment: 765432,
            share: 1000,
            time: new Date() - 400,
            hash: ['#개', '#댕댕이', '#시바', '#산책'],
            content: '멍멍',
            comments: [
                { name: '김정희', comment: '뭐', profileimage: `${dog}`, time: new Date() - 100 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
                { name: '하성호', comment: 'ㅋㅋㅋ', profileimage: `${dog}`, time: new Date() - 10 },
            ],
        };
        setState(state);
    }, []);

    const download = () => {
        // 여기서 파일 다운로드 로직 실행
    };

    const deleteFeed = () => {
        // 여기서 피드 삭제 로직 실행
    };

    return (
        <div className={styles.container}>
            {state && (
                <>
                    <img src={state.image} className={styles.imageContainer} />
                    <div className={styles.etcbox}>
                        <div className={styles.etcinner}>
                            <LoopOutlinedIcon className={styles.etcimg} />
                            댓글보기
                        </div>
                        {user.id != state.id && (
                            <div className={styles.etcinner}>
                                <LoopOutlinedIcon className={styles.etcimg} />
                                이어받기
                            </div>
                        )}
                        {user.id == state.id && (
                            <>
                                <div className={styles.etcinner} onClick={download}>
                                    <FileDownloadOutlinedIcon className={styles.etcimg} />
                                    다운로드
                                </div>
                                <div className={styles.etcinner} onClick={deleteFeed}>
                                    <DeleteOutlinedIcon className={styles.etcimg} />
                                    삭제
                                </div>
                            </>
                        )}
                    </div>
                </>
            )}
        </div>
    );
}

export default DetailFeed;
