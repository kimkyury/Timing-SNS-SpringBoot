import toast, { Toaster } from 'react-hot-toast';

const Error = (message) => toast.error(message);
const Success = (message) => toast.success(message);
const Celebrate = (message) =>
    toast(message, {
        icon: '🎉',
    });
const Normal = (message) =>
    toast(message, {
        duration: 5000,
    });

function Toast() {
    return (
        <Toaster
            position="bottom-center"
            reverseOrder={false}
            toastOptions={{
                className: '',
                style: {
                    marginBottom: '60px',
                    zIndex: 100,
                },
            }}
        />
    );
}

export default Toast;
export { Error, Success, Celebrate, Normal };
