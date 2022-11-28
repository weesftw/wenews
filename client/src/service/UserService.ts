import decode from 'jwt-decode'
import {api} from 'src/boot/axios'
import {LocalStorage, SessionStorage} from 'quasar'
import {useUserStore} from 'stores/UserStore';
import {User} from 'src/model/User';

interface UserLogin {
  username: string
  password: string
}

interface CreateUser {
  firstName: string
  lastName: string
  email: string
  imageUrl: string
  username: string
  password: string
}

interface UserResponse {
  user: User | null
  message: string
}

interface BanResponse {
  message: string
}

interface AllUsers {
  id: string,
  firstName: string
  lastName: string
  email: string
  username: string
  banned: boolean
}

class UserService {
  getAllUsers() {
    const userConnected = this.getUserConnected();
    return new Promise<AllUsers[]>(function(myResolve, myReject) {
      api.get('/core/v1/users', {
        headers: { Authorization: 'Bearer ' + userConnected?.token }
      }).then(value => {
        myResolve(value.data)
      }).catch(reason => {
        myReject({ message: reason.response.data.message })
      })
    });
  }

  banUser(username: string) {
    const userConnected = this.getUserConnected();
    return new Promise<BanResponse>(function(myResolve, myReject) {
      api.get('/core/v1/user/ban/' + username, {
        headers: { Authorization: 'Bearer ' + userConnected?.token }
      }).then(() => {
        myResolve({ message: `User ${username} has banned` })
      }).catch(reason => {
        myReject({ message: reason.response.data.message })
      })
    });
  }

  signInRequest(user: UserLogin) {
    return new Promise<UserResponse>(function(myResolve, myReject) {
      api.post('/core/login', user).then(value => {
        const userModel: User = {
          banned: value.data.banned,
          imageUrl: value.data.imageUrl,
          username: value.data.username,
          token: value.data.access_token,
          refreshToken: value.data.refresh_token,
          roles: value.data.roles
        };

        LocalStorage.set('user', <User>userModel)
        SessionStorage.set('user', <User>userModel)
        myResolve({ message: 'Successfully logged in', user: userModel})
      }).catch(reason => {
        myReject({ message: reason.response.data.message, user: null})
      });
    });
  }

  registerUserRequest(user: CreateUser) {
    return new Promise<UserResponse>(function (myResolve, myReject) {
      return api.post('/core/v1/user', user).then(() => {
        myResolve({ message: 'Successfully registered', user: null } )
      }).catch(reason => {
        myReject({ message: reason.response.data.errors[0], user: null} )
      });
    })
  }

  signOut() {
    LocalStorage.remove('user')
    SessionStorage.remove('user')
    useUserStore().auth = false;
  }

  isSignedIn() {
    const user = this.getUserConnected()
    const token = user?.token
    const refreshToken = user?.refreshToken

    if (token == null)
      return false;

    try {
      const expiration: { exp: number } = decode(token);
      const isExpired = !!expiration && Date.now() > expiration.exp * 1000;

      if (isExpired) {
        throw Error();

        api.post('/core/oauth/access_token', {
          refresh_token: refreshToken,
          grant_type: 'refresh_token'
        }).then(req => {
          useUserStore().fetchUser()
          LocalStorage.set('token', req.data.access_token)
        }).catch(reason => {
          throw new Error('Error: ' + reason)
        });
      }

      return true;
    } catch (_) {
      this.signOut()
      return false;
    }
  }

  getUserConnected() {
    const user = LocalStorage.getItem<User>('user');
    if(user != undefined) {
      return user
    } else {
      const userSession = SessionStorage.getItem<User>('user')
      if(userSession != undefined)
        return userSession
    }

    return null
  }
}

export const userService = new UserService();
