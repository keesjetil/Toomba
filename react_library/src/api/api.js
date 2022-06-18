// export const getBookById = (id) => axios({
//     method: 'get',
//     url: `localhost:8080/api/book/${id}`,
// })
import axios from 'axios';
export default async function getBooks() {
    try {
      // 👇️ const data: CreateUserResponse
      const { data } = axios({
        method: 'get',
        url: 'http://localhost:8080/api/book/all',
    })
      return data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        console.log('error message: ', error.message);
        // 👇️ error: AxiosError<any, any>
        return error.message;
      } else {
        console.log('unexpected error: ', error);
        return 'An unexpected error occurred';
      }
    }
}