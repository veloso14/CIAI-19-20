import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Appointment} from "../components/AppointmentList";

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

export interface ReceiveAppointmentsAction extends Action {
    data: Appointment[]
}

export interface ReceiveAppointmentAction extends Action {
    data: Appointment
}

export const addAppointment = (apt: Appointment) => ({type: AppointmentActionsTypes.ADD_APPOINTMENT, data: apt});

export const requestAppointment = () => ({type: AppointmentActionsTypes.REQUEST_APPOINTMENT});
export const receiveAppointment = (data: {}) => ({type: AppointmentActionsTypes.RECEIVE_APPOINTMENT, data: data});

export const requestAppointments = () => ({type: AppointmentActionsTypes.REQUEST_APPOINTMENTS});
export const receiveAppointments = (data: {}) => ({type: AppointmentActionsTypes.RECEIVE_APPOINTMENTS, data: data});

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
