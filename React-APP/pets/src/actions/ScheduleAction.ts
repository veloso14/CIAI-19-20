import {getData} from "../Utils/NetworkUtils";
import {Action} from "redux";
import {Appointment} from "../components/PetList";
import {requestPet} from "./PetActions";


export enum AppointmentActionsTypes {
    ADD_APPOINTMENT = 'ADD_APPOINTMENT',
    UPDATE_APPOINTMENT = 'UPDATE_APPOINTMENT',
    REQUEST_APPOINTMENTS = 'REQUEST_APPOINTMENTS',
    RECEIVE_APPOINTMENTS = 'RECEIVE_APPOINTMENTS',
}

export interface AddAppointmentAction extends Action {
    name: string,
    species: string
}

export interface ReceiveAppointmentsAction extends Action {
    data: Appointment[]
}

export interface ReceiveAppointmentAction extends Action {
    data: Appointment
}


export const receiveAppointments = (data: {}) => ({type: AppointmentActionsTypes.RECEIVE_APPOINTMENTS, data: data});
export const requestAppointments = () => ({type: AppointmentActionsTypes.REQUEST_APPOINTMENTS});

export function fetchVetAppointments(id: string) {
    return (dispatch: any) => {
        dispatch(requestAppointments());
        return getData(`/vets/appointments/${+id}`, [] as Appointment[])
            .then(data => {
                console.log("appointments fetch log: " + JSON.stringify(data));
                data && dispatch(receiveAppointments(data))
            })
        // notice that there is an extra "pet" in the path above which is produced
        // in this particular implementation of the service. {pet: Pet, appointments:List<AppointmentDTO>}
    }
}


