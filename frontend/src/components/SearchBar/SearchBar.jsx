import styles from './SearchBar.module.css';
import { useEffect, useState } from 'react';
import SearchIcon from '@mui/icons-material/Search';
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';
function SearchBar() {
    const wholeTextArray = ['apple', 'banana', 'coding', 'javascript', '원티드', '프리온보딩', '프론트엔드'];
    const [inputValue, setInputValue] = useState('');
    const [ishaveinputvalue, setishaveinputvalue] = useState(false);
    const [dropDownList, setDropDownList] = useState(wholeTextArray);
    const [dropDownItemIndex, setDropDownItemIndex] = useState(-1);

    const showDropDownList = () => {
        if (inputValue === '') {
            setishaveinputvalue(false);
            setDropDownList([]);
        } else {
            const choosenTextList = wholeTextArray.filter((textItem) => textItem.includes(inputValue));
            setDropDownList(choosenTextList);
        }
    };

    const changeInputValue = (event) => {
        setInputValue(event.target.value);
        setishaveinputvalue(true);
    };

    const clickDropDownItem = (clickedItem) => {
        setInputValue(clickedItem);
        setishaveinputvalue(false);
    };

    const handleDropDownKey = (event) => {
        //input에 값이 있을때만 작동
        if (ishaveinputvalue) {
            if (event.key === 'ArrowDown' && dropDownList.length - 1 > dropDownItemIndex) {
                setDropDownItemIndex(dropDownItemIndex + 1);
            }

            if (event.key === 'ArrowUp' && dropDownItemIndex >= 0) setDropDownItemIndex(dropDownItemIndex - 1);
            if (event.key === 'Enter' && dropDownItemIndex >= 0) {
                clickDropDownItem(dropDownList[dropDownItemIndex]);
                setDropDownItemIndex(-1);
            }
        }
    };

    useEffect(showDropDownList, [inputValue]);
    return (
        <div className={styles.WholeBox}>
            <div className={`${ishaveinputvalue ? styles.inputbox : styles.inputbox2}`}>
                <SearchIcon className={styles.search_icon} />
                <input
                    type="text"
                    className={styles.searchInput}
                    value={inputValue}
                    onChange={changeInputValue}
                    onKeyUp={handleDropDownKey}
                    placeholder="Search"
                />

                <div className={styles.DeleteButton} onClick={() => setInputValue('')}>
                    <CloseOutlinedIcon />
                </div>
            </div>
            {ishaveinputvalue && (
                <ul className={styles.DropDownBox}>
                    {dropDownList.length === 0 && <li>no match</li>}
                    {dropDownList.map((dropDownItem, dropDownIndex) => {
                        return (
                            <li
                                key={dropDownIndex}
                                onClick={() => clickDropDownItem(dropDownItem)}
                                onMouseOver={() => setDropDownItemIndex(dropDownIndex)}
                                className={styles.DropDownItem}
                            >
                                {dropDownItem}
                            </li>
                        );
                    })}
                </ul>
            )}
        </div>
    );
}

export default SearchBar;
