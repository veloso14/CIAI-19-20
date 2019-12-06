import {Action} from "redux";

export const REGISTER = 'REGISTER';

export interface REGISTERAction extends Action { data:string | null }

export const register = () => ({type:REGISTER});

export function requestRegister(username:string, password:string)  {
    return (dispatch:any) =>
        performRegister(username,password)
            .then( )
}

async function performRegister(username:string, password:string) {
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    return fetch("/signup",
        {method:'POST',
            headers: myHeaders,
            body: JSON.stringify({username:username, password:password})})
        .then( response => {
            if( response.ok )
                console.log(`Registado com sucesso`);
            else {
                console.log(`Error: ${response.status}: ${response.statusText}`);
                return null;
                // and add a message to the Ui: wrong password ?? other errors?
            }
        })
        .catch( err => { console.log(err); return null })
}
