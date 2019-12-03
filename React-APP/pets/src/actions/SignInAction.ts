import {Action} from "redux";

export const SIGN_IN = 'SIGN_IN';
export const SIGN_OUT = 'SIGN_OUT';

export interface SignInAction extends Action { data:string | null }

export const signIn = (token:string|null) => ({type:SIGN_IN, data:token});
export const signOut = () => ({type:SIGN_OUT});

export function requestSignIn(username:string, password:string)  {
    return (dispatch:any) =>
        performLogin(username,password)
            .then(token => dispatch(signIn(token)))
}

async function performLogin(username:string, password:string) {
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    return fetch("/login",
        {method:'POST',
            headers: myHeaders,
            body: JSON.stringify({username:username, password:password})})
        .then( response => {
            if( response.ok )
                return response.headers.get('Authorization');
            else {
                console.log(`Error: ${response.status}: ${response.statusText}`);
                return null;
                // and add a message to the Ui: wrong password ?? other errors?
            }
        })
        .catch( err => { console.log(err); return null })
}
