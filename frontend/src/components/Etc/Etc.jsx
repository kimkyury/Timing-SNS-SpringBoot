import styles from './Etc.module.css';
import * as React from 'react';
import Button from '@mui/material/Button';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Fade from '@mui/material/Fade';
import MoreHorizRoundedIcon from '@mui/icons-material/MoreHorizRounded';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import DeleteOutlinedIcon from '@mui/icons-material/DeleteOutlined';
import LoopOutlinedIcon from '@mui/icons-material/LoopOutlined';
function Etc() {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <div>
            <Button
                id="fade-button"
                aria-controls={open ? 'fade-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                onClick={handleClick}
            >
                <MoreHorizRoundedIcon />
            </Button>
            <Menu
                id="fade-menu"
                MenuListProps={{
                    'aria-labelledby': 'fade-button',
                }}
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                TransitionComponent={Fade}
            >
                <MenuItem onClick={handleClose}>
                    <DeleteOutlinedIcon />
                    삭제
                </MenuItem>
                <MenuItem onClick={handleClose}>
                    <FileDownloadOutlinedIcon />
                    다운로드
                </MenuItem>
                <MenuItem onClick={handleClose}>
                    <LoopOutlinedIcon />
                    이어하기
                </MenuItem>
            </Menu>
        </div>
    );
}
export default Etc;
