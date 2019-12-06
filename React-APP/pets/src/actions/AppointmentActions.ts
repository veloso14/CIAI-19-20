import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Appointment} from "../components/AppointmentList";
import {Pet} from "../components/PetList";
import {updatePet} from "./PetActions";

export enum AppointmentActionsTypes {
    ADD_APPOINTMENT = 'ADD_APPOINTMENT',
    UPDATE_APPOINTMENT = 'UPDATE_APPOINTMENT',

    REQUEST_APPOINTMENT = 'REQUEST_APPOINTMENT',
    RECEIVE_APPOINTMENT = 'RECEIVE_APPOINTMENT',

    REQUEST_APPOINTMENTS = 'REQUEST_APPOINTMENTS',
    RECEIVE_APPOINTMENTS = 'RECEIVE_APPOINTMENTS',
}

export interface AddAppointmentAction extends Action {
    data: Appointment
}

export interface UpdateAppointmentAction extends Action {
    data: {
        id: string
        apt: Appointment,
    }
}

export interface ReceiveAppointmentsAction extends Action {
    data: Appointment[]
}

export interface ReceiveAppointmentAction extends Action {
    data: Appointment
}

export const addAppointment = (apt: Appointment) => ({type: AppointmentActionsTypes.ADD_APPOINTMENT, data: apt});
export const updateAppointment  = (id: string, apt: String) => ({type: AppointmentActionsTypes.UPDATE_APPOINTMENT, data: apt});
export const requestAppointment = () => ({type: AppointmentActionsTypes.REQUEST_APPOINTMENT});
export const receiveAppointment = (data: {}) => ({type: AppointmentActionsTypes.RECEIVE_APPOINTMENT, data: data});

export const requestAppointments = () => ({type: AppointmentActionsTypes.REQUEST_APPOINTMENTS});
export const receiveAppointments = (data: {}) => ({type: AppointmentActionsTypes.RECEIVE_APPOINTMENTS, data: data});

let token = localStorage.getItem('jwt');



// fetch a single appointment
export function fetchAppointment(id: string) {
    return (dispatch: any) => {
        dispatch(requestAppointment());
        return getData(`/appointments/${+id}`, {} as Appointment)
            .then(data => {
                console.log("appointment fetch log: " + JSON.stringify(data));
                data && dispatch(receiveAppointment(data))
            })
    }
}


export function updateAppointmentRequest(id: string, desc: String) {
    return (dispatch: any) => {

        return fetch(`/vets/${+id}/appointments`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': '' + token,
            },
            body: JSON.stringify(desc)
        })
            .then(response => {
                if (response.ok) {
                    dispatch(updateAppointment(id, desc));
                    console.log("updated apt with id: " + id);
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

