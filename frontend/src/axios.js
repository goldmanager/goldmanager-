import axios from 'axios';

function getCsrfToken() {
    const csrfToken = document.cookie.split('; ').find(row => row.startsWith('XSRF-TOKEN='));
    return csrfToken ? csrfToken.split('=')[1] : '';
}

const csrfToken = getCsrfToken();

const instance = axios.create({
  baseURL: (process.env.VUE_APP_API_BASE_URL ?? "") +"/api/",
  headers: {
         'X-XSRF-TOKEN': csrfToken
  },
  withCredentials: true
});



instance.interceptors.request.use(config => {
  const token = sessionStorage.getItem('jwt-token');
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
 
  return config;
});


export default instance;

