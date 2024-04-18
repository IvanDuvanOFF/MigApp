import axios from 'axios'   

class AuthService{    
    signing(user){       
        return axios.post("auth/signing", {
            login: user.username, password: user.password
        }).then(response => {
            if (response.data) {
              localStorage.setItem('user', JSON.stringify(response.data));
            }    
            return response.data;
          });
    }
    
    signingtfa(username, password){
        return axios.post("auth/signing/tfa", {
            params: { username: username, password: password }
        });
    }

    restore(email){
        return axios.post("auth/signing", {
            params: { email: email }
        });
    }
    
    restoreByToken(token, email){
        return axios.post(`auth/signing/${token}`, {
            params: { email: email }
        }); 
    }

    refreshToken(refresh_token){
        return axios.post(`auth/signing/{token}`, {
            params: { refresh_token: refresh_token }
        });
    }

    logout(){
        localStorage.removeItem('user');
    }
}

export default new AuthService();