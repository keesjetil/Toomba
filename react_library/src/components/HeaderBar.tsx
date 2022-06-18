import BookOutlined from '@ant-design/icons/lib/icons/BookOutlined';
import PageHeader from 'antd/lib/page-header';
import React, { FC } from 'react';


function HeaderBar() {
    return (
        <>
            <PageHeader
                title="Library App"
                subTitle="In this app one can do CRUD operations on books and categories."
                style={
                    {border: "1px solid rgb(235, 237, 240)"}
                }
                extra={<BookOutlined />}
            ></PageHeader>
        </>
    );
};

export default HeaderBar;
