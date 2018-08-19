import React from 'react';
import { Link, Redirect } from 'react-router-dom'
import { List, message, Button, Form, Input } from 'antd';
import Cookies from 'universal-cookie'
import configs from './config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch';

const cookies = new Cookies();

class Template extends React.Component {

    constructor(props) {
        super(props)
        this.handleClickDelete = this.handleClickDelete.bind(this)
        this.handleClickCreate = this.handleClickCreate.bind(this)
        this.handleClickUpdate = this.handleClickUpdate.bind(this)
        this.handleSubmitCreate = this.handleSubmitCreate.bind(this)
        this.handleSubmitUpdate = this.handleSubmitUpdate.bind(this)
        this.state = {
            deleted: false,
            templateItemName: null,
            checklistName: null,
            completionDate: null,
            description: null,
            created: false,
            updated: false,
            submitCreated: false,
            submitUpdated: false
        }
    }

    handleSubmitCreate = (e) => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
          if (!err) {
            this.setState(() => ({
                templateItemName: values.templateItemName,
                description: values.description,
                submitCreated: true
            }))
          }
        });
    }

    handleSubmitUpdate = (e) => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
          if (!err) {
            this.setState(() => ({
                templateName: values.templateName,
                submitUpdated: true
            }))
          }
        });
    }

    handleClickDelete = () => {
        this.setState({deleted: true})
    }

    handleClickCreate = () => {
        this.setState({created: true})
    }

    handleClickUpdate = () => {
        this.setState({updated: true})
    }

    render() {

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

        if(this.state.deleted) {
            const urlDelete = configs.API+configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId;
            const headerDelete = {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${cookies.get('account')}`,
                    'Access-Control-Allow-Origin': '*'
                }
            };

            return (
                <HttpGet url={urlDelete} headers={headerDelete}
                    render={(result) => (
                        <HttpGetSwitch
                            result={result}
                            onJson={json => {
                                return (<Redirect to={configs.TEMPLATES} />)
                            }}
                            onError={(err) => {
                                return (<Redirect to={configs.TEMPLATES} />)
                            }}
                        />
                    )}
                />
            )
        }

        if(this.state.created) {
            if(this.state.submitCreated) {
                const urlPost = configs.API+configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId+'/templateItems';
                const dataPost = {
                    templateItemName: this.state.templateItemName,
                    description: this.state.description
                }
                const headerPost = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${cookies.get('account')}`,
                        'Access-Control-Allow-Origin': '*'
                    },
                    body: JSON.stringify(dataPost)
                };
        
                return (
                <HttpGet url={urlPost} headers={headerPost}
                    render={(result1) => (
                    <HttpGetSwitch
                        result={result1}
                        onJson={json1 => {
                            return (<Redirect to={configs.TEMPLATES+"/"+this.props.computedMatch.params.templateId+"/templateItems/"+json1.properties.templateItemId}/>)
                        }}
                        onError={(err2) => {
                            message.warning('Couldn\'t create the template item!')
                            return (<Redirect to={configs.TEMPLATES}/>)
                        }}
                    />
                    )}
                />
                )
            }
    
            return (
            <Form onSubmit={this.handleSubmitCreate} className='register'>
                <Form.Item
                {...formItemLayout}
                label="Name"
                >
                {getFieldDecorator('templateItemName', {
                    rules: [{ required: true, message: 'Please input a name for the template item!', whitespace: true }],
                })(
                    <Input />
                )}
                </Form.Item>
                <Form.Item
                {...formItemLayout}
                label="Description"
                >
                {getFieldDecorator('description', {
                    rules: [{ required: true, message: 'Please input a description for the template item!', whitespace: true }],
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

        if(this.state.updated) {
            if(this.state.submitUpdated) {
                const urlPut = configs.API+configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId;
                const dataPut = {
                    templateName: this.state.templateName
                }
                const headerPut = {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${cookies.get('account')}`,
                        'Access-Control-Allow-Origin': '*'
                    },
                    body: JSON.stringify(dataPut)
                };
        
                return (
                    <HttpGet url={urlPut} headers={headerPut}
                        render={(result1) => (
                        <HttpGetSwitch
                            result={result1}
                            onJson={json1 => {
                                message.success('Sucess updating the template!')
                                return (<Redirect to={configs.TEMPLATES}/>)
                            }}
                            onError={(err2) => {
                                message.warning('Couldn\'t update the template!')
                                return (<Redirect to={configs.TEMPLATES}/>)
                            }}
                        />
                        )}
                    />
                )
            }
    
            return (
                <Form onSubmit={this.handleSubmitUpdate} className='register'>
                    <Form.Item
                        {...formItemLayout}
                        label="Name"
                    >
                        {getFieldDecorator('templateName', {
                            rules: [],
                        })(
                            <Input />
                        )}
                    </Form.Item>
                    <Form.Item {...tailFormItemLayout}>
                        <Button htmlType="submit">Update</Button>
                    </Form.Item>
                </Form>
            );
        }

        const url = configs.API+configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId+'/templateItems';
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
                    <Button onClick={this.handleClickCreate}>
                        Create template item
                    </Button>
                </div>
                <div className='checklists-templates-buttons-div'>
                    <Button onClick={this.handleClickDelete}>
                        Delete template
                    </Button>
                </div>
                <div className='checklists-templates-buttons-div'>
                    <Button onClick={this.handleClickUpdate}>
                        Update template
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
                                                <List.Item className='list-item'>
                                                <List.Item.Meta
                                                    title={<Link to={item.links[0].href}>{item.properties.templateItemName}</Link>}
                                                />
                                                </List.Item>
                                            )}
                                        />
                                    )
                                }}
                                onError={(err) => {
                                    message.error('Error getting template items!')
                                    return (<Redirect to={configs.TEMPLATES} />)
                                }}
                            />
                        )}
                    />
                </div>
            </div>
        )
    }
}

const tem = Form.create()(Template);

export default tem