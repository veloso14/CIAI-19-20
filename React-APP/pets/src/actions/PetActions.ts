import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Pet} from "../components/PetList";

export const ADD_PET = 'ADD_PET';
export const DELETE_PET = 'DELETE_PET';
export const UPDATE_PET = 'UPDATE_PET';
export const REQUEST_PETS = 'REQUEST_PETS';
export const RECEIVE_PETS = 'RECEIVE_PETS';

export interface AddPetAction extends Action {
    name: string,
    species: string,
    ownerID: number
}

export interface ReceivePetAction extends Action {
    data: Pet[]
}

export interface DeletePetAction extends Action {
    id: number
}

export const addPet = (pet: Pet) => ({type: ADD_PET, data: pet});
export const deletePet = (id: number) => ({type: DELETE_PET, data: id});
export const updatePet = (id: number, pet: Pet) => ({type: UPDATE_PET, data: {id: id, pet: pet}});
export const requestPets = () => ({type: REQUEST_PETS});
export const receivePets = (data: Pet[]) => ({type: RECEIVE_PETS, data: data});

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
            body: JSON.stringify({name: pet.name, species: pet.species, id: pet.id, ownerID: pet.ownerID})
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
