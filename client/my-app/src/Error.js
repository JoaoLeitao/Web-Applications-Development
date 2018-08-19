import React from 'react'
import {Alert} from 'antd'

class Error extends React.Component {
    render() {
        return (
            <div className='error-view'>
                <Alert
                    message="Error 404:"
                    description="The resource you were trying to find, doesn't exist!"
                    type="error"
                    showIcon
                />
            </div>
        )
    }
}

export default Error