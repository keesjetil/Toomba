export const getBooks = () => axios({
    method: 'get',
    url: 'localhost:8080/api/book/all',
})

export const getBookById = (id) => axios({
    method: 'get',
    url: `localhost:8080/api/book/${id}`,
})