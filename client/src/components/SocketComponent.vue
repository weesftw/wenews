<template>
  <div>
    <q-tab-panels
      v-model="tab"
      animated
      vertical
    >
      <q-tab-panel name="chat">
        <div id="socket-content">

          <div class="col-6">
            <q-card flat bordered class="bg-grey-1">
              <q-card-section>
                <div class="row items-center no-wrap">
                  <div class="col">
                    <div class="text-h6">{{
                        $route.params.id.charAt(0).toUpperCase() + $route.params.id.slice(1)
                      }}
                    </div>
                  </div>

                  <div class="col-auto">
                    <q-btn color="grey-7" round flat icon="more_vert">
                      <q-menu cover auto-close>
                        <q-list>
                          <q-item clickable>
                            <q-item-section @click="fixed = true">Users</q-item-section>
                          </q-item>
                          <q-item clickable>
                            <q-item-section>Share</q-item-section>
                          </q-item>
                        </q-list>
                      </q-menu>
                    </q-btn>
                  </div>
                </div>
              </q-card-section>

              <q-separator/>

              <q-card-section>
                <div class="q-pa-md row justify-center">
                  <div style="width: 100%">
                    <q-scroll-area id="chatArea" ref="chatScroll">
                      <div class="q-pr-lg">
                        <q-chat-message v-for="message in messages" :stamp="message.dateTime"
                                        :name="message.socket.user.username"
                                        :text="[message.content]"
                                        :sent="message.socket.user.username === getUser?.username"
                                        :key="message.socket.user.username"
                                        :bg-color="message.bgColor"/>
                      </div>
                    </q-scroll-area>
                  </div>
                </div>
              </q-card-section>

              <q-separator/>

              <q-form @submit.prevent="handle">
                <q-card-actions>
                  <q-toolbar class="bg-grey-3 text-black row">
                    <q-btn round flat icon="insert_emoticon" class="q-mr-sm"/>
                    <q-input ref="inputFocus" type="text" rounded outlined dense class="WAL__field full-width q-mr-sm"
                             bg-color="white" v-model="messageContent" placeholder="Type a message" maxlength="255"/>
                    <q-btn round flat type="submit" icon="send"/>
                  </q-toolbar>
                </q-card-actions>
              </q-form>
            </q-card>
          </div>
        </div>
      </q-tab-panel>

      <q-tab-panel name="news">
        <NewsComponent :articles="articles"/>
      </q-tab-panel>
    </q-tab-panels>

    <q-separator/>

    <q-tabs
      v-model="tab"
      dense
      indicator-color="transparent"
      align="justify"
      narrow-indicator
    >
      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-fab padding="none xl" color="primary" icon="keyboard_arrow_up" direction="up">
          <q-fab-action color="primary">
            <q-tab name="chat" icon="chat" style="width: 10px"/>
          </q-fab-action>
          <q-fab-action color="secondary">
            <q-tab name="news" icon="newspaper" style="width: 10px"/>
          </q-fab-action>
        </q-fab>
      </q-page-sticky>
    </q-tabs>

    <q-dialog v-model="fixed">
      <q-card>
        <q-card-section>
          <div class="text-h6">Users:</div>
        </q-card-section>

        <q-separator/>

        <q-card-section style="max-height: 50vh" class="scroll">
          <q-item clickable v-ripple v-for="user in usersConnected" v-bind:key="user">
            <q-item-section avatar>
              <q-avatar>
                <img :src="user.user.imageUrl">
              </q-avatar>
            </q-item-section>
            <q-item-section>{{ user.user.username }}</q-item-section>
          </q-item>
        </q-card-section>

        <q-separator/>

        <q-card-actions align="right">
          <q-btn flat label="Close" color="primary" v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue'
import {QScrollArea} from 'quasar'
import {useRouter} from 'vue-router'
import NewsComponent from 'components/NewsComponent.vue'
import {useUserStore} from 'stores/UserStore';
import {storeToRefs} from 'pinia';
import {userService} from 'src/service/UserService';
import {News, Socket, SocketContent, SocketEvent, socketService } from 'src/service/SocketService';

export default defineComponent({
  name: 'SocketComponent',
  components: {NewsComponent},

  created() {
    this.userState.connectSocket(<string>this.$route.params.id)
    const userConnected = this.getUser;
    const connection = this.socket;

    if(userConnected != null && connection != null) {
      connection.onmessage = (event) => {
        const result = socketService.parse(event.data)
        const content = result.content
        const properties = result.properties

        if (result.event == SocketEvent.BAN || result.content.socket.user.banned) {
          this.$q.notify({
            color: 'negative',
            position: 'top',
            message: content.content,
            icon: 'report_problem',
            timeout: 9999
          })

          connection.close();
          this.user.signOut();
          this.router.push('/');
          return;
        }

        if (result.event == SocketEvent.LOAD_NEWS || result.event == SocketEvent.NEW_NEWS) {
          if (properties.news != null) {
            properties.news.forEach(value => this.articles.unshift(value))
          }
          return;
        }

        if (result.event == SocketEvent.USER_JOINED) {
          this.usersConnected = []
          this.usersConnected = properties.users
          return;
        }

        if (result.event == SocketEvent.USER_LEFT) {
          this.usersConnected = []
          this.usersConnected = properties.users
          return;
        }

        this.messages.push(content)
        this.scrollDown()
      }

      connection.onerror = () => {
        this.router.push('/')
      }
    }
  },

  methods: {
    handle: function () {
      if (this.messageContent && this.messageContent.length > 0) {
        this.socket?.send(this.messageContent);
        this.messageContent = ''
        this.scrollDown()
      } else {
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Message has to have some content.',
          icon: 'report_problem'
        })
      }
    },

    scrollDown() {
      const scrollArea: QScrollArea = <QScrollArea>this.$refs.chatScroll;
      const scrollTarget = scrollArea.getScrollTarget();
      const duration = 300; // ms - use 0 to instant scroll
      scrollArea.setScrollPosition('vertical', scrollTarget.scrollHeight, duration);
    }
  },

  setup() {
    const user = userService;
    const userState = useUserStore()
    const router = useRouter();
    const inputFocus = ref();
    const messageContent = ref('')
    const { getUser, socket } = storeToRefs(userState)

    const articles = <News[]><unknown>ref([]);
    let usersConnected = <Socket[]><unknown>ref([]);
    const messages = <SocketContent[]><unknown>ref([]);

    return {
      user,
      router,
      getUser,
      articles,
      messages,
      inputFocus,
      userState,
      messageContent,
      usersConnected,
      fixed: ref(false),
      tab: ref('chat'),
      splitterModel: ref(20),
      socket,
    }
  }
})
</script>

<style lang="sass" scoped>
#socket-content
  padding-left: 100px
  padding-right: 100px

#chatArea
  height: 550px

.custom-caption
  text-align: center
  padding: 12px
  color: white
  background-color: rgba(0, 0, 0, .3)

@media screen and (max-width: 699px)
  #chatArea
    height: 370px
  #socket-content
    padding-left: 15px
    padding-right: 15px
  .custom-caption
    padding-left: 15px
    padding-right: 15px
</style>
