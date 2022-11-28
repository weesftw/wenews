import {defineStore} from 'pinia';
import {User} from 'src/model/User';
import {userService} from 'src/service/UserService';
import {socketService} from 'src/service/SocketService';

interface UserState {
  auth: boolean
  user: User | null
  socket: WebSocket | null
}

export const useUserStore = defineStore('user', {
    state: (): UserState => {
      return {
        auth: userService.isSignedIn() || false,
        user: userService.getUserConnected() || null,
        socket: null
      }
    },

  getters: {
    isAuth: (state) => state.auth,
    getUser: (state) => state.user,
    getSocket: (state) => state.socket
  },

  actions: {
    fetchUser() {
      this.auth = userService.isSignedIn()
      this.user = userService.getUserConnected()
    },

    connectSocket(params: string) {
      if(this.user != null) {
        this.socket = socketService.connect(this.user, params);
      }
    },

    disconnectSocket() {
      if(this.socket != null) {
        this.socket.close()
      }
    }
  }
})
