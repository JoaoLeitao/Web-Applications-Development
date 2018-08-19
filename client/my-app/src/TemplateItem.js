import React from 'react';
import { Redirect } from 'react-router-dom'
import { message, Card, Button, Form, Input } from 'antd';
import Cookies from 'universal-cookie'
import configs from './config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch';

const cookies = new Cookies();

class TemplateItem extends React.Component {
    
    constructor(props) {
        super(props)
        this.handleClickDelete = this.handleClickDelete.bind(this)
        this.handleClickUpdate = this.handleClickUpdate.bind(this)
        this.handleSubmitUpdate = this.handleSubmitUpdate.bind(this)
        this.state = {
            deleted: false,
            updated: false,
            templateItemName: null,
            description: null,
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
                templateItemName: values.templateItemName,
                description: values.description,
                submitUpdated: true
            }))
          }
        });
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
            const urlDelete = configs.API+configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId+'/templateItems/'+this.props.computedMatch.params.templateItemId;
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

        if(this.state.updated === true) {
            if(this.state.submitUpdated) {
                const urlPut = configs.API+configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId+'/templateItems/'+this.props.computedMatch.params.templateItemId;
                const dataPut = {
                    templateItemName: this.state.templateItemName,
                    description: this.state.description
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
                                message.success('Sucess updating the template item!')
                                return (<Redirect to={configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId}/>)
                            }}
                            onError={(err2) => {
                                message.warning('Couldn\'t update the template item!')
                                return (<Redirect to={configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId}/>)
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
                        {getFieldDecorator('templateItemName', {
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
                    <Form.Item {...tailFormItemLayout}>
                        <Button htmlType="submit">Update</Button>
                    </Form.Item>
                </Form>
            );
        }

        const url = configs.API+configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId+'/templateItems/'+this.props.computedMatch.params.templateItemId;
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
                        Delete template item
                    </Button>
                </div>
                <div className='checklists-templates-buttons-div'>
                    <Button onClick={this.handleClickUpdate}>
                        Update template item
                    </Button>
                </div>
                <div>
                    <HttpGet url={url} headers={header}
                        render={(result) => (
                            <HttpGetSwitch
                                result={result}
                                onJson={json => {
                                    const itemName = json.properties.templateItemName
                                    const description = json.properties.description
                                    return (
                                        <Card className='item-card' title={"Name: "+itemName} style={{ width: 300 }}>
                                            <p>Description: {description}.</p>
                                        </Card>
                                    )
                                }}
                                onError={(err) => {
                                    message.error('Error getting template item!')
                                    return (<Redirect to={configs.API+configs.TEMPLATES+'/'+this.props.computedMatch.params.templateId} />)
                                }}
                            />
                        )}
                    />
                </div>
            </div>
        )
    }
}

const tem = Form.create()(TemplateItem);

export default tem