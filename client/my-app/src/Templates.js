import React from 'react';
import { Link, Redirect } from 'react-router-dom'
import { List, message, Button, Form, Input } from 'antd';
import Cookies from 'universal-cookie'
import configs from './config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch';

const cookies = new Cookies();

class Templates extends React.Component {

    constructor(props) {
        super(props)
        this.handleSubmit = this.handleSubmit.bind(this)
        this.handleClick = this.handleClick.bind(this)
        this.state = {
            templateName: null,
            created: false,
            submitCreated: false
        }
    }

    handleClick = () => {
        this.setState({created: true})
    }

    handleSubmit = (e) => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
          if (!err) {
            this.setState(() => ({
                templateName: values.templateName,
                submitCreated: true
            }))
          }
        });
    }

    render() {

        if(this.state.created === true) {
            if(this.state.submitCreated === true) {
                const urlPost = configs.API+configs.TEMPLATES;
                const data = {
                    templateName: this.state.templateName
                }
                const headerPost = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${cookies.get('account')}`,
                        'Access-Control-Allow-Origin': '*'
                    },
                    body: JSON.stringify(data)
                };
          
                return (
                  <HttpGet url={urlPost} headers={headerPost}
                    render={(result1) => (
                      <HttpGetSwitch
                        result={result1}
                        onJson={json1 => {
                          return (<Redirect to={configs.TEMPLATES+"/"+json1.properties.templateId}/>)
                        }}
                        onError={(err2) => {
                          message.warning('Couldn\'t create the template!')
                          return (<Redirect to={configs.TEMPLATES}/>)
                        }}
                      />
                    )}
                  />
                )
            }
        
            const { getFieldDecorator } = this.props.form;
        
            const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 8 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
            };
            const tailFormItemLayout = {
            wrapperCol: {
                xs: {
                span: 24,
                offset: 0,
                },
                sm: {
                span: 16,
                offset: 8,
                },
            },
            };
        
            return (
                <Form onSubmit={this.handleSubmit} className='register'>
                    <Form.Item
                    {...formItemLayout}
                    label="Name"
                    >
                    {getFieldDecorator('templateName', {
                        rules: [{ required: true, message: 'Please input a name for the template!', whitespace: true }],
                    })(
                        <Input />
                    )}
                    </Form.Item>
                    <Form.Item {...tailFormItemLayout}>
                    <Button htmlType="submit">Create</Button>
                    </Form.Item>
                </Form>
            );
        }

        const url = configs.API+configs.TEMPLATES;
        const header = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${cookies.get('account')}`,
                'Access-Control-Allow-Origin': '*'
            }
        };

        return(
            <div>
                <div className='checklists-templates-buttons-div'>
                    <Button onClick={this.handleClick}>
                        Create template
                    </Button>
                </div>
                <div>
                    <HttpGet url={url} headers={header}
                        render={(result) => (
                            <HttpGetSwitch
                                result={result}
                                onJson={json => {
                                    return (
                                        <List
                                            itemLayout="horizontal"
                                            dataSource={json.entities}
                                            renderItem={item => (
                                                <List.Item id='template-item'>
                                                <List.Item.Meta
                                                    title={<Link to={item.links[0].href}>{item.properties.templateName}</Link>}
                                                />
                                                </List.Item>
                                            )}
                                        />
                                    )
                                }}
                                onError={(err) => {
                                    message.error('Error getting templates!')
                                    return (<Redirect to={configs.HOME}/>)
                                }}
                            />
                        )}
                    />
                </div>
            </div>
        )
    }
}

const tem = Form.create()(Templates)

export default tem