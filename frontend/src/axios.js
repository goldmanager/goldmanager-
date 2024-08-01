import axios from 'axios';
import router from '@/router'; // Importieren Sie Ihren Vue Router

const instance = axios.create({
	baseURL: (process.env.VUE_APP_API_BASE_URL ?? "") + "/api/",
	withCredentials: true
});


instance.interceptors.request.use(config => {
	const token = sessionStorage.getItem('jwt-token');
	if (token) {
		config.headers['Authorization'] = `Bearer ${token}`;
	}
	return config;
}, error => {
	return Promise.reject(error);
});

instance.interceptors.response.use(response => {

	return response;
}, error => {

	if (error.response && error.response.status === 403) {

		if (router.currentRoute.path !== '/login') {
			sessionStorage.removeItem('jwt-token');
			sessionStorage.removeItem('username');
			sessionStorage.removeItem('jwtExp');
			router.push({ path: '/login' });
		}
	}
	return Promise.reject(error);
});

export default instance;