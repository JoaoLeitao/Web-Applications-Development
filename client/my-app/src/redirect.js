import { UserManager } from 'oidc-client'

export default () => {
    const user = new UserManager({})
    user.signinPopupCallback()
}