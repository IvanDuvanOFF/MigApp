// Заголовок авторизации, без которого запросы не проходят

export default function AuthHeader(){
    let user = JSON.parse(localStorage.getItem('user'));
    if(user?.access_token){
        return {
            Authorization: 'Bearer ' + user.access_token
        };
    } else {
        return {};
    }
}