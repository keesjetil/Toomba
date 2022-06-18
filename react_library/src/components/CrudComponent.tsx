//TODO: split this into multiple components, for the sake of speed i did not do this...
import { Button, Card, Checkbox, Col, Form, Input, Row, Select } from 'antd';
import React, { Component } from 'react';

const { Option } = Select;

type CrudState = {
  value: number;
};

class CrudComponent extends React.Component<{}, CrudState> {

  state: CrudState = {
    value: 0,
  };

  componentDidMount(){
    console.log("hi")
  }

  render() {
    const { value } = this.state;
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
                      defaultValue={['a10', 'c12']}
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
          <Col span={12}>col-12</Col>
        </Row>
      </>
    );
  }
}

export default CrudComponent