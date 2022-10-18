const axios = require('axios')

function getAll() {
    return axios.get("/news/v1/categories");
}

export default {
    getAll
}