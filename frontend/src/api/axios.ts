import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
});

// 응답 인터셉터 추가
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // 인증 오류(401)시 로그인 페이지로 리다이렉트
    if (error.response?.status === 401) {
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api; 