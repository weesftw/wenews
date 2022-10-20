<template>
  <HeaderBar/>
  <main>
    <div class="row">
      <div class="col-lg-9">
        <div id="sender-page" class="card shadow">
          <div class="card-header text-center fw-bolder">
            Chat
          </div>
          <div class="card-body">
            <div class="justify-content-center sender-container">
              <ul id="sender"></ul>
            </div>
            <div class="input-group clearfix">
              <input @keyup.enter="handleChat" id="message" type="text" placeholder="Type a message..."
                     autocomplete="off" class="form-control"/>
              <button @click="handleChat" id="send" class="primary">Send</button>
            </div>
          </div>
          <div class="mt-3 card-footer text-muted text-end">
            <button id="online-button" type="button" class="btn link btn-sm" data-bs-toggle="modal" data-bs-target="#exampleModal">
              {{ users.length }} {{ users.length > 1 ? 'users' : 'user'}} connected
            </button>
            <!-- Modal -->
            <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">Online</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <div class="justify-content-center sender-container">
                      <ul class="list-group">
                        <li class="list-group-item d-flex justify-content-between align-items-center" v-for="user in users" v-bind:key="user">
                          <span class="badge badge-pill" :style="{ backgroundColor: user.color }">{{ user.initial }}</span>
                          {{ user.username }}
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-3">
        <div id="online-page" class="card shadow">
          <div class="card-header text-center fw-bolder">
            Online
          </div>
          <div class="card-body">
            <div class="justify-content-center sender-container">
              <ul class="list-group">
                <li class="list-group-item d-flex justify-content-between align-items-center" v-for="user in users" v-bind:key="user">
                  <span class="badge badge-pill" :style="{ backgroundColor: user.color }">{{ user.initial }}</span>
                  {{ user.username }}
                </li>
              </ul>
            </div>
          </div>
          <div class="card-footer text-muted text-end"></div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import HeaderBar from '../components/HeaderBar.vue'

export default {
  name: "ChatView",
  components: {
    HeaderBar
  },

  data() {
    return {
      webSocket: null,
      users: []
    }
  },

  mounted() {
    const username = localStorage.getItem('username');
    const url = "ws://" + location.hostname + ":8081/";
    console.log("url: " + url)
    if (username) {
      this.webSocket = new WebSocket(url + username + "/" + this.$route.query.q);
      this.webSocket.onmessage = (event) => {
        let parsedMessage = JSON.parse(event.data);
        const usersConnected = parsedMessage.connectedUsers;
        this.updateChat(parsedMessage);
        if(usersConnected)
          this.addOnlineUser(usersConnected);
      }
    }
  },

  methods: {
    addOnlineUser(users) {
      this.users = [];
      users.forEach(value => {
        this.users.push({
          username: value,
          initial: value[0],
          color: this.getAvatarColor(value)
        })
      })
    },

    handleChat() {
      this.sendMessage(document.getElementById("message").value);
    },

    getAvatarColor(messageSender) {
      const colors = [
        '#2196F3', '#32c787', '#00BCD4', '#ff5652',
        '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
      ]

      let hash = 0;
      for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
      }

      const index = Math.abs(hash % colors.length);
      return colors[index];
    },

    sendMessage(message) {
      this.webSocket.send(message);
      document.getElementById("message").value = "";
    },

    //Update the sender-panel, and the list of connected users
    updateChat(msg) {
      this.insert(msg);
    },

    //Helper function for inserting HTML as the first child of an element
    insert(message) {
      const messageArea = document.querySelector('#sender');
      const messageElement = document.createElement('li');
      messageElement.classList.add('sender-message');

      const avatarElement = document.createElement('i');
      const avatarText = document.createTextNode(message.username[0]);
      avatarElement.appendChild(avatarText);
      avatarElement.style['background-color'] = this.getAvatarColor(message.username);

      messageElement.appendChild(avatarElement);

      const usernameElement = document.createElement('span');
      const usernameText = document.createTextNode(message.username);
      usernameElement.appendChild(usernameText);
      messageElement.appendChild(usernameElement);

      var textElement = document.createElement('p');

      if(message.username === "Console")
        textElement.style.fontWeight = 'bold';

      const messageText = document.createTextNode(message.content);
      textElement.appendChild(messageText);

      messageElement.appendChild(textElement);

      messageArea.appendChild(messageElement);
      messageArea.scrollTop = messageArea.scrollHeight
    }
  }
}
</script>

<style>
* {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
}

.clearfix:after {
  display: block;
  content: "";
  clear: both;
}

.hidden {
  display: none;
}

.form-control {
  width: 100%;
  min-height: 38px;
  font-size: 15px;
  border: 1px solid #c8c8c8;
}

.form-group {
  margin-bottom: 15px;
}

input {
  padding-left: 10px;
  outline: none;
}

h1, h2, h3, h4, h5, h6 {
  margin-top: 20px;
  margin-bottom: 20px;
}

h1 {
  font-size: 1.7em;
}

a {
  color: #128ff2;
}

button {
  box-shadow: none;
  border: 1px solid transparent;
  font-size: 14px;
  outline: none;
  line-height: 100%;
  white-space: nowrap;
  vertical-align: middle;
  padding: 0.6rem 1rem;
  border-radius: 2px;
  transition: all 0.2s ease-in-out;
  cursor: pointer;
  min-height: 38px;
}

button.default {
  background-color: #e8e8e8;
  color: #333;
  box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.12);
}

button.primary {
  background-color: #128ff2;
  box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.12);
  color: #fff;
}

button.accent {
  background-color: #ff4743;
  box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.12);
  color: #fff;
}

.username-page-container .username-submit {
  margin-top: 10px;
}

#sender-page {
  position: relative;
  height: 100%;
}

.sender-container {
  height: calc(100% - 30px);
  max-height: 360px;
}

#sender-page ul {
  list-style-type: none;
  background-color: #FFF;
  overflow: auto;
  padding: 0 20px 0px 20px;
  height: calc(100% - 20px);
}

#sender-page #messageForm {
  padding: 20px;
}

#sender-page ul li {
  line-height: 1.5rem;
  padding: 10px 20px;
  margin: 0;
  border-bottom: 1px solid #f4f4f4;
}

#sender-page ul li p {
  margin: 0;
}

#sender-page .event-message {
  width: 100%;
  text-align: center;
  clear: both;
}

#sender-page .event-message p {
  color: #777;
  font-size: 14px;
  word-wrap: break-word;
}

#sender-page .sender-message {
  padding-left: 68px;
  position: relative;
}

#sender-page .sender-message i {
  position: absolute;
  width: 42px;
  height: 42px;
  overflow: hidden;
  left: 10px;
  display: inline-block;
  vertical-align: middle;
  font-size: 18px;
  line-height: 42px;
  color: #fff;
  text-align: center;
  border-radius: 50%;
  font-style: normal;
  text-transform: uppercase;
}

#sender-page .sender-message span {
  color: #333;
  font-weight: 600;
}

#sender-page .sender-message p {
  color: #43464b;
}

.input-group {
  flex-wrap: unset;
}

#messageForm .input-group input {
  float: left;
  width: calc(100% - 85px);
}

#messageForm .input-group button {
  float: left;
  width: 80px;
  height: 38px;
  margin-left: 5px;
}

.sender-header {
  text-align: center;
  padding: 15px;
  border-bottom: 1px solid #ececec;
}

.sender-header h2 {
  margin: 0;
  font-weight: 500;
}

.connecting {
  padding-top: 5px;
  text-align: center;
  color: #777;
  position: absolute;
  top: 65px;
  width: 100%;
}

#online-button {
  display: none;
}

@media screen and (max-width: 991px) {

  #online-page {
    display: none;
  }

  #online-button {
    display: contents;
    font-weight: bold;
  }
}

@media screen and (max-width: 730px) {

  .sender-container {
    max-height: 350px;
  }
}

@media screen and (max-width: 480px) {
  .sender-container {
    height: calc(100% - 30px);
  }

  .username-page-container {
    width: auto;
    margin-left: 15px;
    margin-right: 15px;
    padding: 25px;
  }

  #messageForm .input-group button {
    width: 65px;
  }

  #messageForm .input-group input {
    width: calc(100% - 70px);
  }

  .sender-header {
    padding: 10px;
  }

  .connecting {
    top: 60px;
  }

  .sender-header h2 {
    font-size: 1.1em;
  }
}
</style>