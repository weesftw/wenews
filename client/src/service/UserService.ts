import decode from 'jwt-decode';
import {api} from 'src/boot/axios';
import { LocalStorage } from 'quasar'

class UserService {
  signOut() {
    LocalStorage.remove('token')
    LocalStorage.remove('username')
    LocalStorage.remove('roles')
    LocalStorage.remove('refreshToken')
    return true;
  }

  isSignedIn() {
    const token: string | null = LocalStorage.getItem('token');
    const refreshToken = LocalStorage.getItem('refreshToken');

    if (token == null)
      return false;

    try {
      const expiration: number = decode(token);
      const isExpired = !!expiration && Date.now() > expiration * 1000;

      if (isExpired) {
        const result = api.post('/core/oauth/access_token', {
          refresh_token: refreshToken,
          grant_type: 'refresh_token'
        });

        result.then(value => {
          localStorage.setItem('token', value.data.access_token)
        }).catch(() => {
          return this.signOut();
        });

        return true;
      }

      return true;
    } catch (_) {
      return false;
    }
  }
}

export const userService = new UserService();
