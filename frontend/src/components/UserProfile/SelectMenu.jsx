import styles from './UserProfile.module.css';
import axios from 'axios';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import { useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import IconButton from '@mui/material/IconButton';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import { padding } from '@mui/system';

function SelectMenu() {
    const navigate = useNavigate();
    const BASE_URL = `http://k9e203.p.ssafy.io`;
    const [accessToken] = useState(sessionStorage.getItem('accessToken'));

    const location = useLocation();

    const [anchorEl, setAnchorEl] = useState(null);
    const open = Boolean(anchorEl);
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    const goToUpdateProfile = () => {
        navigate(`/updateProfile`);
    };

    const deleteProfile = () => {
        axios
            .delete(`${BASE_URL}/api/v1/members`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            })
            .then(() => {
                navigate('/login');
            })
            .catch((error) => {
                console.error(error);
            });
    };

    return (
        <div>
            <IconButton
                aria-label="more"
                id="long-button"
                aria-controls={open ? 'basic-menu' : undefined}
                aria-expanded={open ? 'true' : undefined}
                aria-haspopup="true"
                onClick={handleClick}
                style={{ padding: 0 }}
            >
                <MoreVertIcon />
            </IconButton>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                <MenuItem onClick={goToUpdateProfile}>회원정보 수정</MenuItem>
                <MenuItem onClick={deleteProfile}>회원 탈퇴</MenuItem>
            </Menu>
        </div>
    );
}

export default SelectMenu;
