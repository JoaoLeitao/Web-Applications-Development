const configs = {
    API: 'http://35.230.149.45/api',
    
    MITREidSettings: {
        authority: 'http://35.197.198.121/openid-connect-server-webapp',
        client_id: 'daw',
        redirect_uri: 'http://localhost:3000/redirect',
        popup_redirect_uri: 'http://localhost:3000/redirect',
        response_type: 'token id_token',
        scope: 'openid email profile',
        automaticSilentRenew: true,
        filterProtocolClaims: true,
        loadUserInfo: true
    },
    
    HOME: '/',
    LOGIN: '/login',
    REDIRECT: '/redirect',
    LOGOUT: '/logout',
    TEMPLATES: '/templates',
    CHECKLISTS: '/checklists',
    TEMPLATE: '/templates/:templateId',
    CHECKLIST: '/checklists/:checklistId',
    TEMPLATEITEM: '/templates/:templateId/templateItems/:templateItemId',
    CHECKLISTITEM: '/checklists/:checklistId/checklistItems/:checklistItemId'
}

export default configs