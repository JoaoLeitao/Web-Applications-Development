import React from 'react';
import { Redirect } from 'react-router-dom'
import { message, Card, Button, Checkbox, Form, Input } from 'antd';
import Cookies from 'universal-cookie'
import configs from './config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch';

const cookies = new Cookies();

class ChecklistItem extends React.Component {
    
    constructor(props) {
        super(props)
        this.handleClickDelete = this.handleClickDelete.bind(this)
        this.handleClickUpdate = this.handleClickUpdate.bind(this)
        this.handleSubmitUpdate = this.handleSubmitUpdate.bind(this)
        this.onChange = this.onChange.bind(this)
        this.state = {
            deleted: false,
            updated: false,
            checklistItemName: null,
            description: null,
            state: null,
            submitUpdated: false
        }
    }

    handleClickDelete = () => {
        this.setState({deleted: true})
    }

    handleClickUpdate = () => {
        this.setState({updated: true})
    }

    handleSubmitUpdate = (e) => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
          if (!err) {
            this.setState(() => ({
                checklistItemName: values.checklistItemName,
                description: values.description,
                submitUpdated: true
            }))
          }
        });
    }

    onChange = (e) => {
        if(e.target.checked) {
            this.setState({state: 'completed'})
        }
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

        if(this.state.deleted === true) {
            const urlDelete = configs.API+configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId+'/checklistItems/'+this.props.computedMatch.params.checklistItemId;
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

        if(this.state.updated === true) {
            if(this.state.submitUpdated) {
                const urlPut = configs.API+configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId+'/checklistItems/'+this.props.computedMatch.params.checklistItemId;
                const dataPut = {
                    checklistItemName: this.state.checklistItemName,
                    description: this.state.description,
                    state: this.state.state
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
                                message.success('Sucess updating the checklist item!')
                                return (<Redirect to={configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId}/>)
                            }}
                            onError={(err2) => {
                                message.warning('Couldn\'t update the checklist item!')
                                return (<Redirect to={configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId}/>)
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
                        {getFieldDecorator('checklistItemName', {
                            rules: [],
                        })(
                            <Input />
                        )}
                    </Form.Item>
                    <Form.Item
                        {...formItemLayout}
                        label="Description"
                    >
                        {getFieldDecorator('description', {
                            rules: [],
                        })(
                            <Input />
                        )}
                    </Form.Item>
                    <Checkbox onChange={this.onChange}>
                        State
                    </Checkbox>
                    <Form.Item {...tailFormItemLayout}>
                        <Button htmlType="submit">Update</Button>
                    </Form.Item>
                </Form>
            );
        }

        const url = configs.API+configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId+'/checklistItems/'+this.props.computedMatch.params.checklistItemId;
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
                    <Button onClick={this.handleClickDelete}>
                        Delete checklist item
                    </Button>
                </div>
                <div className='checklists-templates-buttons-div'>
                    <Button onClick={this.handleClickUpdate}>
                        Update checklist item
                    </Button>
                </div>
                <div>
                    <HttpGet url={url} headers={header}
                        render={(result) => (
                            <HttpGetSwitch
                                result={result}
                                onJson={json => {
                                    const itemName = json.properties.checklistItemName
                                    const description = json.properties.description
                                    const state = json.properties.state
                                    return (
                                        <Card className='item-card' title={"Name: "+itemName} style={{ width: 300 }}>
                                            <p>Description: {description}.</p>
                                            <p>State: {state}.</p>
                                        </Card>
                                    )
                                }}
                                onError={(err) => {
                                    message.error('Error getting checklist item!')
                                    return (<Redirect to={configs.API+configs.CHECKLISTS+'/'+this.props.computedMatch.params.checklistId} />)
                                }}
                            />
                        )}
                    />
                </div>
            </div>
        )
    }
}

const che = Form.create()(ChecklistItem);

export default che