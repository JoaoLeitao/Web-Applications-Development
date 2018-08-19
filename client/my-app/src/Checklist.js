import React from 'react';
import { Link, Redirect } from 'react-router-dom'
import { List, message, Button, Form, Input } from 'antd';
import Cookies from 'universal-cookie'
import configs from './config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch';

const cookies = new Cookies();

class Checklist extends React.Component {

    constructor(props) {
        super(props)
        this.handleClickDelete = this.handleClickDelete.bind(this)
        this.handleClickCreate = this.handleClickCreate.bind(this)
        this.handleClickUpdate = this.handleClickUpdate.bind(this)
        this.handleSubmitCreate = this.handleSubmitCreate.bind(this)
        this.handleSubmitUpdate = this.handleSubmitUpdate.bind(this)
        this.state = {
            deleted: false,
            checklistItemName: null,
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
                checklistItemName: values.checklistItemName,
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
                checklistName: values.checklistName,
                completionDate: values.completionDate,
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
            const urlDelete = configs.API+configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId;
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
                                return (<Redirect to={configs.CHECKLISTS} />)
                            }}
                            onError={(err) => {
                                return (<Redirect to={configs.CHECKLISTS} />)
                            }}
                        />
                    )}
                />
            )
        }

        if(this.state.created) {
            if(this.state.submitCreated) {
                const urlPost = configs.API+configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId+'/checklistItems';
                const dataPost = {
                    checklistItemName: this.state.checklistItemName,
                    description: this.state.description,
                    state: "uncompleted"
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
                            return (<Redirect to={configs.CHECKLISTS+"/"+this.props.computedMatch.params.checklistId+"/checklistItems/"+json1.properties.checklistItemId}/>)
                        }}
                        onError={(err2) => {
                            message.warning('Couldn\'t create the checklist item!')
                            return (<Redirect to={configs.CHECKLISTS}/>)
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
                {getFieldDecorator('checklistItemName', {
                    rules: [{ required: true, message: 'Please input a name for the checklist item!', whitespace: true }],
                })(
                    <Input />
                )}
                </Form.Item>
                <Form.Item
                {...formItemLayout}
                label="Description"
                >
                {getFieldDecorator('description', {
                    rules: [{ required: true, message: 'Please input a description for the checklist item!', whitespace: true }],
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
                const urlPut = configs.API+configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId;
                const dataPut = {
                    checklistName: this.state.checklistName,
                    completionDate: this.state.completionDate,
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
                                message.success('Sucess updating the checklist!')
                                return (<Redirect to={configs.CHECKLISTS}/>)
                            }}
                            onError={(err2) => {
                                message.warning('Couldn\'t update the checklist!')
                                return (<Redirect to={configs.CHECKLISTS}/>)
                            }}
                        />
                        )}
                    />
                )
            }
    
            return (
                <Form onSubmit={this.handleSubmitUpdate} id='register'>
                    <Form.Item
                    {...formItemLayout}
                    label="Name"
                    >
                    {getFieldDecorator('checklistName', {
                        rules: [],
                    })(
                        <Input />
                    )}
                    </Form.Item>
                    <Form.Item
                    {...formItemLayout}
                    label="Completion date"
                    >
                    {getFieldDecorator('completionDate', {
                        rules: [],
                    })(
                        <Input type="date"/>
                    )}
                    </Form.Item>
                    <Form.Item {...tailFormItemLayout}>
                    <Button htmlType="submit">Update</Button>
                    </Form.Item>
                </Form>
            );
        }

        const url = configs.API+configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId+'/checklistItems';
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
                        Create checklist item
                    </Button>
                </div>
                <div className='checklists-templates-buttons-div'>
                    <Button onClick={this.handleClickDelete}>
                        Delete checklist
                    </Button>
                </div>
                <div className='checklists-templates-buttons-div'>
                    <Button onClick={this.handleClickUpdate}>
                        Update checklist
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
                                                    title={<Link to={item.links[0].href}>{item.properties.checklistItemName}</Link>}
                                                />
                                                </List.Item>
                                            )}
                                        />
                                    )
                                }}
                                onError={(err) => {
                                    message.error('Error getting checklist items!')
                                    return (<Redirect to={configs.CHECKLISTS} />)
                                }}
                            />
                        )}
                    />
                </div>
            </div>
        )
    }
}

const che = Form.create()(Checklist);

export default che