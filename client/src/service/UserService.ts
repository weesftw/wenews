import {User} from 'src/model/User';

const axios = require('axios')
const decode = require('jwt-decode');

function createUser(user: User) {
  return axios.post('/core/user', user);
}

function signIn(user: User) {
  return axios.post('/core/login', {
    username: user.username,
    password: user.password
  });
}

function signOut() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('roles')
  localStorage.removeItem('refreshToken')
}

function isSignedIn() {
  const token = localStorage.getItem('token');
  const refreshToken = localStorage.getItem('refreshToken');

  if (!token)
    return false;

  try {
    const {exp: expiration} = decode(token);
    const isExpired = !!expiration && Date.now() > expiration * 1000;

    if (isExpired) {
      const result = Promise.resolve(axios.post('/core/oauth/access_token', {
        refresh_token: refreshToken,
        grant_type: 'refresh_token'
      }));

      result.then(value => {
        localStorage.setItem('token', value.data.access_token)
      }).catch(() => {
        signOut();
        return false;
      });

      return true;
    }

    return true;
  } catch (_) {
    return false;
  }
}

function isAdmin() {
  return localStorage.getItem('roles') === 'ROLE_ADMIN'
}

export default {
  createUser,
  signIn,
  signOut,
  isSignedIn,
  isAdmin
}
