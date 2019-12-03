import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Pet} from "../components/PetList";


export enum ChangePasswordTypes {
    UPDATE_PASSWORD = 'UPDATE_PET',
}


export const updatePassword = (id: number, password: string) => ({type: ChangePasswordTypes.UPDATE_PASSWORD, data: {id: id, password: password}});

export function updatePasswordRequest(id: number, password: string) {
    return (dispatch: any) => {

        return fetch(`/${+id}/password/`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({pass: password })
        })
            .then(response => {
                if (response.ok) {
                    dispatch(updatePassword(id, password));
                    console.log("updated password with id: " + id);
                    return response.json();
                } else {
                    console.log(`Error: ${response.status}: ${response.statusText}`);
                    console.error(response)
                }
            })
            .catch(reason => {
                console.log(reason);
            });
    }
}
