import React from 'react';
import { Link, Redirect } from 'react-router-dom'
import { List, message, Button, Form, Input, Dropdown, Icon } from 'antd';
import Cookies from 'universal-cookie'
import configs from './config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch';

const cookies = new Cookies();

class Checklists extends React.Component {

    constructor(props) {
        super(props)
        this.handleSubmit = this.handleSubmit.bind(this)
        this.getTemplates = this.getTemplates.bind(this)
        this.onClick = this.onClick.bind(this)
        this.state = {
            checklistName: null,
            completionDate: null,
            templateId: null,
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
                checklistName: values.checklistName,
                completionDate: values.completionDate,
                submitCreated: true
            }))
          }
        });
    }
    
    onClick = (key) => {
        this.setState({ templateId: key })
    }
    
    getTemplates = () => {
        const urlGet = configs.API+configs.TEMPLATES;
        const headerGet = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${cookies.get('account')}`,
                'Access-Control-Allow-Origin': '*'
            }
        };

        return (
            <HttpGet url={urlGet} headers={headerGet}
            render={(result1) => (
                <HttpGetSwitch
                result={result1}
                onJson={json1 => {
                    const menu = (
                    <List
                        itemLayout="horizontal"
                        dataSource={json1.entities}
                        renderItem={item => (
                        <List.Item
                            key={item.properties.templateId}
                            onClick={this.onClick.bind(this, item.properties.templateId)}
                            className='list-item'
                        >
                        <List.Item.Meta
                            title={
                                <Button>
                                    {item.properties.templateName}
                                </Button>
                            }
                        />
                        </List.Item>
                        )}
                    />
                    );
                    return (
                        <Dropdown overlay={menu}>
                            <Button>
                                Choose template <Icon type="down" />
                            </Button>
                        </Dropdown>
                    )
                }}
                onError={(err2) => {
                    message.warning('Couldn\'t acess the templates!')
                    return (<Redirect to={configs.CHECKLISTS}/>)
                }}
                />
            )}
            />
        )
    }

    render() {

        if(this.state.created === true) {
            if(this.state.submitCreated === true) {
                const urlPost = configs.API+configs.CHECKLISTS;
                const data = {
                    checklistName: this.state.checklistName,
                    completionDate: this.state.completionDate
                }
          
                const tId = this.state.templateId
          
                if(tId) data.templateId = tId
          
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
                          return (<Redirect to={configs.CHECKLISTS+"/"+json1.properties.checklistId}/>)
                        }}
                        onError={(err2) => {
                          message.warning('Couldn\'t create the checklist!')
                          return (<Redirect to={configs.CHECKLISTS}/>)
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
                    {getFieldDecorator('checklistName', {
                        rules: [{ required: true, message: 'Please input a name for the checklist!', whitespace: true }],
                    })(
                        <Input />
                    )}
                    </Form.Item>
                    <Form.Item
                    {...formItemLayout}
                    label="Completion date"
                    >
                    {getFieldDecorator('completionDate', {
                        rules: [{
                        required: true, message: 'Please input the completion date for the checklist!',
                        }],
                    })(
                        <Input type="date" />
                    )}
                    </Form.Item>
                    <Form.Item {...tailFormItemLayout}>
                    {this.getTemplates()}
                    </Form.Item>
                    <Form.Item {...tailFormItemLayout}>
                    <Button htmlType="submit">Create</Button>
                    </Form.Item>
                </Form>
            );
        }

        const url = configs.API+configs.CHECKLISTS;
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
                        Create Checklist
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
                                                <List.Item className='checklist-item'>
                                                <List.Item.Meta
                                                    title={<Link to={item.links[0].href}>{item.properties.checklistName}</Link>}
                                                    description={'Completion Date: '+item.properties.completionDate}
                                                />
                                                </List.Item>
                                            )}
                                        />
                                    )
                                }}
                                onError={(err) => {
                                    message.error('Error getting checklists!')
                                    return (<Redirect to={configs.HOME} />)
                                }}
                            />
                        )}
                    />
                </div>
            </div>
        )
    }
}

const che = Form.create()(Checklists);

export default che