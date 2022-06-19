//TODO: split this into multiple components, for the sake of speed i did not do this...
import { Button, Card, Col, Form, Input, List, Modal, Row, Select, Typography } from 'antd';
import Item from 'antd/lib/list/Item';
import axios from 'axios';
import React, { Component } from 'react';

const { Option } = Select;

interface Category {
  id: string;
  title: string;
  books: any;
}

type CrudState = {
  books: any[];
  categories: Category[];
  visibleCategory: boolean
  visibleBook:boolean;
  selectedItem: any
  selectedCategory: any
};

class CrudComponent extends React.Component<{}, CrudState> {

  state: CrudState = {
    books: [],
    categories: [],
    visibleBook: false,
    visibleCategory: false,
    selectedItem: null,
    selectedCategory:null,
  };

  componentDidMount() {
    this.getBooks();
    this.getCategories();
  }

  createBook(book: any) {
    console.log(JSON.stringify(book))
    axios.post("http://localhost:8080/api/book", book).then(() => {
      this.getBooks()
    }).catch(e => {
      console.log(e)
    })
  }

  createCategory(category: any) {
    console.log("CATEGORYTOPOST: ", category)
    axios.post("http://localhost:8080/api/category", category).then((response) => {
      console.log(response)
      this.getCategories()
    }).catch(e => {
      console.log(e)
    })
  }

  deleteBookById(id: any) {
    axios({
      method: 'delete',
      url: `http://localhost:8080/api/book/${id}`,
    }).then(() => {
      this.getBooks()
    }).catch(e => {
      console.log(e)
    })
  }

  deleteCategoryById(id: any) {
    axios({
      method: 'delete',
      url: `http://localhost:8080/api/category/${id}`,
    }).then(() => {
      this.getBooks()
      this.getCategories()
    }).catch(e => {
      console.log(e)
    })
  }

  editBook(book: any) {
    axios.put("http://localhost:8080/api/book", book).then(() => {
      this.getBooks()
    }).catch(e => {
      console.log(e)
    })
  }

  editCategory(category: any) {
    console.log("MY CATEGORY: ", category)
    axios.put("http://localhost:8080/api/category", category).then(() => {
      this.getBooks()
      this.getCategories()
    }).catch(e => {
      console.log(e)
    })
  }

  getBooks() {
    axios({
      method: 'get',
      url: 'http://localhost:8080/api/book/all',
    }).then(response => {
      if (response.data.length) {
        this.setState({ books: response.data })
      } else {
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
      if (response.data.length) {
        this.setState({ categories: response.data })
      } else {
        this.setState({ categories: [] })
      }
    }).catch(e => {
      this.setState({ categories: [] })
    })
  }

  setIsBookModalVisible(item: any, visible: boolean) {
    this.setState({ selectedItem: item, visibleBook: visible, selectedCategory:null, visibleCategory: false })
  }

  setIsCategoryModalVisible(item: any, visible: boolean) {
    this.setState({ selectedItem: null, visibleCategory: visible,visibleBook: false, selectedCategory:item})
  }



  render() {
    console.log(this.state.selectedItem)
    const onFinishCategory = (values: any) => {
      this.createCategory(values)
    };

    const onFinishBook = (values: any) => {
      const categories: Category[] = []
      values.categories.forEach((category: string) => {
        const categoryFound = this.state.categories.filter(categoryState => {
          return category == categoryState.id
        })[0]
        delete categoryFound.books
        categories.push(categoryFound)
      })
      values.categories = categories
      this.createBook(values)
    };

    const onFinishEditBook = (values: any) => {
      const categories: Category[] = []
      values.categories.forEach((category: string) => {
        const categoryFound = this.state.categories.filter(categoryState => {
          return category == categoryState.id
        })[0]
        delete categoryFound.books
        categories.push(categoryFound)
      })
      values.categories = categories
      values.id = this.state.selectedItem.id
      this.setState({visibleBook:false})
      this.editBook(values)
    };

    const onFinishEditCategory = (values: any) => {
      values.id = this.state.selectedCategory.id
      this.setState({visibleBook:false, visibleCategory:false})
      console.log("MY VALUES: ", values)
      this.editCategory(values)
    };

    const onFinishFailed = (errorInfo: any) => {
      console.log('Failed:', errorInfo);
    };


    const children = [];
    if (this.state.categories.length > 0) {
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
                    rules={[{ required: true, message: 'A book must have a title' }]}
                  >
                    <Input />
                  </Form.Item>

                  <Form.Item
                    label="author"
                    name="author"
                    rules={[{ required: true, message: 'A book must have a author' }]}
                  >
                    <Input />
                  </Form.Item>

                  <Form.Item
                    label="description"
                    name="description"
                    rules={[{ required: true, message: 'a book must have a description' }]}
                  >
                    <Input.Password />
                  </Form.Item>

                  <Form.Item
                    label="categories"
                    name="categories"
                    rules={[{ required: true, message: 'a book must contain at least one category' }]}
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
            <Row>
            <List
              style={{ margin: 20 }}
              header={<div>Books</div>}
              bordered
              dataSource={this.state.books}
              renderItem={item => {
                return (
                  <List.Item>
                    <Typography.Text>{item.id}</Typography.Text>
                    <Typography.Text>Title: <strong>{item.title}</strong></Typography.Text>
                    <Typography.Text>Description: <strong>{item.description}</strong></Typography.Text>
                    <Typography.Text>Author: <strong>{item.author}</strong></Typography.Text>
                    <Typography.Text>Categorie: <strong>{JSON.stringify(item.categories)}</strong></Typography.Text>
                    <Button type="primary" onClick={() => this.setIsBookModalVisible(item, true)}>
                      Edit
                    </Button>
                    <Button type="primary" danger onClick={() => this.deleteBookById(item.id)}>
                      Delete
                    </Button>
                  </List.Item>
                )
              }}
            />
            </Row>
            <Row>
            <List
              style={{ margin: 20 }}
              header={<div>Categories</div>}
              bordered
              dataSource={this.state.categories}
              renderItem={item => {
                return (
                  <List.Item>
                    <Typography.Text>{item.id}</Typography.Text>
                    <Typography.Text>Title: <strong>{item.title}</strong></Typography.Text>
                    <Button type="primary" onClick={() => this.setIsCategoryModalVisible(item, true)}>
                      Edit
                    </Button>
                    <Button type="primary" danger onClick={() => this.deleteCategoryById(item.id)}>
                      Delete
                    </Button>
                  </List.Item>
                )
              }}
            />
            </Row>
          </Col>
        </Row>

        <Modal title={this.state.selectedItem ? `Item with id ${this.state.selectedItem.id}` : 'dummy'} visible={this.state.visibleBook} footer={null}>
          <Form
            name="basic"
            labelCol={{ span: 8 }}
            wrapperCol={{ span: 16 }}
            initialValues={{ remember: true }}
            onFinish={onFinishEditBook}
            onFinishFailed={onFinishFailed}
            autoComplete="off"
          >

            <Form.Item
              label="title"
              name="title"
              initialValue={this.state.selectedItem ? this.state.selectedItem.title : ''}
              rules={[{ required: true, message: 'A book must have a title' }]}

            >
              <Input />
            </Form.Item>

            <Form.Item
              label="author"
              name="author"
              initialValue={this.state.selectedItem ? this.state.selectedItem.author : ''}
              rules={[{ required: true, message: 'A book must have a author' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              label="description"
              name="description"
              initialValue={this.state.selectedItem ? this.state.selectedItem.description : ''}
              rules={[{ required: true, message: 'a book must have a description' }]}
            >
              <Input.Password />
            </Form.Item>

            <Form.Item
              label="categories"
              name="categories"
              rules={[{ required: true, message: 'a book must contain at least one category' }]}
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
              <Button type="primary" onClick={() => this.setState({visibleBook:false})}>
                Cancel
              </Button>
            </Form.Item>
          </Form>
        </Modal>

        <Modal title={this.state.selectedCategory ? `Item with id ${this.state.selectedCategory.id}` : 'dummy'} visible={this.state.visibleCategory} footer={null}>
          <Form
            name="basic"
            labelCol={{ span: 8 }}
            wrapperCol={{ span: 16 }}
            initialValues={{ remember: true }}
            onFinish={onFinishEditCategory}
            onFinishFailed={onFinishFailed}
            autoComplete="off"
          >

            <Form.Item
              label="title"
              name="title"
              initialValue={this.state.selectedCategory ? this.state.selectedCategory.title : ''}
              rules={[{ required: true, message: 'A Category must have a title' }]}

            >
              <Input />
            </Form.Item>
            <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
              <Button type="primary" htmlType="submit">
                Submit Category
              </Button>
              <Button type="primary" onClick={() => this.setState({visibleCategory:false})}>
                Cancel
              </Button>
            </Form.Item>
          </Form>
        </Modal>
      </>
    );
  }
}

export default CrudComponent