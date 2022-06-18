//TODO: split this into multiple components, for the sake of speed i did not do this...
import { Button, Card, Checkbox, Col, Form, Input, List, Row, Select, Typography } from 'antd';
import axios from 'axios';
import React, { Component } from 'react';
import getBooks from '../api/api';

const { Option } = Select;

type CrudState = {
  books: any[];
};

class CrudComponent extends React.Component<{}, CrudState> {

  state: CrudState = {
    books: [],
  };

  componentDidMount() {
    this.getBooks()
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
      this.setState({ books: response.data })
    }).catch(e => {
      this.setState({ books: [] })
    })
  }

  getCategories() {
    axios({
      method: 'get',
      url: 'http://localhost:8080/api/category/all',
    }).then(response => {
      this.setState({ books: response.data })
    }).catch(e => {
      this.setState({ books: [] })
    })
  }

  

  render() {
    const onFinish = (values: any) => {
      console.log('Success:', values);
    };

    const onFinishFailed = (errorInfo: any) => {
      console.log('Failed:', errorInfo);
    };

    const children = [];
    for (let i = 10; i < 36; i++) {
      children.push(<Option key={i.toString(36) + i}>{i.toString(36) + i}</Option>);
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
                  onFinish={onFinish}
                  onFinishFailed={onFinishFailed}
                  autoComplete="off"
                >
                  <Form.Item
                    label="Title"
                    name="Title"
                    rules={[{ required: true, message: 'Een book moet een auteur hebben' }]}
                  >
                    <Input />
                  </Form.Item>

                  <Form.Item
                    label="Description"
                    name="Description"
                    rules={[{ required: true, message: 'Please input your password!' }]}
                  >
                    <Input.Password />
                  </Form.Item>

                  <Form.Item
                    label="Categories"
                    name="Categories"
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
                  onFinish={onFinish}
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