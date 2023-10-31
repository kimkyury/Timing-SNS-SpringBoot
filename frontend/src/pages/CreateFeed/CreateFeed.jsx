import { useState } from 'react';
import styles from './CreateFeed.module.css';
import Input from '@mui/joy/Input';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import Textarea from '@mui/joy/Textarea';
function CreateFeed() {
    const [tags, setTags] = useState([]);
    const [currentTag, setCurrentTag] = useState('');

    const handleTagInputChange = (event) => {
        setCurrentTag(event.target.value);
    };

    const handleTagAdd = () => {
        if (currentTag.trim() !== '') {
            setTags([...tags, '# ' + currentTag]);
            setCurrentTag('');
        }
    };

    const handleTagRemove = (index) => {
        const updatedTags = [...tags];
        updatedTags.splice(index, 1);
        setTags(updatedTags);
    };

    return (
        <div className={styles.container}>
            <div className={styles.boxname}>시작일</div>
            {/* <Input type="date" /> */}
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker className={styles.selectdate} />
            </LocalizationProvider>
            <div className={styles.boxname}>해쉬</div>
            <div className={styles.tagInputContainer}>
                <Input
                    type="text"
                    value={currentTag}
                    onChange={handleTagInputChange}
                    className={styles.inputbox}
                    placeholder="#"
                    onKeyPress={(event) => {
                        if (event.key === 'Enter') {
                            handleTagAdd();
                        }
                    }}
                />
                <button onClick={handleTagAdd} className={styles.btn}>
                    ADD
                </button>
            </div>
            <div className={styles.tagList}>
                {tags.map((tag, index) => (
                    <div key={index} className={styles.tag}>
                        {tag}
                        <button className={styles.tagbtn} onClick={() => handleTagRemove(index)}>
                            X
                        </button>
                    </div>
                ))}
            </div>
            <div className={styles.boxname}>달성목표</div>
            <Textarea minRows={4} />
            <button className={styles.btn}>제출</button>
        </div>
    );
}

export default CreateFeed;
