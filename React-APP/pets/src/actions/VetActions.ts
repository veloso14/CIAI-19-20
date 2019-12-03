import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Vet} from "../components/VetList";


export enum VetActionsTypes {
    ADD_VET = 'ADD_VET',
    UPDATE_VET = 'UPDATE_VET',
    DELETE_VET = 'DELETE_VET',
    REQUEST_VETS = 'REQUEST_VETS',
    RECEIVE_VETS = 'RECEIVE_VETS',
    REQUEST_VET = 'REQUEST_VET',
    RECEIVE_VET = 'RECEIVE_VET',
}

export interface AddVetAction extends Action {
    name: string,
    employeeID: number;
    photo: string;
    address: string;
    password: string;
    username: string;
    cellphone: number
    email: string
}

export interface ReceiveVetsAction extends Action {
    data: Vet[]
}
export interface ReceiveVetAction extends Action {
    data: {
        vet: Vet,
        appointments: []
        schedules:[]
    }
}

export interface UpdateVetAction extends Action {
    data: {
        id: string
        vet: Vet,
    }
}


export interface DeleteVetAction extends Action {
    id: number
}

export const addVet = (vet: Vet) => ({type: VetActionsTypes.ADD_VET, data: vet});
export const deleteVet = (id: string, vet: Vet) => ({type: VetActionsTypes.DELETE_VET, data: {id: id, pet: vet}});
export const updateVet = (id: string, vet: Vet) => ({type: VetActionsTypes.UPDATE_VET, data: {id: id, pet: vet}});
export const requestVets = () => ({type: VetActionsTypes.REQUEST_VETS});
export const receiveVets = (data: Vet[]) => ({type: VetActionsTypes.RECEIVE_VETS, data: data});
export const requestVet = () => ({type: VetActionsTypes.REQUEST_VET});
export const receiveVet = (data: {}) => ({type: VetActionsTypes.RECEIVE_VET, data: data});

export function fetchVet(id: string) {
    return (dispatch: any) => {
        dispatch(requestVet());
        return getData(`/vets/${+id}`, {vet:{}, appointments: [], schedules: []})
            .then(data => {
                console.log("log: " + JSON.stringify(data))
                data && dispatch(receiveVet(data))
            })
    }
}

export function fetchVets() {
    return (dispatch: any) => {
        dispatch(requestVets());
        return getData(`/vets`, [])
            .then(data => {
                data && dispatch(receiveVets(data.map((p: { vet: Vet }) => p.vet)))
            })
    }
}

export function postVet(vet: Vet) {
    return (dispatch: any) => {
        dispatch(addVet(vet));
        return fetch(`/vets`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({id: vet.id, name: vet.name, email: vet.email, username: vet.username, password: vet.password,  cellphone: vet.cellphone, address: vet.address , photo:vet.photo,  employeeID: vet.employeeID})
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


export function deleteVetRequest(id: string, vet: Vet) {
    return (dispatch: any) => {

        return fetch(`/vets/${+id}`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({id: vet.id})
        })
            .then(response => {
                if (response.ok) {
                    dispatch(deleteVet(id, vet));
                    console.log("fired vet with id: " + id);
                    return response.json();
                } else {
                    console.log(`Error: ${response.status}: ${response.statusText}`);
                    console.error(response)
                }
            })
            .catch(reason => {
                //console.log(reason);
            });
    }}

export function updateVetRequest(id: string, vet: Vet) {
    return (dispatch: any) => {

        return fetch(`/vets/${+id}/info`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ address: vet.address,
                employeeID: vet.employeeID,
                id: vet.id,
                password: vet.password,
                photo: vet.photo,
                username: vet.username,
                name: vet.name, cellphone: vet.cellphone, email: vet.email })
        })
            .then(response => {
                if (response.ok) {
                    dispatch(updateVet(id, vet));
                    console.log("updated vet with id: " + id);
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
