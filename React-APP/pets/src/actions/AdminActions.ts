import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Admin} from "../components/AdminList";


export enum AdminActionsTypes {
    ADD_ADMIN = 'ADD_ADMIN',
    REQUEST_ADMINS = 'REQUEST_ADMINS',
    RECEIVE_ADMINS = 'RECEIVE_ADMINS',
    REQUEST_ADMIN = 'REQUEST_ADMIN',
    RECEIVE_ADMIN = 'RECEIVE_ADMIN',
    DELETE_ADMIN = 'DELETE_ADMIN',
    UPDATE_ADMIN = 'UPDATE_ADMIN',
}

export interface AddAdminAction extends Action {
    name: string,
    employeeID: number;
    photo: string;
    address: string;
    password: string;
    username: string;
    cellphone: number
    email: string
}

export interface ReceiveAdminsAction extends Action {
    data: Admin[]
}
export interface ReceiveAdminAction extends Action {
    data: Admin

}

export interface DeleteAdminAction extends Action {
    id: number
}

export interface UpdateAdminAction extends Action {
    data: {
        id: string
        admin: Admin,
    }
}


export const addAdmin = (admin: Admin) => ({type: AdminActionsTypes.ADD_ADMIN, data: admin});
export const deleteAdmin = (id: number) => ({type: AdminActionsTypes.DELETE_ADMIN, data: id});
export const updateAdmin = (id: string, admin: Admin) => ({type: AdminActionsTypes.UPDATE_ADMIN, data: {id: id, admin: admin}});
export const requestAdmins = () => ({type: AdminActionsTypes.REQUEST_ADMINS});
export const receiveAdmins = (data: Admin[]) => ({type: AdminActionsTypes.RECEIVE_ADMINS, data: data});
export const requestAdmin = () => ({type: AdminActionsTypes.REQUEST_ADMIN});
export const receiveAdmin = (data: {}) => ({type: AdminActionsTypes.RECEIVE_ADMIN, data: data});

export function fetchAdmin(id: string) {
    return (dispatch: any) => {
        dispatch(requestAdmin());
        return getData(`/admins/${+id}`, {admin:{}})
            .then(data => {
                console.log("log: " + JSON.stringify(data))
                data && dispatch(receiveAdmin(data))
            })
    }
}

export function fetchAdmins() {
    return (dispatch: any) => {
        dispatch(requestAdmins());
        return getData('/admins', [])
            .then(data => {
                data && dispatch(receiveAdmins(data.map((p: { admin: Admin }) => p.admin)))
            })
    }
}

export function postAdmin(admin: Admin) {
    return (dispatch: any) => {
        dispatch(addAdmin(admin));
        return fetch('/admins', {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({id: admin.id, name: admin.name, email: admin.email, username: admin.username, password: admin.password,  cellphone: admin.cellphone, address: admin.address , photo:admin.photo,  employeeID: admin.employeeID})
        })
            .then(response => {
                if (response.ok)
                    return response.json();
                else {
                    console.log(`Error: ${response.status}: ${response.statusText}`);
                    console.error(response)
                }
            })
            .catch(reason => {
                console.log(reason);
            });
    }
}

export function deleteAdminRequest(id: number) {
    return (dispatch: any) => {

        return fetch(`/admins/${id}`, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    dispatch(deleteAdmin(id));
                    console.log("deleted admin with id: " + id);
                    return response.json();
                } else {
                    console.log(`Error: ${response.status}: ${response.statusText}`);
                    console.error(response)
                }
            })
            .catch(reason => {
                //console.log(reason);
            });
    }
}

export function updateAdminRequest(id: string, admin: Admin) {
    return (dispatch: any) => {

        return fetch(`/admins/${+id}/info`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({id: 0, cellphone: admin.cellphone, email: admin.email, address: admin.address})
        })
            .then(response => {
                if (response.ok) {
                    dispatch(updateAdmin(id, admin));
                    console.log("updated admin with id: " + id);
                    return response.json();
                } else {
                    console.log(`Error: ${response.status}: ${response.statusText}`);
                    console.error(response)
                }
            })
            .catch(reason => {
                //console.log(reason);
            });
    }
}
