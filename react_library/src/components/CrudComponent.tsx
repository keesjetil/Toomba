//TODO: split this into multiple components, for the sake of speed i did not do this...
import { BookFilled } from '@ant-design/icons';
import { Button, Card, Checkbox, Col, Form, Input, List, Row, Select, Typography } from 'antd';
import axios from 'axios';
import React, { Component } from 'react';

const { Option } = Select;

interface Category {
  id: string;
  title: string
}

type CrudState = {
  books: any[];
  categories: Category[];
};

class CrudComponent extends React.Component<{}, CrudState> {

  state: CrudState = {
    books: [],
    categories: []
  };

  componentDidMount() {
    this.getBooks();
    this.getCategories();
  }

  createBook(book:any){
    console.log("MY BOOK: ", book)
    let bodyFormData = new FormData();
    bodyFormData.append('title', book.title);
    bodyFormData.append('author', book.author);
    bodyFormData.append('description', book.description)
    bodyFormData.append('categories', book.categories)
    axios({
      method: "post",
      url: "http://localhost:8080/api/book",
      data: bodyFormData,
      headers: { "Content-Type": "multipart/form-data" },
    }).then(() => {
      this.getBooks()
    }).catch(e => {
      console.log(e)
    })
  }

  createCategory(category:any){
    let bodyFormData = new FormData();
    bodyFormData.append('title', category.title);
    axios({
      method: "post",
      url: "http://localhost:8080/api/category",
      data: bodyFormData,
      headers: { "Content-Type": "multipart/form-data" },
    }).then(() => {
      this.getCategories()
    }).catch(e => {
      console.log(e)
    })
  }

  deleteBookById(id:any){
    axios({
      method: 'delete',
      url: `http://localhost:8080/api/book/${id}`,
    }).then(() => {
      this.getBooks()
    }).catch(e => {
      console.log(e)
    })
  }

  getBooks() {
    axios({
      method: 'get',
      url: 'http://localhost:8080/api/book/all',
    }).then(response => {
      if(response.data.length){
        this.setState({ books: response.data })
      }else{
        this.setState({ books: [] })
      }
    
    }).catch(e => {
      this.setState({ books: [] })
    })
  }

  getCategories() {
    axios({
      method: 'get',
      url: 'http://localhost:8080/api/category/all',
    }).then(response => {
      this.setState({ categories: response.data })
    }).catch(e => {
      this.setState({ categories: [] })
    })
  }

  

  render() {
    const onFinishCategory = (values: any) => {
      this.createCategory(values)
    };

    const onFinishBook = (values: any) => {
      let categories = new Set()
      values.categories.forEach((category: any) => categories.add({title: category}))
      values.categories = categories
      this.createBook(values)
    };

    const onFinishFailed = (errorInfo: any) => {
      console.log('Failed:', errorInfo);
    };


    const children = [];
    console.log("IN RENDER: ", this.state.categories)
    if(this.state.categories.length > 0){
      for (const category of this.state.categories) {
        children.push(<Option key={category.id}>{category.title}</Option>);
      }
    }

    const handleChange = (value: string[]) => {
      console.log(`selected ${value}`);
    };

    return (
      <>
        <Row>
          <Col span={12}>
            <Row style={{ margin: 20 }}>
              <Card title="Add a Book here">
                <Form
                  name="basic"
                  labelCol={{ span: 8 }}
                  wrapperCol={{ span: 16 }}
                  initialValues={{ remember: true }}
                  onFinish={onFinishBook}
                  onFinishFailed={onFinishFailed}
                  autoComplete="off"
                >
                  <Form.Item
                    label="title"
                    name="title"
                    rules={[{ required: true, message: 'Een book moet een titel hebben' }]}
                  >
                    <Input />
                  </Form.Item>

                  <Form.Item
                    label="author"
                    name="author"
                    rules={[{ required: true, message: 'Een book moet een auteur hebben' }]}
                  >
                    <Input />
                  </Form.Item>

                  <Form.Item
                    label="description"
                    name="description"
                    rules={[{ required: true, message: 'Please input your password!' }]}
                  >
                    <Input />
                  </Form.Item>

                  <Form.Item
                    label="categories"
                    name="categories"
                    rules={[{ required: true, message: 'Please input your password!' }]}
                  >
                    <Select
                      mode="multiple"
                      allowClear
                      style={{ width: '100%' }}
                      placeholder="Please select"
                      onChange={handleChange}
                    >
                      {children}
                    </Select>
                  </Form.Item>

                  <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
                    <Button type="primary" htmlType="submit">
                      Submit Book
                    </Button>
                  </Form.Item>
                </Form>
              </Card>
            </Row>
            <Row style={{ margin: 20 }}>
              <Card title={"Add a category here"}>
                <Form
                  name="basic"
                  labelCol={{ span: 8 }}
                  wrapperCol={{ span: 16 }}
                  initialValues={{ remember: true }}
                  onFinish={onFinishCategory}
                  onFinishFailed={onFinishFailed}
                  autoComplete="off"
                >
                  <Form.Item
                    label="title"
                    name="title"
                    rules={[{ required: true, message: 'Please input a title!' }]}
                  >
                    <Input />
                  </Form.Item>

                  <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
                    <Button type="primary" htmlType="submit">
                      Submit Category
                    </Button>
                  </Form.Item>
                </Form>
              </Card>
            </Row>
          </Col>
          <Col span={12}>
             <List
              style={{margin:20}}
              header={<div>Books</div>}
              bordered
              dataSource={this.state.books}
              renderItem={item => (
                <List.Item>
                  <Typography.Text>{item.id}</Typography.Text>
                  <Typography.Text>Title: <strong>{item.title}</strong></Typography.Text>
                  <Typography.Text>Description: <strong>{item.description}</strong></Typography.Text>
                  <Typography.Text>Author: <strong>{item.author}</strong></Typography.Text>
                  <Typography.Text>Categories: <strong>{item.categories}</strong></Typography.Text>
                  <Button type="primary">
                    Edit
                  </Button>
                  <Button type="primary" danger onClick={() => this.deleteBookById(item.id)}>
                    Delete
                  </Button>
                </List.Item>
              )}
            />
          </Col>
        </Row>
      </>
    );
  }
}

export default CrudComponent