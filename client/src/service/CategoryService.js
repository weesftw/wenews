const axios = require('axios')

function getAll() {
    return axios.get("/v1/categories");
}

export default {
    getAll
}