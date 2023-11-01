import styles from './DetailFeed.module.css';
import { useLocation } from 'react-router-dom';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import DeleteOutlinedIcon from '@mui/icons-material/DeleteOutlined';
import LoopOutlinedIcon from '@mui/icons-material/LoopOutlined';
function DetailFeed() {
    const location = useLocation();
    const state = location.state;
    const user = { id: '@abcd' };
    console.log(state.id);
    return (
        <div className={styles.container}>
            <div>
                <img src={state.image} className={styles.imageContainer} />
            </div>
            <div className={styles.etcbox}>
                {user.id == state.id ? (
                    <div className={styles.etcinner}>
                        <LoopOutlinedIcon className={styles.etcimg} />
                        이어가기
                    </div>
                ) : (
                    <div className={styles.etcinner}>
                        <LoopOutlinedIcon className={styles.etcimg} />
                        이어받기
                    </div>
                )}
                <div className={styles.etcinner}>
                    <FileDownloadOutlinedIcon className={styles.etcimg} />
                    다운로드
                </div>
                {user.id == state.id ? (
                    <div className={styles.etcinner}>
                        <DeleteOutlinedIcon className={styles.etcimg} />
                        삭제
                    </div>
                ) : (
                    <></>
                )}
            </div>
        </div>
    );
}

export default DetailFeed;
