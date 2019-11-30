import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Pet} from "../components/PetList";


export enum PetActionsTypes {
    ADD_PET = 'ADD_PET',
    UPDATE_PET = 'UPDATE_PET',
    DELETE_PET = 'DELETE_PET',
    REQUEST_PETS = 'REQUEST_PETS',
    RECEIVE_PETS = 'RECEIVE_PETS',
    REQUEST_PET = 'REQUEST_PET',
    RECEIVE_PET = 'RECEIVE_PET',
}

export interface AddPetAction extends Action {
    name: string,
    species: string
}

export interface ReceivePetAction extends Action {
    data: Pet[]
}

export interface DeletePetAction extends Action {
    id: number
}

export const addPet = (pet: Pet) => ({type: PetActionsTypes.ADD_PET, data: pet});
export const deletePet = (id: number) => ({type: PetActionsTypes.DELETE_PET, data: id});
export const updatePet = (id: number, pet: Pet) => ({type: PetActionsTypes.UPDATE_PET, data: {id: id, pet: pet}});
export const requestPets = () => ({type: PetActionsTypes.REQUEST_PETS});
export const receivePets = (data: Pet[]) => ({type: PetActionsTypes.RECEIVE_PETS, data: data});
export const requestPet = () => ({type: PetActionsTypes.REQUEST_PETS});
export const receivePet = (data: {}) => ({type: PetActionsTypes.RECEIVE_PETS, data: data});

export function fetchPet(id: string) {
    return (dispatch: any) => {
        //dispatch(requestPets());
        return getData(`/pets/${+id}`, {})
            .then(data => {
                data && dispatch(receivePet(data))
            })
        // notice that there is an extra "pet" in the path above which is produced
        // in this particular implementation of the service. {pet: Pet, appointments:List<AppointmentDTO>}
    }
}

export function fetchPets() {
    return (dispatch: any) => {
        dispatch(requestPets());
        return getData('/pets', [])
            .then(data => {
                data && dispatch(receivePets(data.map((p: { pet: Pet }) => p.pet)))
            })
        // notice that there is an extra "pet" in the path above which is produced
        // in this particular implementation of the service. {pet: Pet, appointments:List<AppointmentDTO>}
    }
}

export function postPet(pet: Pet) {
    return (dispatch: any) => {
        //dispatch(addPet(pet));
        return fetch('/pets', {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({id: pet.id, name: pet.name, ownerID: 2, species: pet.species})
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

export function deletePetRequest(id: number) {
    return (dispatch: any) => {

        return fetch(`/pets/${id}`, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    dispatch(deletePet(id));
                    console.log("deleted pet with id: " + id);
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

export function updatePetRequest(id: number, pet: Pet) {
    return (dispatch: any) => {

        return fetch(`/pets/${id}`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({name: pet.name, species: pet.species, id: pet.id, ownerID: 2})
        })
            .then(response => {
                if (response.ok) {
                    dispatch(updatePet(id, pet));
                    console.log("updated pet with id: " + id);
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
