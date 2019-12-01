import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Appointment, Pet} from "../components/PetList";


export enum AppointmentActionsTypes {
    ADD_APPOINTMENT = 'ADD_APPOINTMENT',
    UPDATE_APPOINTMENT = 'UPDATE_APPOINTMENT',
    REQUEST_APPOINTMENT = 'REQUEST_APPOINTMENT',
    RECEIVE_APPOINTMENT = 'RECEIVE_APPOINTMENT',
}

export interface AddAppointmentAction extends Action {
    name: string,
    species: string
}

export interface ReceiveAppointmentAction extends Action {
    data: Appointment
}


export interface UpdatePetAction extends Action {
    data: {
        id: string
        apt: Appointment,
    }
}

export const addAppointment = (apt: Appointment) => ({type: AppointmentActionsTypes.ADD_APPOINTMENT, data: apt});
export const updateAppointment = (id: string, apt: Appointment) => ({type: AppointmentActionsTypes.UPDATE_APPOINTMENT, data: {id: id, pet: apt}});
export const requestAppointment = () => ({type: AppointmentActionsTypes.REQUEST_APPOINTMENT});
export const receiveAppointment = (data: {}) => ({type: AppointmentActionsTypes.RECEIVE_APPOINTMENT, data: data});

export function fetchAppointment(id: string) {
    return (dispatch: any) => {
        dispatch(requestAppointment());
        return getData(`/appointments/${+id}`, {})
            .then(data => {
                console.log("appointment fetch log: " + JSON.stringify(data))
                data && dispatch(receiveAppointment(data))
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
                   // dispatch(deletePet(id));
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

export function updatePetRequest(id: string, pet: Pet) {
    return (dispatch: any) => {

        return fetch(`/pets/${+id}`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({name: pet.name, species: pet.species, id: pet.id, ownerID: 2})
        })
            .then(response => {
                if (response.ok) {
                   // dispatch(updatePet(id, pet));
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
