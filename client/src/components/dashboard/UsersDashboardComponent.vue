<template>
  <div class="q-mt-lg card-content">
    <q-card>
      <q-card-section class="q-pa-none">
        <q-table
          flat
          bordered
          class="statement-table"
          title="Users"
          :rows="rows"
          :columns="columns"
          row-key="__index"
          :filter="filter"
          virtual-scroll
          :rows-per-page-options="[0]"
        >
          <template v-slot:top-right>
            <q-input
              outlined
              dense
              debounce="300"
              v-model="filter"
              placeholder="Search"
            >
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
          </template>

          <template v-slot:body-cell-active="props">
            <q-td :props="props">
              <q-chip
                :color="props.row.banned ? 'red': 'green'"
                text-color="white"
                dense
                class="text-weight-bolder"
                square
              >{{ props.row.banned ? 'Banned' : 'Ok' }}
              </q-chip>
            </q-td>
          </template>
          <template v-slot:body-cell-actions="props">
            <q-td :props="props">
              <q-btn :disable="props.row.banned == true" dense flat round color="blue" field="ban" icon="block" @click="banUser(props.row)"></q-btn>
            </q-td>
          </template>
        </q-table>
      </q-card-section>
    </q-card>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import {userService} from 'src/service/UserService';
import {useUserStore} from 'stores/UserStore';
import {useQuasar} from 'quasar';

interface Users {
  id: string
  firstName: string
  lastName: string
  email: string
  username: string
  banned: boolean
}

export default defineComponent({
  name: 'UsersDashboardComponent',

  mounted() {
    userService.getAllUsers()
      .then(value => {
        this.rows = value;
      }).catch(reason => {
      this.q.notify({
        color: 'negative',
        position: 'top',
        message: reason.message,
        icon: 'report_problem'
      })
    })
  },

  methods: {
    banUser: function(user: Users) {
      this.rows.forEach(value => {
        if(value == user) {
          if(value.username != 'weesftw') {
            userService.banUser(value.username)
              .then(value1 => {
                value.banned = true
                this.q.notify({
                  color: 'negative',
                  position: 'top',
                  message: value1.message,
                  icon: 'report_problem'
                })
              }).catch(reason => {
              this.q.notify({
                color: 'negative',
                position: 'top',
                message: reason.message,
                icon: 'report_problem'
              })
            })
          }
        }
      })
    }
  },

  setup() {
    const q = useQuasar()
    const userState = useUserStore()
    const columns = [
      {
        name: 'id',
        required: true,
        label: 'Id',
        align: 'left',
        field: (row: { id: string; }) => row.id,
        format: (val: string) => `${val}`,
        sortable: true
      },
      {name: 'firstName', align: 'left', label: 'First Name', field: 'firstName', sortable: true},
      {name: 'lastName', align: 'left', label: 'Last Name', field: 'lastName', sortable: true},
      {name: 'email', align: 'left', label: 'E-mail', field: 'email'},
      {name: 'username', align: 'left', label: 'Username', field: 'username'},
      {name: 'active', align: 'left', label: 'Active', field: 'active'},
      {name: 'actions', align: 'left', label: 'Actions', field: 'actions'}
    ]

    const rows: Users[] = <Users[]><unknown>ref([])

    return {
      filter: ref(''),
      userState,
      columns,
      rows,
      q
    }
  }
})
</script>

<style lang="sass" scoped>
.card-content
  padding-left: 100px
  padding-right: 100px

@media screen and (max-width: 699px)
  .card-content
    padding-left: 15px
    padding-right: 15px
</style>
