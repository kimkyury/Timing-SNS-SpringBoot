import styles from "./CreateFeed.module.css";
import Input from "@mui/joy/Input";
import Textarea from "@mui/joy/Textarea";
import Calendar from "react-calendar";
import moment from "moment";
import { useEffect, useState } from "react";
import axios from "axios";
import "./Calendar.css";
function CreateFeed() {
  const BASE_URL = `http://k9e203.p.ssafy.io`;
  const [accessToken] = useState(sessionStorage.getItem("accessToken"));
  const [tags, setTags] = useState([]);
  const [currentTag, setCurrentTag] = useState("");
  const [value, onChange] = useState(new Date());
  const [contentValue, setContentValue] = useState("");
  const handleTagInputChange = (event) => {
    setCurrentTag(event.target.value);
  };

    const handleTagAdd = () => {
        if (currentTag.trim() !== '') {
            setTags([...tags, currentTag]);
            setCurrentTag('');
        }
    };

    const handleTagRemove = (index) => {
        const updatedTags = [...tags];
        updatedTags.splice(index, 1);
        setTags(updatedTags);
    };
    const createChallenge = () => {
        const accessToken = sessionStorage.getItem('accessToken');
        console.log(accessToken);
        console.log(value.toISOString().split('T')[0]);
        console.log(tags);
        console.log(contentValue.target.value);
        axios
            .post(
                `${BASE_URL}/api/v1/challenges`,
                {
                    startedAt: value.toISOString().split('T')[0],
                    hashTags: tags,
                    goalContents: contentValue.target.value,
                },
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                }
            )
            .then(() => {
                navigate('/');
            })
            .catch((error) => {
                console.error(error);
            });
    };
    useEffect(() => {
        console.log(tags);
        console.log(value.toDateString());
        console.log(typeof tags);
    }, [value]);
    return (
        <div className={styles.container}>
            <div className={styles.boxname}>시작일</div>
            <Calendar
                onChange={onChange}
                value={value}
                className={styles.calender}
                minDate={new Date()}
                calendarType={'iso8601'}
            />
            <div>{moment(value).format('YYYY년 MM월 DD일')} </div>
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
                    추가
                </button>
            </div>
            <div className={styles.tagList}>
                {tags.map((tag, index) => (
                    <div key={index} className={styles.tag}>
                        #{tag}
                        <button className={styles.tagbtn} onClick={() => handleTagRemove(index)}>
                            X
                        </button>
                    </div>
                ))}
            </div>
            <div className={styles.boxname}>달성목표</div>
            <Textarea minRows={4} className={styles.contentbox} onChange={setContentValue} />
            <button className={styles.summitbtn} onClick={createChallenge}>
                챌린지 시작하기
            </button>
        </div>
    );
}

export default CreateFeed;
