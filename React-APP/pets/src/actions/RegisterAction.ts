import {Action} from "redux";

export const REGISTER = 'REGISTER';

export interface REGISTERAction extends Action {
    data: string | null
}

export const register = () => ({type: REGISTER});

export function requestRegister(username: string, password: string, nome: string, email: string, cellphone: string, address: string) {
    return (dispatch: any) =>
        performRegister(username, password, nome, email, cellphone, address)
            .then()
}

async function performRegister(username: string, password: string, nome: string, email: string, cellphone: string, address: string) {
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    return fetch("/clients",
        {
            method: 'POST',
            headers: myHeaders,
            body: JSON.stringify({
                id: 0,
                name: nome,
                email: email,
                username: username,
                password: password,
                cellphone: cellphone,
                address: address,
                photo: ""
            })
        })
        .then(response => {
            if (response.ok) {
                window.alert("Registado com Sucesso");
                console.log(`Registado com sucesso`);
            } else {
                console.log(`Error: ${response.status}: ${response.statusText}`);
                window.alert("Erro no registo");
                return null;
                // and add a message to the Ui: wrong password ?? other errors?
            }
        })
        .catch(err => {
            console.log(err);
            return null
        })
}
