import axios from 'axios';

const API_URL = 'http://localhost:8081/api/users'; // Direct to user-service

export const login = async (email: string, password: string) => {
  const response = await axios.post(`${API_URL}/login`, { email, password });
  if (response.data.token) {
    localStorage.setItem('user', JSON.stringify(response.data));
  }
  return response.data;
};

export const register = async (userData: any) => {
  const response = await axios.post(`${API_URL}/register`, userData);
  return response.data;
};

export const getCurrentUser = () => {
  const userStr = localStorage.getItem('user');
  if (userStr) return JSON.parse(userStr);
  return null;
};

export const logout = () => {
  localStorage.removeItem('user');
};

export const getAuthHeader = () => {
  const user = getCurrentUser();
  if (user && user.token) {
    return { 'x-auth-token': user.token };
  }
  return {};
};
